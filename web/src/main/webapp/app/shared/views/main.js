angular.module('REALM').controller('MainController', function($rootScope, $scope, USER_ROLES, AuthService){

  $scope.currentUser = null;
  $scope.userRoles = USER_ROLES;
  $scope.isAuthorized = AuthService.isAuthorized;

  $rootScope.$on("$routeChangeStart", function(){
    $rootScope.loading = true;
  });

  $rootScope.$on("$routeChangeSuccess", function(){
    $rootScope.loading = false;
  });
  
  $scope.userAgent =  navigator.userAgent;

});