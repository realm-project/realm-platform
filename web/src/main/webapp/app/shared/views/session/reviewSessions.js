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
        enableHighlighting: true,
        showGroupPanel: true,
        canSelectRows: true,
        columnDefs: [
            {field:'token', displayName:'token'},
            {field:'startTime', displayName:'start date'},
            {field:'duration', displayName:'duration'}
        ]
    }

    $scope.gridOptionsAll = {
        data: 'sessions',
        enableHighlighting: true,
        showGroupPanel: true,
        canSelectRows: true,
        columnDefs: [
            {field:'token', displayName:'token'},
            {field:'startTime', displayName:'start date'},
            {field:'duration', displayName:'duration'}
        ]
    }

    $scope.filterSessions=function(startDate, endDate)
    {
        console.log("filtering...")
        for (var i=0;i<$scope.sessions.length;i++)
        {
            var sessionStartDate=new Date;
            sessionStartDate=$scope.sessions[i].startTime;
            console.log(sessionStartDate);
            if(moment(startDate).isBefore(sessionStartDate) && moment(endDate).isAfter(sessionStartDate))
            {
                $scope.filteredSessions.push($scope.sessions[i])
            }
        }

    }

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
    }

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
    }

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