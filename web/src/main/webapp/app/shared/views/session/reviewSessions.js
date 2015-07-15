'use strict';

angular.module('REALM').controller('ReviewSessionsController', function ($scope, $rootScope, AuthService, AUTH_EVENTS, $state, $http, $q,RepoService) {


    $scope.sessions=[];
    $scope.allSessions=[];
    $scope.UIStartDate=new Date;
    $scope.UIEndDate=new Date;
    $scope.filteredSessions=[];

    $scope.$watch('UIStartDate',function(){
        $scope.filteredSessions=[];
        console.log("start changed to:");
        console.log($scope.UIStartDate);
        $scope.filterSessions($scope.UIStartDate,$scope.UIEndDate);
    });
    $scope.$watch('UIEndDate',function(){
        $scope.filteredSessions=[];
        console.log("end changed to:")
        console.log($scope.UIEndDate);
        $scope.filterSessions($scope.UIStartDate,$scope.UIEndDate)
    });

    $scope.gridOptions = {
        data: 'filteredSessions',
        enableHighlighting: true,
        columnDefs: [
            //{field:'kindLabel', displayName:'label'},
            {field:'token', displayName:'token'},
            {field:'startTime', displayName:'start date'},
            {field:'duration', displayName:'duration'}

        ]
    }


    $scope.gridOptionsAll = {
        data: 'allSessions',
        enableHighlighting: true,
        columnDefs: [
            //{field:'kindLabel', displayName:'label'},
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
        console.log('in the loop!');
        callback();
    }

    $scope.run=function(iterations) {
        asyncLoop(iterations, function (loop) {
            someFunction(function (result) {
                $scope.createSessionInformation($scope.sessionsArray[loop.iteration()]);
                // log the iteration
                console.log(loop.iteration());
                // Okay, for cycle could continue
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
            $scope.allSessions.push(session);
            console.log("created session!")
           },function(error){})
    }

    RepoService.getSessionsForDeviceOwner().then(function(response){
        var sessionList=response.data;
        $scope.sessionsArray=sessionList.split(",");
        console.log($scope.sessionsArray);
        $scope.run($scope.sessionsArray.length-1);
    },function(error){
        console.log("failed to get sessions for device owner " +error.status)
    })

});