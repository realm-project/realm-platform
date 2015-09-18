'use strict';

angular.module('REALM').service('AuthService',function($http, $q, $cookies, $timeout, $rootScope, AUTH_EVENTS, StorageService){
 
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

            //When login is successful, deferred.promise is resolved with value = person object returned from login API   
            var personObject = response.data;
            // read the user role after login
            $http.get(localStorage.basePath + 'rest/repo/role/' + personObject.value.role.loc + ".json").then(function(roleResponse){
                if(typeof(roleResponse.data)!="undefined" && typeof(roleResponse.data.value)!=="undefined" && typeof(roleResponse.data.value.name)!=="undefined"){

                    localStorage.currentUser = JSON.stringify(personObject);
                    localStorage.currentRole = roleResponse.data.value.name;
                    deferred.resolve(personObject);
                }else{
                    console.log("cannot read the user role");
                    deferred.reject(404);
                }
            },function(errResponse){
                console.log('login fail error: cannot load the user role' + errResponse.status)
                deferred.reject(errResponse.status);
            });                      
        }, function(errResponse) {
            console.log('login fail error: ' + errResponse.status)
            deferred.reject(errResponse.status);
        });
        
        return deferred.promise;
    }

    this.logout = function(){
        localStorage.removeItem('currentUser');
        localStorage.removeItem('currentRole');
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

        if(StorageService.isAvailable())
        {
            return (typeof(localStorage.currentUser) !== "undefined");
        }
        else
        {
            // we do not support browsers without localStorage
            // possible solution: use cookie
            return false;
        }
    }

    this.isAuthorized = function (authorizedRoles) {
        
        if (!angular.isArray(authorizedRoles)) {
            // to handle single authorizedRoles (example: 'teacher')
            authorizedRoles = [authorizedRoles];
        }
    
        if(StorageService.isAvailable() && this.isAuthenticated())
        {  
            if (localStorage.currentRole === "admin"){
                // admin can access any page
                return true;
            }else{
                return (authorizedRoles.indexOf(localStorage.currentRole) !== -1);
            }
              
        }else{
            // user is not logged-in or the browser does not support localStorage
            // we do not support browsers without localStorage. possible solution: use cookie
            return false;
        }
    }
});
