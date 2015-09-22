angular.module('REALM', ['ui.bootstrap']);

var robotModalCtrl = function ($scope, $modal) {

    $scope.open = function (name) {

        var modalInstance = $modal.open({
            template:
            '<div class="modal-header">' + 
                '<h3 class="modal-title">{{name}} joint</h3>'+
            '</div>'+
            '<div class="modal-body">'+
                '<span>Valid input range: -2π to +2π (radian)</span>'+
                '<div class="metadata-image-container">'+
                    '<img width="10%" height="10%" ng-src={{url}}>'+
                '</div>'+
            '</div>'+
            '<div class="modal-footer">'+
                '<button class="btn btn-primary" ng-click="ok()">OK</button>'+
            '</div>',
            controller: robotModalInstanceCtrl,
            resolve: {
                name: function () {
                    return name;
                }
            }
        });

        modalInstance.result.then(function(){});
    };
};


var robotModalInstanceCtrl = function ($scope, $modalInstance,name) {

    $scope.name=name.replace(" ","").toLowerCase();
    $scope.url='assets/img/robot/'+$scope.name+'.png'
    $scope.ok = function () {
        $modalInstance.close();
    };

};