'use strict';

angular.module('REALM')
.controller('TeacherSessionsController', function ($scope, $rootScope, AuthService, AUTH_EVENTS, $state, $http, $q,RepoService) {


    // show the bottom Navbar on mobile devices
    $rootScope.mainScope.bottomNavbarCollapse = false;
    $scope.$on("$destroy", function(){
        $rootScope.mainScope.bottomNavbarCollapse = true;
    });

    $scope.assignments=[];
    $scope.stations = [];
    $scope.vm={
        sessionTimesType:'Single'
    };
    $scope.assignmentArray=[];

    $scope.childModel = {
        rangeTabIsActive : false
    };

    // create the list of stations
    RepoService.getStationsForTeacher(AuthService.getCurrentUser().loc).then(function(response){
        // make array of stations
        var stationArray = response.data.substring(0, response.data.length - 1).split(",");
        for (var i=0 ; i < stationArray.length; i++){
            RepoService.getObject("Station", stationArray[i]).then(function(response){
                $scope.stations.push(response.data);
            },function(errorResponse){
                $rootScope.toggle('loadStationsError','on');
            });
        }
    },function(errorResponse){
        $rootScope.toggle('loadStationsError','on');
    });

    // create the list of assignments
    RepoService.getCoursesForTeacher().then(function(response){
        var courseArray = response.data.substring(0,response.data.length-1).split(",");
        
        if (courseArray.length===0){
            $rootScope.toggle('noCourseError','on');
        }

        // to understand the asyncLoop look at: http://stackoverflow.com/questions/4288759/asynchronous-for-cycle-in-javascript
        asyncLoop(courseArray.length, function(loop){
            RepoService.getAsnsForCourse(courseArray[loop.iteration()]).then(function(response){
                // add new assignments to the asssignmentArray
                $scope.assignmentArray = $scope.assignmentArray.concat(response.data.split(','));
                loop.next();
            },function(errorResponse){
                $rootScope.toggle('loadAssignmentsError','on');
                loop.next();
            });

        },function(){
            if ($scope.assignmentArray.length===0){
                $rootScope.toggle('noAssignmentError','on');
            }else{
                // start the run function to load assignments.
                $scope.run($scope.assignmentArray.length-1);
            }
        });        
  
    },function(errorResponse){
        $rootScope.toggle('loadAssignmentsError','on');
    });



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

                    $scope.readAssignmentInformation($scope.assignmentArray[loop.iteration()],loop.iteration());
                    // log the iteration
                    //console.log(loop.iteration());
                    // Okay, for cycle could continue
                    loop.next();
                })
            },
            function () {
                console.log('cycle ended')
            }
        );
    }

    $scope.readAssignmentInformation=function(assignmentLocation, index) {
        RepoService.getObject("Assignment", assignmentLocation).then(function (response) {

            var assignment = {};
            assignment.kindLabel = response.data.loc;
            assignment.name = response.data.value.name;
            $scope.assignments.push(assignment);
            if(index=$scope.assignmentArray.length-1)
            {
                $scope.initialize();
            }
        },function(error)
        {
            console.log("failed to get assignment information " +error.status);
        });
    }




