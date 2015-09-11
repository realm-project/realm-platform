'use strict';

app.directive('poseInputComponent', ['$timeout', '$http', '$q', 'RobotService', function($timeout, $http, $q, RobotService) {

    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template:   '<div class="pose-input-component__container"> ' +
                        '<form name="poseInputForm">' +



                            //Menu start
                            '<div class="menu-container">'+
                              '<div class="menu-content">'+
                                  ' <div class="btn-group" dropdown is-open="status.isopen">' +
                                         '<button type="button" class="btn dropdown-toggle" ng-disabled="disabled">  <span class="menu-icon"></span></button>' +
                                             '<ul class="dropdown-menu" role="menu">' +
                                                  '<li><a class="sub-menu" ng-click="savePoseSet()">Save</a></li>' +
                                                   '<li class="divider"></li>' +

                                                    //if something is already saved
                                                    '<li ng-show="savedPoseSets.length" class="dropdown-submenu">' +
                                                    ' <a class="sub-menu" tabindex="-1">Sets</a>'+
                                                    ' <ul class="dropdown-menu" ng-click="$event.stopPropagation()">'+
                                                        '<li ng-repeat="poseSet in savedPoseSets" ng-click="$event.stopPropagation()">' +

                                                        '<span class="sub-menu" ng-click="$event.stopPropagation()">'+
                            // '<a> {{angleSet.name}} </a>'+
                                                  '<div class="sub-menu-content">'+
                                                        '<input class="menu-input" ng-model="poseSet.name" ng-change="change()" ng-readonly="poseSet.readable" ng-click="$event.stopPropagation()">'+
                                                        '<span class="glyphicon glyphicon-trash"   ng-click="deleteSavedPoseSet(poseSet.name,$index);$event.stopPropagation()"></span>  ' +
                                                        '<span class="glyphicon glyphicon-pencil" ng-show="device"  ng-click="editSavedPoseSet(poseSet.name,$index);$event.stopPropagation()"></span>  ' +
                                                        '<span class="glyphicon glyphicon-refresh"  ng-click="loadPoseSet(poseSet);$event.stopPropagation()" ></span>  ' +
                                                  '</div>'+
                            //'<a>'+
                                          '</li>'+
                                     ' </ul>'+

                                    '</li>' +
                            //if nothing is saved
                                    '<li ng-show="!savedPoseSets.length"> <a class="sub-menu">Sets (no saved poses)</a> </li>' +
                                  '</ul>' +
                                 '</div>' +
                             '</div>'+
                            '</div>'+
                            //Menu end




                            '<h3 class="pose-input-component__section-title">Position</h3>' +
                            '<div class="pose_inputs-container">'+
                                '<div class="pose-input-container" ng-repeat="dataPoint in poseSet.position">'+
                                    '<h4 class="pose-input-label">{{dataPoint.label}}</h4>' +
                                    '<numeric-input  min="-6.28" max="6.28" step="0.0001" value="dataPoint.value"></numeric-input>' +
                                '</div>'+
                            '</div>'+
                            '<h3 class="pose-input-component__section-title">Rotations</h3>' +
                            '<div class="pose_inputs-container">'+
                                '<div class="pose-input-container" ng-repeat="dataPoint in poseSet.orientation">'+
                                    '<h4 class="pose-input-label">{{dataPoint.label}}</h4>' +
                                    '<numeric-input min="-6.28" max="6.28" step="0.0001" value="dataPoint.value"></numeric-input>' +
                                '</div>'+
                            '</div>'+
                            '<div class="pose-input-component__buttons" >'+
                            '<button class="pose-input-component__submitBtn btn btn-primary" ng-click="submitPose()">Submit Pose</button>' +
                            '<button class="btn btn-primary" ng-click="goHome()" ng-show="component.componentOptions.showHomeButton">Go Home</button>' +
                            '</div>'+
                        '</form>'+
                    ' </div>',

        controller: function ControllerFunction($scope,$element,$attrs,$rootScope)
        {
            var robotPath = $scope.component.componentOptions.url;

            $scope.savedPoseSets=[];
            $scope.device=true;
            if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
                $scope.device=false;
            }

            $scope.poseSet = {
                name: 'Untitled Set',
                readable:true,
                position: [
                    {label:'x',value:0.0},
                    {label:'y',value:0.0},
                    {label:'z',value:0.0}
                ],
                orientation: [
                    {label:'Roll',value:0},
                    {label:'Pitch',value:0},
                    {label:'Yaw',value:0}
                ]
            };
            //get the current position
            RobotService.getPose(robotPath).then(function(poseState){
                    $scope.poseSet.position[0].value = parseFloat((poseState.position.x).toFixed(4));
                    $scope.poseSet.position[1].value = parseFloat((poseState.position.y).toFixed(4));
                    $scope.poseSet.position[2].value =parseFloat((poseState.position.z).toFixed(4));

                    $scope.poseSet.orientation[0].value = parseFloat((poseState.orientation.x).toFixed(4));
                    $scope.poseSet.orientation[1].value = parseFloat((poseState.orientation.y).toFixed(4));
                    $scope.poseSet.orientation[2].value =parseFloat((poseState.orientation.z).toFixed(4));
            },
                function(response){
                    console.log(response);
            });

            $scope.savePoseSet=function()
            {
                $scope.setNumber=(($scope.savedPoseSets).length)+1;
                $scope.poseSet.name="Set "+$scope.setNumber;
                $scope.savedPoseSets.push(angular.copy($scope.poseSet));
                console.log("saved poses:");
                console.log($scope.savedPoseSets);
            };


                $scope.loadPoseSet = function (loadedPoseSet) {
                    console.log("load the following pose set");
                    console.log(loadedPoseSet);
                    $scope.poseSet=angular.copy(loadedPoseSet);
                };

            //deletes the set
            $scope.deleteSavedPoseSet = function (name,index) {

                //  var index=$scope.find(name);
                //  console.log("name "+name+" @ index "+index+" will be deleted");
                $scope.savedPoseSets.splice(index,1);
            };

            //edits the current set
            $scope.editSavedPoseSet=function(name,index)
            {
                //   var index=$scope.find(name);
                $scope.savedPoseSets[index].readable=false;
                $(".menu-input:eq("+index+")").focus();
                console.log("name "+name+" @ index "+index+" will be re-named");
                console.log($scope.savedPoseSets);
            };


            $scope.goHome=function()
            {
                RobotService.goHome(robotPath);
            }


            $scope.submitPose = function(){
                var postData = {
                    record:false,
                    action: 'setPose',
                    arguments: {
                        linear: {
                            x: parseFloat($scope.poseSet.position[0].value),
                            y: parseFloat($scope.poseSet.position[1].value),
                            z: parseFloat($scope.poseSet.position[2].value)
                        },
                        angular: {
                            x: parseFloat($scope.poseSet.orientation[0].value),
                            y: parseFloat($scope.poseSet.orientation[1].value),
                            z: parseFloat($scope.poseSet.orientation[2].value)
                        }
                    }
                };

                RobotService.setPose(robotPath,postData);

                console.log('Sent the following pose to arm:');
                console.log(postData);
            }
        },
        compile: function CompilingFunction(tElement, tAttrs)
        {
            //can only manipulate DOM here (can't access scope yet)
            return function LinkingFunction(scope, element, attrs, ctrl) {
                //can access scope now
            }
        },
        link: function(scope, element, attrs)
        {
           // elements.a.bind('click', function(){scope.$apply(function(){scope.x++}); alert("works!")});
            $(element).bind('focusout', function () {

                for(var i=0;i<scope.savedPoseSets.length;i++)
                {
                    scope.savedPoseSets[i].readable=true;
                }
            });

        }


    }
}]);





