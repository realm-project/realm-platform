'use strict';

app.directive('obstaclesComponent', ['$timeout', 'RobotService', function($timeout, RobotService) {

    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template:   '<div class="obstacles-component__container">' +
                        '<h4 ng-show="data.showMessage" class="obstacles-component__message">{{data.message}}</h4>' +
                        '<div ng-show="!data.showMessage" style="display:inline">' +
                            '<h4 style="float:left;">Please select your obstacle set</h4>' +
                            '<div class="btn-group" dropdown style="float:left;margin-left:10px;">' +
                              '<button id="obstacle-button" type="button" class="btn btn-primary" dropdown-toggle style="top:-8px;border-radius:4px;">' +
                                '{{data.selectedSet.title}} <span class="caret"></span>' +
                              '</button>' +
                              '<ul class="dropdown-menu obstacles-component__dropDown-ul" role="menu" aria-labelledby="obstacle-button">' +
                                '<li role="menuitem" ng-click="obstacleSelected(data.defaultSet)">{{data.defaultSet.title}}</li>' +
                                '<li role="menuitem" ng-repeat="obstacleSet in data.obstacleSets" ng-click="obstacleSelected(obstacleSet)">{{obstacleSet.title}}</li>' +
                              '</ul>' +
                            '</div>' +
                            '<div style="clear:both;"></div>'+
                        '</div>'+
                        '<div>'+
                            '<h4 style="display:inline; float:left;">Description</h4>'+
                            '<textarea style="width:100%; resize: none;" readonly>{{data.selectedSet.description}}</textarea>'+
                        '</div>'+
                        '<div style="margin-top:5px;">'+
                            '<h4 style="display:inline; float:left;">Obstacle List:</h4>'+
                            '<ul ng-show="data.selectedSet.obstacles.length != 0" class="list-group" style="float:left; margin-left:10px;">'+
                                '<li ng-repeat="obstacle in data.selectedSet.obstacles" class="list-group-item">{{obstacle.x1}},{{obstacle.y1}},{{obstacle.z1}} - {{obstacle.x2}},{{obstacle.y2}},{{obstacle.z2}}</li>' +
                            '</ul>'+
                            '<h4 ng-show="data.selectedSet.obstacles.length == 0" style="display:inline; float:left; margin-left:10px;">No Obstacle</h4>' +
                            '<div style="clear:both;"></div>'+
                        '</div>'+
                        '<div>'+
                            '<h4 style="display:inline; float:left;">Status:</h4>'+
                            '<h4 style="display:inline; float:left; margin-left:10px; border-radius: 4px; padding: 2px 6px;" ng-class="{\'obstacles-component__green\' : data.status===\'No Collision\', \'obstacles-component__red\': data.status!==\'No Collision\'}">{{data.status}}</h4>'+
                            '<h5 ng-show="data.status!==\'No Collision\'" style="display:inline; float:left; margin-left:5px;">{{data.crossedMessage}}</h5>'+
                            '<div style="clear:both;"></div>'+
                        '</div>'+
                        '<button class="btn btn-primary" ng-click="obstacleRestart()">Restart</button>' +
                    '</div>',
        controller: function ControllerFunction($scope, $element, $attrs, $rootScope)
        {
            $scope.data = {};
            $scope.data.showMessage = false;
            $scope.data.message="";

            $scope.data.defaultSet ={"title":"No Obstacle","description":"Please select an obstacle set to see description here", "obstacles":[]};
            $scope.data.selectedSet=$scope.data.defaultSet;
            $scope.data.status = "No Collision";
            $scope.data.crossedMessage = "";

            // get the robot current possition
            if ($scope.component.componentOptions.url === undefined){
                $scope.data.message = "Error: Cannot find robot URL in component options";
                $scope.data.showMessage = true;
                return;
            }
            
            if ($scope.component.componentOptions.obstacleSets === undefined || $scope.component.componentOptions.obstacleSets.length == 0){
                $scope.data.message = "Error: There is not any obstacle set in component options";
                $scope.data.showMessage = true;
                return;
            }else{
                $scope.data.obstacleSets = $scope.component.componentOptions.obstacleSets;
            }

            // this function update selected obstable set
            $scope.obstacleSelected = function(obstacleSet){
                $scope.data.selectButtonMessage = obstacleSet.title;
                $scope.data.selectedSet = obstacleSet;
            };

            $scope.obstacleRestart = function(){
                $scope.data.status = "No Collision";
            };

            var robotPath = $scope.component.componentOptions.url;
            var poseStateTimeout;
            var getPoseState = function(){
                RobotService.getPose(robotPath).then(function(poseState){
                    $scope.data.position = poseState.position;
                    poseStateTimeout = setTimeout(getPoseState,200);
                    
                    var x = $scope.data.position.x;
                    var y = $scope.data.position.y;
                    var z = $scope.data.position.z;

                    $scope.data.selectedSet.obstacles.forEach(function(obstacle){
                        if (x > obstacle.x1 && y > obstacle.y1 && z > obstacle.z1 && x < obstacle.x2 && y < obstacle.y2 && z < obstacle.z2){
                            $scope.data.status = "Crossed Obstacles!";
                            $scope.data.crossedMessage = "Crossed Point: ("+ x.toFixed(3) +', '+ y.toFixed(3)+ ', ' + z.toFixed(3)+')';
                        }
                    });

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