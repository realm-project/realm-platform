'use strict';



angular.module('REALM')
.controller('SignupController', function ($scope, $rootScope, $http, $q, AuthService, $state) {
    
    $scope.accountDetails = {
        name:'',
        email:'',
        password:'',
        confirmPassword:''
    };

    $scope.signup = function(){
        /*AuthService.signup($scope.accountDetails).then(function(){
            $rootScope.$broadcast(AUTH_EVENTS.signupSuccess);
        }, function(){
            $rootScope.$broadcast(AUTH_EVENTS.signupFailed);
        });*/
        /*$rootScope.toggle('signupSuccessOverlay','on');
        $state.go('login');*/
        
        var postData = {
            username: $scope.accountDetails.email,
            password: $scope.accountDetails.password,
            fullname: $scope.accountDetails.name
        };

        $http.post(localStorage.basePath + 'rest/signup',postData).then(function(response){
            console.log(response);
            $rootScope.toggle('signupSuccessOverlay','on');
        },function(response){
            console.log(response);
            $rootScope.toggle('signupFailureOverlay','on');
        });



    }

    $scope.getFirstName = function(){
        return $scope.accountDetails.name.split(' ')[0];
    }
});