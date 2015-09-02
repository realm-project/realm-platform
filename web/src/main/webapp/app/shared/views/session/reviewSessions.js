'use strict';

angular.module('REALM').controller('ReviewSessionsController', function ($scope, $rootScope, AuthService, AUTH_EVENTS, $state, $http, $q,RepoService) {


    $scope.sessions=[];
    $scope.filteredSessions=[];

    $scope.UIStartDate=new Date;
    $scope.UIEndDate=new Date;

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
            {field:'token', displayName:'token', enableHiding:false},
            {field:'startTime', displayName:'start date', enableHiding:false},
            {field:'duration', displayName:'duration', enableHiding:false}
        ]
    };


    $scope.gridOptions.onRegisterApi = function(gridApi) {
        $scope.gridOptionsGridApi = gridApi;
    };

    $scope.gridOptionsAll = {
        data: 'sessions',
        gridMenuShowHideColumns:false,
        columnDefs: [
            {field:'token', displayName:'token', enableHiding:false},
            {field:'startTime', displayName:'start date', enableHiding:false},
            {field:'duration', displayName:'duration', enableHiding:false}
        ]
    };

    $scope.filterSessions=function(startDate, endDate)
    {
        console.log("filtering...")
        for (var i=0;i<$scope.sessions.length;i++)
        {
            var sessionStartDate=new Date;
            sessionStartDate=$scope.sessions[i].startTime;
            if(moment(startDate).isBefore(sessionStartDate) && moment(endDate).isAfter(sessionStartDate))
            {
                $scope.filteredSessions.push($scope.sessions[i])
            }
        }

    };

    function asyncLoop(iterations, func, callback) {
        var index = 0;
        var done = false;
        var loop = {
            next: function() {
                if (done) {
                    return;
                }
                if (index < iterations) {
                    index++;
                    func(loop);
                } else {
                    done = true;
                    callback();
                }
            },
            iteration: function() {
                return index - 1;
            },
            break: function() {
                done = true;
                callback();
            }
        };
        loop.next();
        return loop;
    }
    function someFunction(callback) {
        //console.log('in the loop!');
        callback();
    }

    $scope.run=function(iterations) {
        asyncLoop(iterations, function (loop) {
            someFunction(function (result) {
                $scope.createSessionInformation($scope.sessionsArray[loop.iteration()]);
                loop.next();
            })
        },function () {
            console.log('cycle ended')
        });
    };

    $scope.createSessionInformation=function(sessionLocation)
    {
        RepoService.getObject("Session",sessionLocation).then(function(response){
            var session={};
            session.kindLabel=response.data.loc;
            session.token=response.data.value.sessionToken;
            session.startTime=response.data.value.startTime;
            session.duration=response.data.value.duration.toString();
            $scope.sessions.push(session);
            
           },function(error){
                console.log("error in creating session!");
           }
        );
    };

    $scope.showDeviceCommands = function(){
        var selectedSession = $scope.gridOptionsGridApi.selection.getSelectedRows();

        if ( selectedSession== ""){
            //Nothing selected
            $rootScope.toggle('noSessionSelected','on');
        }else{
            // read device commands and show them
            RepoService.getDeviceCommandsForSession(selectedSession[0].kindLabel).then(
                function(response){
                    console.log(response);
                    alert('hi');
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
                RepoService.getSessionsForCourse(course).then(
                    function(response){
                        $scope.sessionsArray = response.data.substring(0,response.data.length-1).split(",");
                        $scope.run($scope.sessionsArray.length);
                    },function(errorResponse){
                        console.log("failed to get sessions for course");            
                    }
                );
            });
        },function(errorResponse){
            console.log("failed to get courses for teacher");
        }
    );

});