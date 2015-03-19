angular.module('REALM', ['ui.bootstrap']);

var robotModalCtrl = function ($scope, $modal, $log) {

    $scope.open = function (name) {

        var modalInstance = $modal.open({
            templateUrl: '/robotModal.html',
            controller: robotModalInstanceCtrl,
          //  size: 'sm',
            resolve: {
                name: function () {
                    return name;
                }
            }
        });

        modalInstance.result.then(
            function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
    };
};


var robotModalInstanceCtrl = function ($scope, $modalInstance,name) {

    $scope.name=name.replace(" ","").toLowerCase();
    $scope.url='assets/img/'+$scope.name+'.png'
    $scope.ok = function () {
        $modalInstance.close();
    };

};