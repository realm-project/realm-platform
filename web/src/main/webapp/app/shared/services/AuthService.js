'use strict';

angular.module('REALM')
    .service('AuthService',function($http, $q, $cookies, $timeout, $rootScope, AUTH_EVENTS, StorageService){
 
        //urls
        var loginPath = 'rest/login';
        var logoutPath ='rest/logout';

        this.refreshCurrentUser = function(){

            var userPath = localStorage.basePath + 'rest/repo/Person/' + JSON.parse(localStorage.currentUser).loc + ".json";
            $http.get(userPath).then(function(response){
                console.log("in refreshCurrentUser: get user response:");
                console.log(response.data);
                localStorage.currentUser=JSON.stringify(response.data);
                console.log("in refreshCurrentUser refreshed the current user");
            },function(response){
                console.log('Failed to refresh user data, error code: ' + response.status);
            });
        }

        this.getCurrentUser = function(){
            return JSON.parse(localStorage.currentUser);
        }
//---------------------------------------------------------------------------
    	this.login = function(credentials){
    		
    			var deferred = $q.defer();
             
                //Call REALM Login API
                $http.post(localStorage.basePath + loginPath,{"username": credentials.email, "password": credentials.password}, {withCredentials:true})
                .then(function(response){
                    
                    console.log("Login Response: ");
                    console.log(response);

                    $timeout(function(){
                        
                        //var allCookies = document.cookie;
                        //console.log("Cookies: ");
                        //console.log(allCookies);


                        //When login is successful, deferred.promise is resolved with value = person object returned from login API
                        if(response.status == 200)
                        {
                            var personObject = response.data;
                            localStorage.currentUser = JSON.stringify(personObject);
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
                
    			return deferred.promise;
    		}

        this.logout = function(){
            localStorage.removeItem('currentUser');
            var deferred = $q.defer();
            $http.post(localStorage.basePath + logoutPath).then(function(response){
                deferred.resolve(response);
            },function(errResponse){
                deferred.reject(errResponse);
            });
            return deferred.promise;
        };

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
