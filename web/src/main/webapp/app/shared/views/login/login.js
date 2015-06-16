'use strict';

angular.module('REALM')
.controller('LoginController', function ($scope, $rootScope, AuthService, StorageService, AUTH_EVENTS, $state, RepoService , $http, USER_ROLES) {
    
    


    $scope.credentials = {
        email:'',
        password:''
    };

    $scope.userSettings = {
        rememberMe: false
    }
    
    $scope.firstName = '';


    if(StorageService.isAvailable())
    {
        //If user has logged in before, localStorage.rememberMe should be non-null
        if(typeof(localStorage.rememberMe) !== "undefined")
        {
            //If rememberMe is true, autoFill email
            if(localStorage.rememberMe === "true" && typeof(localStorage.rememberedEmail) !== "undefined")
            {
                $scope.credentials.email = localStorage.rememberedEmail;
                $scope.userSettings.rememberMe = true;
            }            
        }
    }
    //TODO: give an appropriate message if storageService was not available
    
    
    $scope.login = function(credentials){
        
        if(StorageService.isAvailable()){
            if($scope.userSettings.rememberMe)
            {
                localStorage.rememberMe = "true";
                localStorage.rememberedEmail = credentials.email;
            }else{
                localStorage.rememberMe = "false";
                localStorage.removeItem("rememberedEmail");
            }
        }
        //On login success, event arg = none (AuthService.currentUser is now available though)
        //On login failure, event arg = error code
        AuthService.login(credentials).then(function(personObject){
            $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
        }, function(errorCode){
            if(errorCode === 404)
            {
                $rootScope.$broadcast(AUTH_EVENTS.notFound, errorCode);
            }
            else if(errorCode === 401)
            {
                $rootScope.$broadcast(AUTH_EVENTS.loginFailed, errorCode);
            }    
        });
    }
    $scope.sendEmail=function(){
        //TODO: show loading icon
        $http.post(localStorage.basePath + "rest/forgotpassword",{"username": $scope.credentials.email}, {withCredentials:true}).then(function(response){
            //TODO: hide loading icon
            console.log(response);
        },function(errResponse){
            //TODO: hide loading icon and show an appropriate error message
            console.log(errResponse);
        });
    }

    $scope.$on(AUTH_EVENTS.loginSuccess, function() {

        $scope.firstName = AuthService.getCurrentUser().value.name.split(' ')[0];
        
        if(AuthService.isAuthorized(USER_ROLES.student)){
            $state.go('studentHome');
        }else if(AuthService.isAuthorized(USER_ROLES.teacher)){
            $state.go('teacherHome');
        }else if(AuthService.isAuthorized(USER_ROLES.admin)){
            // not any specific page for admins
            $state.go('teacherHome');
        }else{
            $rootScope.$broadcast(AUTH_EVENTS.notFound);
        }
    });

    $scope.$on(AUTH_EVENTS.loginFailed, function() {
        $rootScope.toggle('accessDeniedOverlay','on');
    });
    
    $scope.$on(AUTH_EVENTS.notFound, function() {
        $rootScope.toggle('resourceNotFoundOverlay','on');
    });
});