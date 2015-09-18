'use strict';

app.directive('obstaclesComponent', ['$timeout', 'RobotService', function($timeout, RobotService) {

    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template:   '<div class="obstacles-component__container">' +
                        '<h4 ng-show="data.showMessage" class="obstacles-component__message">{{data.message}}</h4>' +
                        '<div ng-show="!data.showMessage">' +
                            '<h4>Please select your obstacle set</h4>' +
                            '<div class="btn-group" dropdown is-open="status.isopen">' +
                              '<button id="single-button" type="button" class="btn btn-primary" dropdown-toggle ng-disabled="disabled">' +
                                '{{data.selectButtonMessage}}<span class="caret"></span>' +
                              '</button>' +
                              '<ul class="dropdown-menu" role="menu" aria-labelledby="single-button">' +
                                '<li role="menuitem">Action</li>' +
                                '<li role="menuitem">Another action</li>' +
                                '<li role="menuitem">Something else here</li>' +
                                '<li class="divider"></li>' +
                                '<li role="menuitem">Separated link</li>' +
                              '</ul>' +
                            '</div>' +
                        '</div>'+
                    '</div>',
        controller: function ControllerFunction($scope, $element, $attrs, $rootScope)
        {
            $scope.data = {};
            $scope.data.showMessage = false;
            $scope.data.message="";

            $scope.data.selectedSet=null;
            $scope.data.selectButtonMessage ="No Obstacle";


            // get the robot current possition
            if ($scope.component.componentOptions.url === undefined){
                $scope.data.message = "Error: Cannot find robot URL in component options";
                $scope.data.showMessage = true;
                return;
            }
            if ($scope.component.componentOptions.obstacleSets=== undefined || $scope.component.componentOptions.obstacleSets.length == 0){
                $scope.data.message = "Error: There is not any obstacle set in component options";
                $scope.data.showMessage = true;
                return;
            }

            var robotPath = $scope.component.componentOptions.url;
            var poseStateTimeout;
            var getPoseState = function(){
                RobotService.getPose(robotPath).then(function(poseState){
                    $scope.data.position = poseState.position;
                    //$scope.data.orientation = poseState.orientation;
                    poseStateTimeout = setTimeout(getPoseState,200);
                }, function(errorResponse){
                    console.log(errorResponse);
                    poseStateTimeout = setTimeout(getPoseState,200);
                });
            };

            $scope.$on("$destroy", function(){
                clearTimeout(poseStateTimeout);
            });

            getPoseState();
        },

        compile: function CompilingFunction(tElement, tAttrs)
        {
            //can only manipulate DOM here (can't access scope yet)
            return function LinkingFunction(scope, element, attrs, ctrl) {
                //can access scope now
            }
        }

    }
}]);