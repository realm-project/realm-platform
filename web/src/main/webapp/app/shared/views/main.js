angular.module('REALM').controller('MainController', function($rootScope, $scope, USER_ROLES, AuthService, $state){

  $scope.currentUser = null;
  $scope.userRoles = USER_ROLES;
  $scope.isAuthorized = AuthService.isAuthorized;
  $scope.bottomNavbarCollapse = true;
  $rootScope.mainScope = $scope;

  $rootScope.$on("$routeChangeStart", function(){
    $rootScope.loading = true;
  });

  $rootScope.$on("$routeChangeSuccess", function(){
    $rootScope.loading = false;
  });
  
  $scope.userAgent =  navigator.userAgent;

  $scope.logoutFunction = function(){

    AuthService.logout().then(function(response){
      $state.go('login');
    },function(errResponse){
      console.log(errResponse);
      $state.go('login');
    });
  }

});