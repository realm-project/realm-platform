'use strict';

angular.module('REALM').controller('ExperimentTemplateController', ['$scope','$state', '$rootScope', '$location', 'CameraFeedService', 'RobotService', '$q', '$http', function ($scope,$state,$rootScope, $location, CameraFeedService, RobotService, $q, $http) {
    $scope.layoutObject = null;
    var layout = localStorage.getItem('layout');
    $scope.layoutObject=$.parseJSON(layout);
    sessionStorage.setItem("state",$state.current);

}
]);