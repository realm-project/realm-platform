'use strict';

angular.module('REALM').controller('ReviewSessionsController', function ($scope, $rootScope, AuthService, AUTH_EVENTS, $state, $http, $q,RepoService) {


    $scope.sessions=[];
    $scope.filteredSessions=[];

    
    $scope.UIEndDate=new Date();
    $scope.UIStartDate=new Date();
    // set the start date 1 dat before the end date
    $scope.UIStartDate.setDate($scope.UIEndDate.getDate()-1);

    $scope.$watch('UIStartDate',function(){
        $scope.filteredSessions=[];
        $scope.filterSessions($scope.UIStartDate,$scope.UIEndDate);
    });
    $scope.$watch('UIEndDate',function(){
        $scope.filteredSessions=[];
        $scope.filterSessions($scope.UIStartDate,$scope.UIEndDate)
    });

    $scope.gridOptions = {
        data: 'filteredSessions',
        multiSelect: false,
        gridMenuShowHideColumns:false,
        columnDefs: [
            {field:'token', displayName:'Token', enableHiding:false},
            {field:'localStartTime', displayName:'Start date', enableHiding:false},
            {field:'duration', displayName:'Duration', enableHiding:false}
        ]
    };


    $scope.gridOptions.onRegisterApi = function(gridApi) {
        $scope.gridOptionsGridApi = gridApi;
    };

    $scope.gridOptionsAll = {
        data: 'sessions',
        gridMenuShowHideColumns:false,
        columnDefs: [
            {field:'token', displayName:'Token', enableHiding:false},
            {field:'localStartTime', displayName:'Start date', enableHiding:false},
            {field:'duration', displayName:'Duration', enableHiding:false}
        ]
    };

    $scope.filterSessions=function(startDate, endDate)
    {
        //console.log("filtering...")
        for (var i=0;i<$scope.sessions.length;i++)
        {
            if(moment(new Date(startDate).toISOString()).isBefore($scope.sessions[i].startTime) && moment(new Date(endDate).toISOString()).isAfter($scope.sessions[i].startTime))
            {
                $scope.filteredSessions.push($scope.sessions[i])
            }
        }

    };

    $scope.showDeviceCommands = function(){
        var selectedSession = $scope.gridOptionsGridApi.selection.getSelectedRows();

        if ( selectedSession== ""){
            //Nothing selected
            $rootScope.toggle('noSessionSelected','on');
        }else{
            // read device commands and show them
            RepoService.getDeviceCommandObjectsForSession(selectedSession[0].kindLabel).then(
                function(response){
                    console.log("This is what we recieved as DeviceCommands:");
                    console.log(response);
                }
                ,function(errorResponse){
                    $rootScope.toggle('cannotReadDeviceCommands','on');
                }
            );
        }
    };

    // read list of sessions :
    RepoService.getCoursesForTeacher().then(
        function(response){
            var courseList = response.data.substring(0,response.data.length-1).split(",");
            courseList.forEach(function(course){
                RepoService.getAsnsForCourse(course).then(
                    function(response){
                        var assignmentList = response.data.substring(0,response.data.length-1).split(",");
                        assignmentList.forEach(function(assignment){
                            RepoService.getSessionObjectsForAssignment(assignment).then(
                                function(response){
                                    for (var i=0; i<response.data.length; i++){
                                        var session={};
                                        session.kindLabel=response.data[i].loc;
                                        session.token=response.data[i].value.sessionToken;
                                        session.startTime=response.data[i].value.startTime;
                                        // convert to localTime
                                        var tempDate = moment(response.data[i].value.startTime);
                                        session.localStartTime = tempDate.year()+'/'+ tempDate.month() + '/' + tempDate.date() + ' - ' + tempDate.hour()+ ':' + tempDate.minute();
                                        session.duration=response.data[i].value.duration.toString();
                                        $scope.sessions.push(session);
                                    }
                                    // update filter after adding each assignment sessions
                                    $scope.filteredSessions=[];
                                    $scope.filterSessions($scope.UIStartDate,$scope.UIEndDate)
                                },function(errorResponse){
                                    console.log("failed to get session object for assignment");                        
                                }
                            );
                        });
                    },function(errorResponse){
                        console.log("failed to get assignment for course");            
                    }
                );
            });
        },function(errorResponse){
            console.log("failed to get courses for teacher");
        }
    );

});