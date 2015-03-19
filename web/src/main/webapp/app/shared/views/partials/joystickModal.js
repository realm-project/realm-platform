angular.module('REALM', ['ui.bootstrap']);

var joystickModalCtrl = function ($scope, $modal, $log) {

    $scope.open = function (axis) {

        var modalInstance = $modal.open({
            templateUrl: 'shared/views/partials/joystickModal.html',
            controller: joystickModalInstanceCtrl,
          //  size: 'sm',
            resolve: {
                axis: function () {
                    return axis;
                }
            }
        });

        modalInstance.result.then(
            function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
    };
};


var joystickModalInstanceCtrl = function ($scope, $modalInstance,axis) {

    $scope.axis=axis;
    $scope.name= axis=="Z:"? "z":"xy";
    $scope.url='assets/img/'+$scope.name+'.svg';
    $scope.ok = function () {
        $modalInstance.close();
    };

};