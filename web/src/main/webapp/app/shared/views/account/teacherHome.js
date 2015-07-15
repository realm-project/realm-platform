
'use strict';

angular.module('REALM')
.controller('teacherHomeController', function ($scope, $rootScope, AuthService, AUTH_EVENTS, $state) {
    $scope.user = AuthService.getCurrentUser();
    $scope.firstName = $scope.user.value.name.split(' ')[0];

    $scope.goToPage = function(page){
        switch(page)
        {
            case "ReviewSessions":
                $state.go('teacherHome.reviewsSessions');
                break;
            case "CreateSessions":
                $state.go('teacherHome.createSessions');
                break;            
            case 'Sessions':
                $state.go('teacherHome.mySessions');
                break;
            case 'Profile':
                $state.go('teacherHome.profile');
                break;
            case 'Courses':
                $state.go('teacherHome.courses');
                break;
            //case 'Notifications':
            //    $state.go('notifications');
            //    break;
            default:
                alert('invalid page argument');
                break;
        }
    }
});