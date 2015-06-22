'use strict';

angular.module('REALM')
.controller('ResetPassController', function ($scope, $rootScope, $state, $http, $stateParams) {
    $scope.resetData= {
      token:$stateParams.tokenID,
      password:"",
      confirmPassword:""
    };

    $scope.generalErrorMessage = "";

    // to check if the user have clicked on the submit button or not
    $scope.submitted = false;

    $scope.resetPass = function(){
        $scope.submitted = true;
        if($scope.resetData.confirmPassword !== $scope.resetData.password || $scope.resetData.password.length < 6 ){
            return;
        }

        //TODO: show loading icon
        $http.post(localStorage.basePath + "rest/resetpassword" ,{"username": $scope.resetData.email , "token": $scope.resetData.token, "password": $scope.resetData.password}, {withCredentials:true}).then(function(response){
            //TODO: hide loading icon
            $rootScope.toggle('passwordUpdateOverlay','on');
            
        },function(errResponse){
            //TODO: hide loading icon
            if (errResponse.data !== undefined && errResponse.data.message !== undefined)
            {
                $scope.generalErrorMessage = errResponse.data.message;
                $rootScope.toggle('generalErrorOverlay','on');
            }else{
                $rootScope.toggle('resourceNotFoundOverlay','on');
            }
            console.log(errResponse);
        });

    };
});