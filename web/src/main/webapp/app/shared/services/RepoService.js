'use strict';

angular.module('REALM')
    .service('RepoService', function ($http, $q, AuthService, AUTH_EVENTS) {

        // var that=this;
//-----------------------------------------------------------
        //give a user returns an array of sessions
        this.getSessionsFromUser = function () {
            return AuthService.getCurrentUser().value.sessions;
        }
//-----------------------------------------------------------
        this.getAssignmentFromSession = function (session) {
            return session.value.assignment.loc;
        }
//-----------------------------------------------------------
        //given a session location, return the session json
        this.getObject = function (type, location) {
            var data = $q.defer();
            var dataPath = localStorage.basePath + 'rest/repo/' + type + '/' + location + ".json";
            $http.get(dataPath).then(function (response) {
                var info = response;
                data.resolve(info);
            }, function (response) {
                console.log('Failed to get ' + type + ' info, error code: ');
                // arash kh
                if(typeof(response.status) !=="undefined"){
                	console.log(response.status);
                }else{
                	console.log(response.message);
                }
                data.reject(response);
                
            });
            return data.promise;
        };
//------------------------------------------------------------------
        this.addUserToSession = function (token) {
            //add session path and token
            var join = $q.defer();
            var apiPath = localStorage.basePath + 'rest/api/common/joinSession';
            var sessionToken =
            {
                "token": token
            }
            $http.post(apiPath, sessionToken).then(
                function (response) {
                    console.log("Added user to session");
                    join.resolve(response);

                }, function (response) {
                    console.log('Failed to add user to session, error code: ' + response.status);
                    join.reject()
                });
            return join.promise;
        }
//-----------------------------------------------------------------
        this.getSessionsForDeviceOwner = function (device) {
            //add session path and token
            var sessions = $q.defer();
            var apiPath = localStorage.basePath + 'rest/api/teacher/getSessionsForDeviceOwner';
            var postData =
            {
                "owner":AuthService.getCurrentUser().loc
            }
            if (device !== null && device !== "" && device !== undefined) {
                postData =
                {
                    "owner":AuthService.getCurrentUser().loc,
                    "device": device
                }
            }
            console.log(apiPath);
            console.log(postData)
            $http.post(apiPath, postData).then(
                function (response) {
                    console.log(response);
                    sessions.resolve(response);

                }, function (response) {
                    console.log('Failed to get sessions per owner per device, error code: ' + response.status);
                    sessions.reject()
                });
            return sessions.promise;
        }
//-----------------------------------------------------------------
        this.getCoursesForTeacher = function () {
            //add session path and token
            var courses = $q.defer();
            var apiPath = localStorage.basePath + 'rest/api/teacher/getCoursesForTeacher';
            var postData = {
            }
            console.log(apiPath);
            console.log(postData)
            $http.post(apiPath, postData).then(
                function (response) {
                    console.log(response);
                    courses.resolve(response);

                }, function (response) {
                    console.log('Failed to get courses for teacher, error code: ' + response.status);
                    courses.reject()
                });
            return courses.promise;
        }
//-----------------------------------------------------------------------------
        this.getAsnsForCourse = function (course) {
            //add session path and token
            var assignments = $q.defer();
            var apiPath = localStorage.basePath + 'rest/api/teacher/getAsnsForCourse';
            var postData = {
                "course":course
            }
            console.log(apiPath);
            console.log(postData)
            $http.post(apiPath, postData).then(
                function (response) {
                    console.log(response);
                    assignments.resolve(response);

                }, function (response) {
                    console.log('Failed to get assignments for course, error code: ' + response.status);
                    assignments.reject()
                });
            return assignments.promise;
        }
//--------------------------------------------------------------------------------
        this.createSessions = function (data) {
            //add session path and token
            var defer = $q.defer();
            var apiPath = localStorage.basePath + 'rest/api/teacher/createSession';

            $http.post(apiPath, data).then(
                function (response) {
                    console.log("created sessions");
                    defer.resolve(response);

                }, function (response) {
                    console.log('Failed to create sessions: ' + response.status);
                    defer.reject()
                });
            return defer.promise;
        }
//-------------------------------------------------------------------------------
    });
//----------------------------------------------------------------
