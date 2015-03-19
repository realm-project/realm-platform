'use strict';

angular.module('REALM').controller('TeachPointsAndDynamicResponseController', ['$scope', '$rootScope', '$location', 'CameraFeedService', 'RobotService', '$q', '$http', function ($scope, $rootScope, $location, CameraFeedService, RobotService, $q, $http) {
    $scope.layoutObject = null;

    $http.get('mico/views/layouts/teachPointsAndDynamicResponse.json').then(function(response){
        $scope.layoutObject = response.data;
    });


}
]);