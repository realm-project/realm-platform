'use strict';

angular.module('REALM')
    .controller('TeacherMySessionsController', function ($scope, $rootScope, AuthService, AUTH_EVENTS, $state, $http, $q, RepoService) {




        var user = RepoService.getObject("Person", AuthService.getCurrentUser().loc)
        user.then(
            function (response) {
                var currentUser = response.data;
                $scope.name=currentUser.value.name;
            }, function (error) {
                console.log("Unable to get user information")
            });


        $scope.sessionTokenString = "";
        //dynamic sessions include the session information ready to be presented
        $scope.dynamicSessions = [];
        //$scope.firstName=AuthService.getCurrentUser().value.firstName;
        var sessions = RepoService.getSessionsFromUser();
        readSessions(sessions);
//-----------------------------------------------------------------------------------------------
        function readSessions(sessions) {
            // arash kh: also check sessions.value 
        	if (sessions !== null && sessions !== "" && sessions !== undefined && sessions.value!= "" ) {
                $scope.dynamicSessions = [];
                var sessionValue = sessions.value;
                console.log("sessions are not null")
                //loops through the sessions, create session info from session data, and add them to dynamicSessions
                for (var i = 0; i < sessionValue.length; i++) {
                    RepoService.getObject("Session", sessionValue[i].loc).then(
                        function (response) {
                            var sessionData = response.data;
                            $scope.createSession(sessionData);
                        }, function (error) {
                            console.log(error.status);
                        });
                }//end of for
            }//end of if
        }//end of function
//---------------------------------------------------------------------------------------
        /**
         * Updates dynamicSessions, which is an array of user's session objects with the required attributes
         * @param {object} returnedSession The session object pulled out from the server
         */
        $scope.createSession = function (returnedSession) {
            var assignmentLocation = RepoService.getAssignmentFromSession(returnedSession);
            var returnedAssignment = RepoService.getObject("Assignment", assignmentLocation);
            returnedAssignment.then(
                function (assignmentResponse) {
                    var deviceUI = RepoService.getObject("deviceUI", assignmentResponse.data.value.deviceUI.loc)
                    deviceUI.then(
                        function (deviceUIResponse) {
                            var course=RepoService.getObject("course",assignmentResponse.data.value.course.loc)
                            course.then(
                                function(courseResponse){
                                    var station=RepoService.getObject("station",returnedSession.value.station.loc);
                                    station.then(
                                        function(stationResponse)
                                        {
                                            var session = new Object();
                                            var name = assignmentResponse.data.value.name;
                                            var start = new Date(returnedSession.value.startTime);
                                            var startMili = start.getTime();
                                            var durationMili = returnedSession.value.duration * 60 * 1000;
                                            var endDateMili = startMili + durationMili;
                                            var endDate = new Date(endDateMili);
                                            name = name.replace(/\b./g, function (m) {return m.toUpperCase();});
                                            session.assignment = name;
                                            session.order = $scope.setOrder(name);
                                            session.startDateTime = returnedSession.value.startTime;
                                            session.endDateTime = endDate.toISOString();
                                            session.fromNow = null;
                                            session.isActive = null;
                                            session.station=stationResponse.data.value.devices.value;

                                            //get information from deviceUI
                                            session.experimentType = deviceUIResponse.data.value.url;
                                            session.layout=deviceUIResponse.data.value.layout;


                                            //get information from course
                                            session.course = courseResponse.data.value.name;

                                            //update dynamic sessions and order
                                            $scope.dynamicSessions.push(session); //add processed session data to dynamicSessions
                                            $scope.date(); //update dates for the data
                                            $scope.dynamicSessions.sort(function (a, b) {
                                                return parseFloat(a.order) - parseFloat(b.order)
                                            }); //order sessions
                                            console.log($scope.dynamicSessions);

                                        },
                                        function(error)
                                        {
                                        	if(typeof(error.status) !=="undefined"){
                                        		console.log("failed to get station information: "+error.status);
                                        	}else if(typeof(error.message) !=="undefined"){
                                        		console.log("failed to get station information:; " + error.message);
                                        	}
                                            $rootScope.toggle('loadSessionError','on');
                                        }
                                    )//end of station argumets
                                  },
                                function(error)
                                {
                                    if(typeof(error.status) !=="undefined"){
                                        console.log(error.status)
                                    }
                                    console.log("failed to get course information");
                                    $rootScope.toggle('loadSessionError','on');
                                }
                            )
                        },
                        function (error) {
                            if(typeof(error.status) !=="undefined"){
                                console.log(error.status)
                            }
                            console.log("failed to get deviceUI information");
                            $rootScope.toggle('loadSessionError','on');
                        }
                    )
                },
                function (error) {
                    if(typeof(error.status) !=="undefined"){
                        console.log(error.status)
                    }
                    console.log("failed to get assignment information");
                    $rootScope.toggle('loadSessionError','on');
                }
            )//end of promise checking
        } //end of function
        //---------------------------------------------------------------------------------
        $scope.date = function () {
            for (var i = 0; i < $scope.dynamicSessions.length; i++) {
                $scope.dynamicSessions[i].fromNow = moment(new Date($scope.dynamicSessions.startDateTime)).fromNow();
                if (moment().isAfter($scope.dynamicSessions[i].startDateTime) && moment().isBefore($scope.dynamicSessions[i].endDateTime)) {
                    $scope.dynamicSessions[i].isActive = true;
                    $scope.dynamicSessions[i].fromNow = 'Active Now!'
                }
                else {
                    $scope.dynamicSessions[i].isActive = false;
                }
            }//end of for dynamic sessions

        }
        //------------------------------------------------------------------------------
        $scope.setOrder = function (name) {
            switch (name) {
                case "Teach Points":
                    return 1;
                case "Forward Kinematics":
                    return 2;
                case "Inverse Kinematics":
                    return 3;
                default:
                    return 4;
            }
        }
//----------------------------------------------------------------------------
        $scope.addUserToSession = function (sessionToken) {
            var joinSession = RepoService.addUserToSession(sessionToken);
            joinSession.then(
                function (response) {
                    var user = RepoService.getObject("Person", AuthService.getCurrentUser().loc)
                    user.then(
                        function (response) {
                            var currentUser = response.data;
                            var sessions = currentUser.value.sessions;
                            readSessions(sessions);
                            AuthService.refreshCurrentUser();
                            $rootScope.toggle("addSessionOverlay", "off");

                        }, function (error) {
                            console.log("cannot get user informations");
                        })
                },
                function (error) {
                    console.log("Unable to add user to session")
                    if(error.data.message=="Invalid session ID"){
                        $rootScope.toggle("addSessionOverlay", "off");
                        $rootScope.toggle('addSessionInvalidToken','on');

                    }else if (error.data.message=="Session is already added"){
                        $rootScope.toggle("addSessionOverlay", "off");
                        $rootScope.toggle('addSessionUsedToken','on');

                    }else{
                        $rootScope.toggle("addSessionOverlay", "off");
                        $rootScope.toggle('addSessionFailedOverlay','on');
                    }
                }
            );

        }
//----------------------------------------------------------------------------
        $scope.openAddSessionOverlay = function () {
            $scope.sessionTokenString = "";
            $rootScope.toggle('addSessionOverlay', 'on');
        }
//----------------------------------------------------------------------------
        /**
         * Given a URL, it first populate the route definition and then matches the path to that definition
         * @param {String} experimentType The URL pulled out from deviceUI object
         */
        $scope.goToExperiment = function (experimentType,layout,station) {

                var stateChecker=false;
                for(var i=0;i<$state.get().length;i++)
                {
                    if("teacherHome.mySessions."+experimentType==$state.get()[i].name)
                    {
                        console.log("the state already exists!")
                        stateChecker=true;
                    }
                }
                if(!stateChecker)
                {
                    console.log("state is new and is being created!");
                    app.stateProvider.state("teacherHome.mySessions."+experimentType,
                        {
                            url: '/' + experimentType,
                            controller: 'ExperimentTemplateController',
                            templateUrl: 'shared/views/experiment/experimentTemplate.html',
                            data: {authorizedRoles:['teacher'],pageName:"Experiment"}
                        });
                }

           /*var station={};
           station.camera="rest/device/camera";
           station.robot="rest/device/mico";*/
           //layout=$scope.updateLayout($.parseJSON(layout),station);
           layout = $.parseJSON(layout);
           localStorage.setItem('layout',JSON.stringify(layout));
           $state.go("teacherHome.mySessions."+experimentType);
        }
//-----------------------------------------------------------------------------
        /**
         * It takes layout and a station object and updates the layout object with station URLs
         * @param {object} layout
         * @param {object} station
         * @return {object} updated layout
         */
        $scope.updateLayout = function (layout, station) {
            var sections = layout.sections;
            for (var i = 0; i < sections.length; i++) {
                var components = sections[i].components;
                for (var j = 0; j < components.length; j++) {
                    if (components[j].componentOptions.hasOwnProperty("device")) {
                            var type=components[j].componentOptions.device;
                            layout.sections[i].components[j].componentOptions.url=station[type];
                            console.log(components[j].componentOptions.url);
                    }
                    else
                    {
                        console.log("Error: device information is undefined!")
                    }
                }
            }
            console.log(layout);
            return layout;
        }
    });