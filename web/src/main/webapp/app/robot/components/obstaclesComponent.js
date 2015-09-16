'use strict';

app.directive('obstaclesComponent', ['$timeout', 'RobotService', function($timeout, RobotService) {

    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template:   '<div class="obstacles-component__container">' +
                        '<h3 ng-show="data.showMessage" class="obstacles-component__message">{{message}}</h3>' +
                        '<div ng-show="!data.showMessage">' +
                            '<h3>Please select your obstacle set</h3>' +
                            '<div class="btn-group" dropdown is-open="status.isopen">' +
                              '<button id="single-button" type="button" class="btn btn-primary" dropdown-toggle ng-disabled="disabled">' +
                                'Obstacle Sets<span class="caret"></span>' +
                              '</button>' +
                              '<ul class="dropdown-menu" role="menu" aria-labelledby="single-button">' +
                                '<li role="menuitem"><a href="#">Action</a></li>' +
                                '<li role="menuitem"><a href="#">Another action</a></li>' +
                                '<li role="menuitem"><a href="#">Something else here</a></li>' +
                                '<li class="divider"></li>' +
                                '<li role="menuitem"><a href="#">Separated link</a></li>' +
                              '</ul>' +
                            '</div>' +
                        '</div>'+
                    '</div>',
        controller: function ControllerFunction($scope, $element, $attrs, $rootScope)
        {
            $scope.data = {};
            $scope.data.showMessage = false;
            $scope.data.message="";




            // get the robot current possition
            if (scope.component.componentOptions.url === undefined){
                $scope.data.message = "Error: Cannot find robot URL in component options";
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
                }, function(response){
                    console.log(response);
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