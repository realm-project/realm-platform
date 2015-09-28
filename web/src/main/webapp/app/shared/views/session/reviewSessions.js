'use strict';

angular.module('REALM').controller('ReviewSessionsController', function ($scope, $rootScope, RepoService) {


    $scope.sessions=[];
    $scope.filteredSessions=[];
    $scope.deviceCommands = [];
    $scope.selectedDeviceIODetail = "Please select a command to see details here";
    $scope.selectedSession = "";

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
        enableColumnResizing: true,
        columnDefs: [
            {field:'token', displayName:'Token', enableHiding:false},
            {field:'localStartTime', displayName:'Start date', enableHiding:false},
            {field:'duration', displayName:'Duration', enableHiding:false},
            {field:'station', displayName:'Station', enableHiding:false}
        ]
    };
    $scope.gridOptions.onRegisterApi = function(gridApi) {
        gridApi.selection.on.rowSelectionChanged($scope,function() {
            $scope.selectedSession = gridApi.selection.getSelectedRows();
        });
    };

    $scope.gridOptionsAll = {
        data: 'sessions',
        gridMenuShowHideColumns:false,
        enableColumnResizing: true,
        columnDefs: [
            {field:'token', displayName:'Token', enableHiding:false},
            {field:'localStartTime', displayName:'Start date', enableHiding:false},
            {field:'duration', displayName:'Duration', enableHiding:false},
            {field:'station', displayName:'Station', enableHiding:false}
        ]
    };

    $scope.deviceCommandGrid = {
        data: 'deviceCommands',
        multiSelect: false,
        gridMenuShowHideColumns:false,
        columnDefs: [
            {field:'command', displayName:'Name', enableHiding:false},
            {field:'localDate', displayName:'Date', enableHiding:false}
        ]
    };
    
    $scope.deviceCommandGrid.onRegisterApi = function(gridApi) {
        //$scope.deviceCommandGridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope,function() {
           
            var tempSelectedDeviceIODetail = gridApi.selection.getSelectedRows();
            if(tempSelectedDeviceIODetail == ""){
                $scope.selectedDeviceIODetail = "Please select a command to see details here";
            }else{
                $scope.selectedDeviceIODetail = JSON.stringify(tempSelectedDeviceIODetail[0].properties, null, 4);
            }
        });
    };

    $scope.filterSessions=function(startDate, endDate)
    {
        // filter between begining of the startDate to end of endDate
        startDate.setHours(0,0,0,0);
        endDate.setHours(23,59,59,999);

        for (var i=0;i<$scope.sessions.length;i++)
        {
            if(moment(startDate.toISOString()).isBefore($scope.sessions[i].startTime) && moment(endDate.toISOString()).isAfter($scope.sessions[i].startTime))
            {
                $scope.filteredSessions.push($scope.sessions[i])
            }
        }

    };

    $scope.showDeviceCommands = function(){

        if ( $scope.selectedSession== ""){
            //Nothing selected
            $rootScope.toggle('noSessionSelected','on');
        }else{
            // read commands and show them
            RepoService.getDeviceIOObjectsForSession($scope.selectedSession[0].kindLabel).then(
                function(response){
                    $scope.deviceCommands = []
                    for (var i=0; i < response.data.length; i++){
                        var tempDeviceCommand = {};
                        var jsonCommand = JSON.parse(response.data[i].value.json);
                        tempDeviceCommand.command = jsonCommand.action;
                        tempDeviceCommand.properties = jsonCommand.properties;
                        
                        var tempDate = moment(response.data[i].value.unixtime);
                        tempDeviceCommand.localDate = tempDate.year()+'/'+ tempDate.format("M") + '/' + tempDate.date() + ' - ' + tempDate.hour()+ ':' + tempDate.format("mm");
                        $scope.deviceCommands.push(tempDeviceCommand);
                    }

                    $scope.selectedDeviceIODetail = "Please select a command to see details here";   
                    $rootScope.toggle('deviceCommandsModal','on');
                }
                ,function(errorResponse){
                    $rootScope.toggle('cannotReadDeviceCommands','on');
                }
            );
        }
    };

    $scope.deleteSession = function(){

        if ($scope.selectedSession== ""){
            //Nothing selected
            $rootScope.toggle('noSessionSelected','on');
        }else{
            // Are you sure? message
            $rootScope.toggle('DeleteForSure','on');
        }
    };
    
    $scope.confirmedDeleteSession = function(){
        RepoService.removeSessions($scope.selectedSession[0].kindLabel).then(
            function(response){
                // remove selected session
                $scope.sessions = $scope.sessions.filter(function(session){
                    return session.kindLabel !== $scope.selectedSession[0].kindLabel;
                });
                 // empty $scope.selectedSession
                $scope.selectedSession = "";
                // refilter sessions
                $scope.filteredSessions=[];
                $scope.filterSessions($scope.UIStartDate,$scope.UIEndDate);
                // show message to the user
                $rootScope.toggle('successDelete','on');

            },function(errorResponse){
                $rootScope.toggle('errorDeleteSession','on');
            }
        );
    }

    $scope.editSession = function(){

        if ($scope.selectedSession== ""){
            //Nothing selected
            $rootScope.toggle('noSessionSelected','on');
        }else{

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
                                        session.station = response.data[i].value.station.loc;
                                        session.startTime=response.data[i].value.startTime;
                                        // convert to localTime
                                        var tempDate = moment(response.data[i].value.startTime);
                                        session.localStartTime = tempDate.year()+'/'+ tempDate.format("M") + '/' + tempDate.date() + ' - ' + tempDate.hour()+ ':' + tempDate.format("mm");
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