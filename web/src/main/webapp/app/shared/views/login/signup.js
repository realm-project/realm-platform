'use strict';

angular.module('REALM')
.controller('SignupController', function ($scope, $rootScope, $http, $q, AuthService, $state) {
    
    $scope.accountDetails = {
        name:'',
        email:'',
        password:'',
        confirmPassword:''
    };
    $scope.submitted = false;

    $scope.signup = function(){

        var postData = {
            username: $scope.accountDetails.email,
            password: $scope.accountDetails.password,
            fullname: $scope.accountDetails.name
        };
        $http.post(localStorage.basePath + 'rest/signup',postData).then(function(response){
            $rootScope.toggle('signupSuccessOverlay','on');
        },function(response){
            if (response.data !== undefined && response.data.message !== undefined) {
                $scope.generalErrorMessage = response.data.message;
                $rootScope.toggle('generalErrorOverlay','on');
            }
            else{
                $rootScope.toggle('signupFailureOverlay','on');
            }
        });
    }

    $scope.getFirstName = function(){
        return $scope.accountDetails.name.split(' ')[0];
    }
});