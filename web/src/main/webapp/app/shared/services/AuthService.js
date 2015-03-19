'use strict';

angular.module('REALM')
    .service('AuthService',function($http, $q, $cookies, $timeout, $rootScope, AUTH_EVENTS, StorageService){

        //flag for when service is down, true = mock response
        var serverDown = false;
        
        //reference to AuthService
        var that = this;

        //default value of currentUser is null, changes after real/fake response
        this.currentUser = null;
        

        //urls
        var loginPath = 'rest/login';


        this.refreshCurrentUser = function(){

            var userPath = localStorage.basePath + 'rest/repo/Person/' + that.getCurrentUser().loc + ".json";
            $http.get(userPath).then(function(response){
                console.log("in refreshCurrentUser: get user response:");
                console.log(response.data);
                that.currentUser = response.data;
                localStorage.currentUser=JSON.stringify(response.data);
                console.log("in refreshCurrentUser refreshed the current user");
            },function(response){
                console.log('Failed to refresh user data, error code: ' + response.status);
            });
        }

        this.getCurrentUser = function(){
            return this.currentUser || JSON.parse(localStorage.currentUser);
        }
//---------------------------------------------------------------------------
    	this.login = function(credentials){
    		
    			var deferred = $q.defer();
                if(serverDown)
                {
                    console.log("server is down");
                    var personObject = {
                                loc:"Person-1",
                                value: {
                                    name: "Joshua Asokanthan",
                                    email: "a@b.com",
                                    sessions:[
                                                {
                                                    assignment: 'Forward Kinematics',
                                                    experimentType: 'forwardKinematics',
                                                    course: 'MSE 404',
                                                    startDateTime: 'Jan 1, 2014 11:00:00',
                                                    endDateTime: 'Jan 1, 2015 12:00:00',
                                                    fromNow:null,
                                                    isActive:null
                                                },{
                                                    assignment: 'Inverse Kinematics',
                                                    experimentType: 'inverseKinematics',
                                                    course: 'MSE 404',
                                                    startDateTime: 'Jan 2, 2015 11:00:00',
                                                    endDateTime: 'Jan 2, 2015 12:00:00',
                                                    fromNow:null,
                                                    isActive:null
                                                }
                                            ]
                                }
                            }
                    that.currentUser = personObject;
                    deferred.resolve(personObject);
                }
                else{
                    //Call REALM Login API
                    $http.post(localStorage.basePath + loginPath,{"username": credentials.email, "password": credentials.password}, {withCredentials:true})
                    .then(function(response){
                        
                        console.log("Login Response: ");
                        console.log(response);

                        $timeout(function(){
                            
                            var allCookies = document.cookie;
                            console.log("Cookies: ");
                            console.log(allCookies);


                            //When login is successful, deferred.promise is resolved with value = person object returned from login API
                            if(response.status == 200)
                            {
                                var personObject = response.data;
                                that.currentUser = personObject;
                                deferred.resolve(personObject);
                            }
                            //When login fails, deferred.promise is rejected with reason = status code returned from login server
                            else
                            {
                                deferred.reject(response.status);
                            }
                        });
                    }, function(response) {
                        console.log('login fail error: ' + response.status)
                        deferred.reject(response.status);
                    });
                }

    			return deferred.promise;
    		}
  //------------------------------------------------------------------------------------------------
    	this.isAuthenticated = function(){
    		
            /*if(StorageService.isAvailable())
            {
                return (localStorage.currentUser !== null);
            }
            else
            {
                //FAKE IT TIL YOU MAKE IT
                return true;
            }*/

            return true;
    	}

    	this.isAuthorized = function (authorizedRoles) {
	        /*if (!angular.isArray(authorizedRoles)) {
	           authorizedRoles = [authorizedRoles];
	        }
	        
            if(StorageService.isAvailable())
            {
                return (this.isAuthenticated() && authorizedRoles.indexOf(localStorage.currentUser.role) !== -1);
            }
            else
            {
                return (this.isAuthenticated() && authorizedRoles.indexOf($rootScope.currentUser.role) !== -1);
            }*/

            return true;
	    }
});