$scope.initialize=function(assignmentNames)
{
    var assignmentNames=[];
    for (var i=0;i<$scope.assignments.length;i++)
        assignmentNames.push($scope.assignments[i].name)

    $scope.vm = {
        assignmentTypes: assignmentNames,
        chosenAssignmentType:$scope.assignments[0].name,
        stationTypesRadioModel: $scope.stations,
        selectedStation: $scope.stations[0].loc,
        chosenStationType: $scope.stations[0],

        sessionTimesType:'Single',
        sessionStartTime:new Date(),
        sessionEndTime:new Date(),
        duration:60,
        sessionDatesType:'Range',
        now:new Date(),
        sessionStartDate:new Date(),
        sessionEndDate:new Date(),
        dateList:[new Date()],
        dateToAdd: new Date(),
        days: {"Monday":false, "Tuesday":false, "Wednesday":false, "Thursday":false, "Friday":false},
        isPopupCalendarOpen:false
    }
    $scope.isOpened = false;

}//end of initialize


        $scope.assignmentChanged = function(assignment) {
            $scope.vm.chosenAssignmentType = assignment;
        }

        $scope.stationChanged = function(station) {
            $scope.vm.chosenStationType = station;
        }

        $scope.startTimeChanged = function() {

        }

        $scope.singleSessionTabSelected = function()
        {
            $scope.vm.sessionTimesType = 'Single';
            $scope.childModel.rangeTabIsActive = true;
        }

        $scope.bulkSessionTabSelected = function()
        {
            $scope.vm.sessionTimesType = 'Bulk';
        }

        $scope.dateRangeTabSelected = function()
        {
            $scope.vm.sessionDatesType = 'Range';
        }

        $scope.dateListTabSelected = function()
        {
            $scope.vm.sessionDatesType = 'List';
        }

        $scope.open = function()
        {
            $scope.isOpened = true;
        }

        $scope.appendDateList = function()
        {
            var decisionBool = true;
            for(var index=0; index<$scope.vm.dateList.length; ++index)
            {
                if($scope.vm.dateList[index].toDateString() === $scope.vm.dateToAdd.toDateString())
                    decisionBool = false;
            }
            if(decisionBool)
            {
                $scope.vm.dateList.push($scope.vm.dateToAdd);
            }
            else
            {
                console.log('duplicate date caught');
            }
        }

        $scope.toggleCal = function()
        {
            $scope.isOpened = !$scope.isOpened;
        }


        // main function to craete new sessions
        $scope.createSessions = function()
        {
           
            var postData = {
                "assignment":"",
                "station":"",
                "time":{},
                "date":{}
            };

            // station
            postData.station = $scope.vm.chosenStationType.loc;

            //Assignment
            for (var i=0;i<$scope.assignments.length;i++)
            {
                if($scope.assignments[i].name==$scope.vm.chosenAssignmentType)
                   postData.assignment=$scope.assignments[i].kindLabel;
            }

            //Session Times
            //Sanitizing Times
            var startTime = $scope.vm.sessionStartTime;
            var startTimeHours = startTime.getHours().toString();
            if(startTimeHours.length < 2)
                startTimeHours = "0" + startTimeHours;
            var startTimeMinutes = startTime.getMinutes().toString();
            if(startTimeMinutes.length < 2)
                startTimeMinutes = "0"+ startTimeMinutes;

            startTime = startTimeHours + ":" + startTimeMinutes;

            //Sanitizing Times
            var endTime = $scope.vm.sessionEndTime;
            var endTimeHours = endTime.getHours().toString();
            if(endTimeHours.length < 2)
                endTimeHours = "0" + endTimeHours;
            var endTimeMinutes = endTime.getMinutes().toString();
            if(endTimeMinutes.length < 2)
                endTimeMinutes = "0"+ endTimeMinutes;

            endTime = endTimeHours + ":" + endTimeMinutes;
            //Single Session Creation
            if($scope.vm.sessionTimesType==='Single')
            {
                postData.time.single={};
                postData.time.single.start=startTime;
                postData.time.single.duration=$scope.vm.duration;
            }
            //Bulk Session Creation
            else if($scope.vm.sessionTimesType==='Bulk')
            {
                postData.time.bulk={};
                postData.time.bulk.start=startTime;
                postData.time.bulk.end=endTime;
                postData.time.bulk.duration=$scope.vm.duration;
            }
            //Session Dates
            //Sanitizing Output
            var startDate = $scope.vm.sessionStartDate;
            var startDateYear = startDate.getFullYear();
            var startDateMonth = startDate.getMonth().toString();
            var startDateMonthInt=parseInt(startDateMonth)+1;
            startDateMonth=startDateMonthInt.toString();
            if(startDateMonth.length < 2)
                startDateMonth = "0" + startDateMonth;
            var startDateDate = startDate.getDate().toString();
            if(startDateDate.length < 2)
                startDateDate = "0" + startDateDate;

            startDate = startDateYear + "-" + startDateMonth + "-" + startDateDate;

            //Sanitizing Output
            var endDate = $scope.vm.sessionEndDate;
            var endDateYear = endDate.getFullYear();
            var endDateMonth = endDate.getMonth().toString();
            var endDateMonthInt=parseInt(endDateMonth)+1;
            endDateMonth=endDateMonthInt.toString();
            if(endDateMonth.length < 2)
                endDateMonth = "0" + endDateMonth;
            var endDateDate = endDate.getDate().toString();
            if(endDateDate.length < 2)
                endDateDate = "0" + endDateDate;

            endDate = endDateYear + "-" + endDateMonth + "-" + endDateDate;

            //Date Range
            if($scope.vm.sessionDatesType==='Range')
            {
                postData.date.range={};
                postData.date.range.start= startDate;
                postData.date.range.end= endDate;

                var days=[];

                if($scope.vm.sessionTimesType==='Single'){
                    // ignore week days and send an empty array on single sessions
                }else if($scope.vm.sessionTimesType==='Bulk'){
                    // create week days list for Bulk sessions
                    for(var day in $scope.vm.days)
                    {
                        if($scope.vm.days[day])
                        {
                            days.push(day);
                        }
                    }
                }

                postData.date.range.days=days;
            }
            //Date List
            else if($scope.vm.sessionDatesType==='List')
            {
                postData.date.list = [];

                for(var i=0; i<$scope.vm.dateList.length; i++)
                {
                    var year;
                    var month;
                    var date;

                    year = $scope.vm.dateList[i].getFullYear();
                    month = $scope.vm.dateList[i].getMonth();
                    date = $scope.vm.dateList[i].getDate();

                    var dateString = year + "-" + month + "-" + date;

                    postData.date.list.push(dateString);
                }
            }
            //Final Shipment
           
            RepoService.createSessions(postData).then(function(response){
                if (response.data==='0'){
                    $rootScope.toggle('createSessionZero','on');
                }else{
                    $rootScope.toggle('createSessionSuccess','on');
                }
            },function(error){
                console.log("failed to create sessions");
                console.log(error);
                $rootScope.toggle('createSessionError','on');
            })

        }//end of createSessions
//---------------------------------------------------------------------------------

});