'use strict';

angular.module('REALM')
	.service('RepoService', function ($http, $q, AuthService, AUTH_EVENTS) {


	//give a user returns an array of sessions
	this.getSessionsFromUser = function () {
		return AuthService.getCurrentUser().value.sessions;
	};


	this.getAssignmentFromSession = function (session) {
		return session.value.assignment.loc;
	};


	//given a session location, return the session json
	this.getObject = function (type, location) {
		var data = $q.defer();
		var dataPath = localStorage.basePath + 'rest/repo/' + type + '/' + location + ".json";
		$http.get(dataPath).then(function (response) {
			var info = response;
			data.resolve(info);
		}, function (response) {
			console.log('Failed to get ' + type + ' info, error code: ');
			if(typeof(response.status) !=="undefined"){
				console.log(response.status);
			}else{
				console.log(response.message);
			}
			data.reject(response);
			
		});
		return data.promise;
	};

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
				join.reject(response);
			});
		return join.promise;
	};

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

		$http.post(apiPath, postData).then(
			function (response) {
				console.log(response);
				sessions.resolve(response);

			}, function (response) {
				console.log('Failed to get sessions per owner per device, error code: ' + response.status);
				sessions.reject();
			});
		return sessions.promise;
	};

	this.getCoursesForTeacher = function () {
		//add session path and token
		var courses = $q.defer();
		var apiPath = localStorage.basePath + 'rest/api/teacher/getCoursesForTeacher';
		var postData = {
		}
		$http.post(apiPath, postData).then(
			function (response) {
				courses.resolve(response);
			}, function (response) {
				console.log('Failed to get courses for teacher, error code: ' + response.status);
				courses.reject();
			});
		return courses.promise;
	};

	this.getAsnsForCourse = function (course) {
		//add session path and token
		var assignments = $q.defer();
		var apiPath = localStorage.basePath + 'rest/api/teacher/getAsnsForCourse';
		var postData = {
			"course":course
		}
		$http.post(apiPath, postData).then(
			function (response) {
				assignments.resolve(response);

			}, function (errorResponse) {
				console.log('Failed to get assignments for course');
				console.log(errorResponse);
				assignments.reject();
			});
		return assignments.promise;
	};

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
				defer.reject();
			});
		return defer.promise;
	};

	this.getSessionsForCourse = function(course){
		var defer = $q.defer();
		var apiPath = localStorage.basePath + 'rest/api/teacher/getSessionsForCourse';
		var postData = {
			"course":course
		}
		$http.post(apiPath, postData).then(
			function (response) {
				defer.resolve(response);

			}, function (errorResponse) {
				console.log('Failed to get sessions for course');
				console.log(errorResponse);
				defer.reject();
			});
		return defer.promise;
	};

	this.getStationsForTeacher = function(teacher){
		var defer = $q.defer();
		var apiPath = localStorage.basePath + 'rest/api/teacher/getStationsForTeacher';
		var postData = {
			"teacher":teacher
		}
		$http.post(apiPath, postData).then(
			function (response) {
				defer.resolve(response);

			}, function (errorResponse) {
				console.log('Failed to get stations for teacher');
				console.log(errorResponse);
				defer.reject();
			});
		return defer.promise;
	};


	this.getDeviceCommandsForSession = function(sessionID){
		var defer = $q.defer();
		var apiPath = localStorage.basePath + 'rest/api/common/getDeviceCommandsForSession';
		var postData = {
			"session":sessionID
		}
		$http.post(apiPath, postData).then(
			function (response) {
				defer.resolve(response);

			}, function (errorResponse) {
				console.log('Failed to get list of device commands for session');
				console.log(errorResponse);
				defer.reject();
			});
		return defer.promise;
	}

	this.getDeviceCommandObjectsForSession = function(sessionID){
		var defer = $q.defer();
		var apiPath = localStorage.basePath + 'rest/api/common/getDeviceCommandObjectsForSession';
		var postData = {
			"session":sessionID
		}
		$http.post(apiPath, postData).then(
			function (response) {
				defer.resolve(response);

			}, function (errorResponse) {
				console.log('Failed to get device command objects for session');
				console.log(errorResponse);
				defer.reject();
			});
		return defer.promise;
	}

	this.getSessionObjectsForAssignment = function(assignmentID){
		var defer = $q.defer();
		var apiPath = localStorage.basePath + 'rest/api/teacher/getSessionObjectsForAssignment';
		var postData = {
			"assignment":assignmentID
		}
		$http.post(apiPath, postData).then(
			function (response) {
				defer.resolve(response);

			}, function (errorResponse) {
				console.log('Failed to get session objects for assignment');
				console.log(errorResponse);
				defer.reject();
			});
		return defer.promise;
	}

});

