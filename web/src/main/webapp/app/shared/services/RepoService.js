'use strict';

angular.module('REALM')
	.service('RepoService', function ($http, $q, AuthService, AUTH_EVENTS, $state, $rootScope) {


	// disabling the handleError will unstable the client side UI, it is strongly recommended to set it to true.
	var handleError = true;

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
		}, function (errorResponse) {
			console.log('Failed to get ' + type + ' info');
			console.log(errorResponse);

			if (handleError){
				resolveError(errorResponse);
			}else{
				data.reject(errorResponse);
			}
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

			}, function (errorResponse) {
				console.log('Failed to add user to session');
				console.log(errorResponse);
				if (handleError){
					// send known errors to the client side
					if (errorResponse.data !== undefined && errorResponse.data.message !== undefined && (errorResponse.data.message==="Invalid session ID" || errorResponse.data.message==="Session is already added")){
						join.reject(errorResponse);
					}else{
						resolveError(errorResponse);
					}
				}else{
					join.reject(errorResponse);
				}
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

			}, function (errorResponse) {
				console.log('Failed to get sessions per owner per device');
				if (handleError){
					resolveError(errorResponse);
				}else{
					sessions.reject(errorResponse);
				}
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
			}, function (errorResponse) {
				console.log('Failed to get courses for teacher');
				console.log(errorResponse);
				if (handleError){
					resolveError(errorResponse);
				}else{
					courses.reject(errorResponse);
				}
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
				if (handleError){
					resolveError(errorResponse);
				}else{
					assignments.reject(errorResponse);
				}
			});
		return assignments.promise;
	};

	this.createSessions = function (data) {
		//add session path and token
		var defer = $q.defer();
		var apiPath = localStorage.basePath + 'rest/api/teacher/createSession';

		$http.post(apiPath, data).then(
			function (response) {
				defer.resolve(response);

			}, function (errorResponse) {
				console.log('Failed to create sessions');
				console.log(errorResponse);
				if (handleError){
					resolveError(errorResponse);
				}else{
					defer.reject(errorResponse);
				}
			});
		return defer.promise;
	};

	this.removeSessions = function (sessionID) {
		var defer = $q.defer();
		var apiPath = localStorage.basePath + 'rest/api/teacher/removeSession';
		var postData = {
			"session":sessionID
		}

		$http.post(apiPath, postData).then(
			function (response) {
				defer.resolve(response);

			}, function (errorResponse) {
				console.log('Failed to delete session');
				console.log(errorResponse);
				// 403 is a known error when the user does not have the access to delete the session
				// in this case we reject the promise and the client side will handle it
				// any other kind of error considered as unknown error and we redirect it to resolveError function
				if (errorResponse.status !== undefined && errorResponse.status == 403) {
					defer.reject(errorResponse);
				}else {
					if (handleError){
						resolveError(errorResponse);
					}else{
						defer.reject(errorResponse);
					}
				}
			});
		return defer.promise;
	};

	this.editSessions = function (sessionID, startTime, duration) {
		var defer = $q.defer();
		var apiPath = localStorage.basePath + 'rest/api/teacher/editSession';
		var postData = {
			"session":sessionID,
			"startTime": startTime,
			"duration": duration
		}

		$http.post(apiPath, postData).then(
			function (response) {
				defer.resolve(response);

			}, function (errorResponse) {
				console.log('Failed to delete session');
				console.log(errorResponse);
				// 403 is a known error when the user does not have the access to edit the session
				if (errorResponse.status !== undefined && errorResponse.status == 403){
					defer.reject(errorResponse);
				}else {
					if (handleError){
						resolveError(errorResponse);
					}else{
						defer.reject(errorResponse);
					}
				}
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
				if (handleError){
					resolveError(errorResponse);
				}else{
					defer.reject(errorResponse);
				}
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
				if (handleError){
					resolveError(errorResponse);
				}else{
					defer.reject(errorResponse);
				}
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
				if (handleError){
					resolveError(errorResponse);
				}else{
					defer.reject(errorResponse);
				}
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
				if (handleError){
					resolveError(errorResponse);
				}else{
					defer.reject(errorResponse);
				}
			});
		return defer.promise;
	}

	this.getDeviceIOObjectsForSession = function(sessionID){
		var defer = $q.defer();
		var apiPath = localStorage.basePath + 'rest/api/common/getDeviceIOObjectsForSession';
		var postData = {
			"session":sessionID
		}
		$http.post(apiPath, postData).then(
			function (response) {
				defer.resolve(response);

			}, function (errorResponse) {
				console.log('Failed to get deviceIO objects for session');
				console.log(errorResponse);
				console.log(errorResponse);
				if (handleError){
					resolveError(errorResponse);
				}else{
					defer.reject(errorResponse);
				}
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
				console.log(errorResponse);
				if (handleError){
					resolveError(errorResponse);
				}else{
					defer.reject(errorResponse);
				}
			});
		return defer.promise;
	}

	// in case of handleError == true, we do not resolve the promise and call resolveError function
	function resolveError(errorResponse){
		if (errorResponse.status !== undefined && errorResponse.status == 401){
			// timeout error
			$state.go('login');
			$rootScope.toggle('generalTimeoutError','on');
		}else{
			// other errors
			$state.go('login');
			$rootScope.toggle('generalError','on');
		}
	}
});

