'use strict';

angular.module('REALM')
.controller('LoginController', function ($scope, $rootScope, AuthService, StorageService, AUTH_EVENTS, $state, RepoService) {
    
    


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

    $scope.$on(AUTH_EVENTS.loginSuccess, function() {

        $scope.firstName = AuthService.getCurrentUser().value.name.split(' ')[0];
        
        //$rootScope.toggle('loginSuccessOverlay','on');
        var userRole= RepoService.getObject("role",AuthService.getCurrentUser().value.role.loc);
        userRole.then(
            function(response)
            {
                if(typeof(response.data)!="undefined" && typeof(response.data.value)!="undefined" && typeof(response.data.value.name)!="undefined")
                {
                    if(response.data.value.name=="student"){
                        $state.go('studentHome');
                    }else if (response.data.value.name=="teacher"){
                        $state.go('teacherHome');
                    }else if(response.data.value.name=="admin"){
                        console.log("admin login");
                        $state.go('teacherHome');
                    }else{
                        console.log("Unknown user role");
                        console.log(response);
                        $rootScope.$broadcast(AUTH_EVENTS.notFound);
                    }
                }else{
                    console.log("Unknown user role");
                    console.log(response);
                    $rootScope.$broadcast(AUTH_EVENTS.notFound);
                }
            },function(response){
                console.log("Cannot get the user role");
                console.log(response);
                $rootScope.$broadcast(AUTH_EVENTS.notFound);
            }
        );
    });

    $scope.$on(AUTH_EVENTS.loginFailed, function() {
        $rootScope.toggle('accessDeniedOverlay','on');
    });
    
    $scope.$on(AUTH_EVENTS.notFound, function() {
        $rootScope.toggle('resourceNotFoundOverlay','on');
    });
});


/*switch(User.currentUser.role)
            {
                case 'student': 
                    $state.go('studentHome');
                    break;
                case 'teacher': 
                    $state.go('teacherHome');
                    break;
                case 'admin': 
                    $state.go('adminHome');
                    break;
                default:
                    alert('Error: User has no role');
            }*/