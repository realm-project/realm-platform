'use strict';

app.directive('angleInputComponent', ['$timeout', '$http', '$q', 'RobotService', function ($timeout, $http, $q, RobotService) {

    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template:
            '<div class="angle-input-component__container">' +

                '<form name="angleInputForm">' +

            //Menu start
                    '<div class="menu-container">'+
                         '<div class="menu-content">'+
                             ' <div class="btn-group" dropdown is-open="status.isopen">' +
                                '<button type="button" class="btn dropdown-toggle" ng-disabled="disabled">  <span class="menu-icon"></span></button>' +
                                 '<ul class="dropdown-menu" role="menu">' +
                                     '<li><a class="sub-menu" ng-click="saveAngleSet(angleSet)">Save</a></li>' +
                                      '<li class="divider"></li>' +

                                          //if something is already saved
                                       '<li ng-show="savedAngleSets.length" class="dropdown-submenu">' +
                                           ' <a class="sub-menu" tabindex="-1">Sets</a>'+
                                           ' <ul class="dropdown-menu" ng-click="$event.stopPropagation()">'+
                                                 '<li ng-repeat="angleSet in savedAngleSets" ng-click="$event.stopPropagation()">' +

                                                           '<span class="sub-menu" ng-click="$event.stopPropagation()">'+
                                                            // '<a> {{angleSet.name}} </a>'+
                                                               '<div class="sub-menu-content">'+
                                                                 '<input class="menu-input" ng-model="angleSet.name" ng-change="change()" ng-readonly="angleSet.readable" ng-click="$event.stopPropagation()">'+
                                                                 '<span class="glyphicon glyphicon-trash"   ng-click="deleteSavedAngleSet(angleSet.name,$index);$event.stopPropagation()"></span>  ' +
                                                                 '<span class="glyphicon glyphicon-pencil"  ng-show="device" ng-click="editSavedAngleSet(angleSet.name,$index);$event.stopPropagation()"></span>  ' +
                                                                 '<span class="glyphicon glyphicon-refresh"  ng-click="loadAngleSet(angleSet);$event.stopPropagation()" ></span>  ' +
                                                               '</div>'+
                                                           //'<a>'+
                                                  '</li>'+
                                           ' </ul>'+

                                      '</li>' +

                                        //if nothing is saved
                                      '<li ng-show="!savedAngleSets.length"> <a class="sub-menu">Sets (no saved angles)</a> </li>' +


                                   '</ul>' +
                                 '</div>' +
                             '</div>'+
                    '</div>'+
                     //Menu end
                    '<div class="angle-inputs-container">'+
                    //Input boxes start
                        '<div class="angle-input-container" ng-repeat="angle in angleSet.angles">' +

                            '<div class="angle-input-label-container">'+

                                '<h4 class="angle-input-label">{{angle.name}}</h4>' +
                                '<span class="glyphicon glyphicon-info-sign" ng-controller="robotModalCtrl" ng-click="open(angle.name)" ></span>'+

                            '</div>'+
                            '<div class="numeric-input-container">'+
                                '<numeric-input min="-6.28" max="6.28" step="0.001" value=angle.value></numeric-input>' +
                            '</div>'+
                        '</div>' +
                        //Input boxes end
                    '</div>'+

                 '<div class="angle-input-component__buttons">'+
                    '<button class="angle-input-component__submitBtn btn btn-primary" ng-click="submitAngleSet(angleSet)">Submit Angles</button>' +
                    '<button class="btn btn-primary" ng-click="goHome()" ng-show="component.componentOptions.showHomeButton">Go Home</button>' +
                    "&nbsp;<button class='btn' ng-click='reset()' ng-show='component.componentOptions.showResetButton'>Reset Arm</button>" +
                '</div>'+
                '</form>' +
            '</div>',

        controller: function ControllerFunction($scope, $element, $attrs, $rootScope) {
            var robotPath = $scope.component.componentOptions.url;

            $scope.savedAngleSets = [];
            $scope.device=true;
            if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
                $scope.device=false;
            }

            $scope.angleSet = {
                name: 'Untitled Set',
                readable:true,
                angles: [
                    {
                        name: 'Base',
                        value: 0.00
                    },
                    {
                        name: 'Shoulder',
                        value: 0.00
                    },
                    {
                        name: 'Arm',
                        value: 0.00
                    },
                    {
                        name: 'Forearm',
                        value: 0.00
                    },
                    {
                        name: 'Wrist 1',
                        value: 0.00
                    },
                    {
                        name: 'Wrist 2',
                        value: 0.00
                    }
                ]
            };


            //get the current angle joints
            RobotService.getJoints($scope.component.componentOptions.url).then(function(angleState){
               $scope.angleSet.angles[0].value = parseFloat(angleState.radians[0].toFixed(3));
               $scope.angleSet.angles[1].value = parseFloat(angleState.radians[1].toFixed(3));
               $scope.angleSet.angles[2].value = parseFloat(angleState.radians[2].toFixed(3));
               $scope.angleSet.angles[3].value = parseFloat(angleState.radians[3].toFixed(3));
               $scope.angleSet.angles[4].value = parseFloat(angleState.radians[4].toFixed(3));
               $scope.angleSet.angles[5].value = parseFloat(angleState.radians[5].toFixed(3));
            },
                function(response){
                console.log(response);
            });



            //sets the name and saves the current set
            $scope.saveAngleSet = function () {
                $scope.setNumber=(($scope.savedAngleSets).length)+1;
                $scope.angleSet.name="Set "+$scope.setNumber;
                $scope.savedAngleSets.push(angular.copy($scope.angleSet));
                console.log("saved angles:");
                console.log($scope.savedAngleSets);
            };

            //loads the current set
            $scope.loadAngleSet = function (loadedAngleSet) {
                console.log("load the following angle set");
                console.log(loadedAngleSet);
                $scope.angleSet=angular.copy(loadedAngleSet)
            };

            //deletes the set
            $scope.deleteSavedAngleSet = function (name,index) {

              //  var index=$scope.find(name);
              //  console.log("name "+name+" @ index "+index+" will be deleted");
                $scope.savedAngleSets.splice(index,1);
            };

            //edits the current set
            $scope.editSavedAngleSet=function(name,index)
            {
             //   var index=$scope.find(name);
                $scope.savedAngleSets[index].readable=false;
                $(".menu-input:eq("+index+")").focus();
                console.log("name "+name+" @ index "+index+" will be re-named");
                console.log($scope.savedAngleSets);
            };

            //finds the index of an element by name
           /* $scope.find=function(name)
            {
                var index=0;
                for(var i=0;i<$scope.savedAngleSets.length;i++)
                {
                    if($scope.savedAngleSets[i].name==name)
                    {
                        index=i;
                    }
                }
                return index;
            };*/

            $scope.goHome=function()
            {
                RobotService.goHome(robotPath);
            }
            $scope.reset=function()
            {
                RobotService.restart(robotPath);
            }

            //submits the current set
            $scope.submitAngleSet = function (angleSet) {
                var angleArray = [];
                for (var i = 0; i < angleSet.angles.length; i++) {
                    angleArray.push(parseFloat(angleSet.angles[i].value));
                }
                RobotService.setJoints(robotPath, angleArray);
            };


        },
        compile: function CompilingFunction(tElement, tAttrs) {
            //can only manipulate DOM here (can't access scope yet)
            return function LinkingFunction(scope, element, attrs, ctrl) {
                //can access scope now
                    $(element).bind('focusout', function () {

                        for(var i=0;i<scope.savedAngleSets.length;i++)
                        {
                            scope.savedAngleSets[i].readable=true;
                        }
                });



            }
        }
    }
}]);