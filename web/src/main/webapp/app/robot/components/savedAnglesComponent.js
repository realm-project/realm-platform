'use strict';

app.directive('savedAngleSetsComponent', ['$timeout', '$http', '$q', 'RobotService', function($timeout, $http, $q, RobotService) {
    
    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template:   '<div class="ui input">' +
                        //'<div class="inner-card ui segment blue">' +
                            '<input class="full-width" placeholder="Set Name" ng-model="angleSet.name"></input>' +
                            '<button class="ui button green half-width" id="goButton" ng-click="writeAnglesToArm(angleSet)">Go</button>' +
                            '<button class="ui button red half-width" id="saveButton" ng-click="saveAngleSet()" >Save</button>' +
                            '<form name="angleInputForm">' +
                                '<div class="control-group" ng-class="{error: myForm.angle.value.$invalid}">' +
                                    '<input ng-repeat="angle in angleSet.angles" min="-6.2" max="6.2" step="0.1" ng-model="angle.value" placeholder="{{angle.name}}" class="angleInput half-width" type="number">' +
                                    '</input>' +
                                '</div>' +
                            '</form>' +
                        //'</div>' +
                    '</div>',
        controller: function ControllerFunction($scope,$element,$attrs)
        {
        
            $scope.model.savedAngleSets = [];
            $scope.currentImage = '';
            
            $scope.currentAngles = [];
            $scope.currentAngleSet;
            
            $scope.angleSet = {
                name: 'Untitled Set',
                angles: [{
                    name: 'Joint1',
                    value: 0.00
                }, {
                    name: 'Joint2',
                    value: 0.00
                }, {
                    name: 'Joint3',
                    value: 0.00
                }, {
                    name: 'Joint4',
                    value: 0.00
                }, {
                    name: 'Joint5',
                    value: 0.00
                }, {
                    name: 'Joint6',
                    value: 0.00
                }, ]
            };
    
            $scope.saveAngleSet = function () {
                $scope.model.savedAngleSets.push(angular.copy($scope.angleSet));
            };
            
            $scope.deleteAngleSet = function(setName){
                var angleIndex = -1;                                                          
                for(var i=0; i < $scope.model.savedAngleSets.length; i++)
                {
                    if($scope.model.savedAngleSets[i].name === setName) angleIndex = i;
                }
                
                if(angleIndex >= 0)
                {
                    $scope.model.savedAngleSets.splice(angleIndex,1);
                }
            };
            
            $scope.writeAnglesToArm = function(angleSet){
                $scope.anglesToApproach = angleSet;
                
                RobotService.setAngle($scope.anglesToApproach.angles[0].value,1);
                RobotService.setAngle($scope.anglesToApproach.angles[1].value,2);
                RobotService.setAngle($scope.anglesToApproach.angles[2].value,3);
                RobotService.setAngle($scope.anglesToApproach.angles[3].value,4);
                RobotService.setAngle($scope.anglesToApproach.angles[4].value,5);
                RobotService.setAngle($scope.anglesToApproach.angles[5].value,6);
            };
            
            $scope.deleteSavedAngleSet = function(index)
            {
                $scope.model.savedAngleSets.splice(index,1);
                console.log('deleted angleSet @ index: ' + index);
            }        
            
            
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





