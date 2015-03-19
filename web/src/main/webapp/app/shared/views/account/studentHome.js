
'use strict';

angular.module('REALM')
.controller('StudentHomeController', function ($scope, $rootScope, AuthService, AUTH_EVENTS, $state) {
    $scope.user = AuthService.getCurrentUser();
    $scope.firstName = $scope.user.value.name.split(' ')[0];

    $scope.goToPage = function(page){
        switch(page)
        {
            case 'Sessions':
                $state.go('studentSessions')
                break;
            case 'Profile':
                $state.go('studentProfile')
                break;
            case 'Courses':
                $state.go('studentCourses')
                break;
            case 'Notifications':
                $state.go('notifications')
                break;
            default:
                alert('invalid page argument');
                break;
        }
    }
});