'use strict';

app.directive('trajectorySaveComponent', ['$timeout', '$http', '$q', 'RobotService', function ($timeout, $http, $q, RobotService) {

    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template:
            '<div class="trajectory-component__container">' +
            //Menu start
            '<div class="menu-container">'+
                '<div class="menu-content">'+
                    '<div class="btn-group" dropdown is-open="status.isopen">' +
                    '<button type="button" class="btn dropdown-toggle" ng-disabled="disabled">  <span class="menu-icon"></span></button>' +
                    '<ul class="dropdown-menu" role="menu">' +
                        '<li><a class="sub-menu" ng-click="createTrajectory()">New Trajectory</a></li>' +
                        '<li class="divider"></li>' +

            //if something is already saved
                        '<li ng-show="savedTrajectories.length" class="dropdown-submenu">' +
                        ' <a class="sub-menu" tabindex="-1">Trajectories</a>'+
                    '<ul class="dropdown-menu" ng-click="$event.stopPropagation()">'+
                        '<li ng-repeat="trajectory in savedTrajectories" ng-click="$event.stopPropagation()">' +

                        '<span class="sub-menu" ng-click="$event.stopPropagation()">'+

                    '<div class="sub-menu-content">'+
                            '<input id="field" class="menu-input" ng-model="trajectory.name" ng-change="change($index)" ng-readonly="trajectory.readable" ng-click="$event.stopPropagation()">'+
                            '<span class="glyphicon glyphicon-trash"  ng-click="deleteTrajectory(trajectory);event.stopPropagation()"></span>  ' +
                            '<span class="glyphicon glyphicon-pencil"  ng-show="device" ng-click="editSavedTrajectories($index);$event.stopPropagation()"></span>  ' +
                            '<span class="glyphicon glyphicon-refresh"  ng-click="loadTrajectory(trajectory);$event.stopPropagation()" ></span>  ' +
                    '</div>'+

                      '</li>'+
                      '</ul>'+

                    '</li>' +

                 //if nothing is saved
                 '<li ng-show="!savedTrajectories.length"> <a class="sub-menu">Trajectories (no trajectory)</a> </li>' +


                    '</ul>' +
                  '</div>' +
                '</div>'+
            '</div>'+
            //Menu end
                    '<div class="trajectory-component__title"><h4>{{trajectory.name}}</h4></div>'+
                    '<div class="trajectory-component__explanation" ng-show="!savedTrajectories.length" >use the menu to add a new trajectory</div>'+
                    '<div ng-show="trajectory.teachPoints.length" class="trajectory-component__pointContainer">'+
                            '<ul>' +
                                '<li ng-repeat="teachPoint in trajectory.teachPoints">' +
                                    '{{teachPoint.name}} - ' +
                                    '<span class="trajectory-component__savedNum">'+
                                    '({{teachPoint.position[0].label}}: {{teachPoint.position[0].value}}, ' +
                                    '{{teachPoint.position[1].label}}: {{teachPoint.position[1].value}}, ' +
                                    '{{teachPoint.position[2].label}}: {{teachPoint.position[2].value}})' +
                                    '</span>'+
                                '<span class="glyphicon glyphicon-trash"  style="display:inline-block; float:right; padding-top: 4px; padding-right:10px;" ng-click="deleteTeachPoint(teachPoint);event.stopPropagation()"></span>  ' +
                                '</li>' +
                            '</ul>'+
                    '</div>'+
                        '<div class="trajectory-component__btnContainer">'+
                            '<button ng-disabled="!savedTrajectories.length" class="trajectory-component__addBtn btn btn-primary"  ng-click="addTeachPoint()">Add Teach Point</button>'+
                            '<a ng-disabled="!trajectory.teachPoints.length" class="trajectory-component__downloadBtn btn btn-primary"  ng-click="downloadTrajectory()">Download Trajectory</a>'+
                        '</div>'+
                    '</div>',//end of template container,

        controller: function ControllerFunction($scope, $element, $attrs, $rootScope) {
            var robotPath = $scope.component.componentOptions.url;

            $scope.savedTrajectories=[];
            $scope.teachPoints=[];

            $scope.device=true;
            if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
                $scope.device=false;
            }


            $scope.teachPoint={
                id: '0',
                name: 'Untitled Name',
                position:
                [
                    {label:'x', value:'0.00'},
                    {label:'y',value:'0.00'},
                    {label:'z',value:'0.00'}
                ]
            };//end of teach point declaration

            $scope.trajectory={
                readable:true,
                id:'0',
                name:'No Trajectory Available',
                teachPoints:[]
            }

            $scope.emptyTrajectory=function()
            {
                $scope.readable=true;
                $scope.trajectory.id='0';
                $scope.trajectory.name='No Trajectory Available';
                $scope.trajectory.teachPoints=[];
            }

            //creates a new trajectory
            $scope.createTrajectory=function()
            {

                var setNumber=(($scope.savedTrajectories).length)+1;
                $scope.trajectory.name="Trajectory "+setNumber;
                $scope.trajectory.id=setNumber;
                $scope.trajectory.teachPoints=[];
                $scope.savedTrajectories.push(angular.copy($scope.trajectory));
            }
            $scope.createTrajectory();
            //loads a new trajectory
           $scope.loadTrajectory=function(loadedTrajectory)
            {
                $scope.trajectory=angular.copy(loadedTrajectory);
            }

            //add a teach point to the teach point set
            $scope.addTeachPoint=function()
            {
                RobotService.getPose(robotPath).then(function(poseState){

                        console.log("this is the teach point");
                        console.log(poseState);
                        $scope.teachPoint.position[0].value = poseState.position.x.toFixed(4);
                        $scope.teachPoint.position[1].value = poseState.position.y.toFixed(4);
                        $scope.teachPoint.position[2].value = poseState.position.z.toFixed(4);
                        var setNum=($scope.trajectory.teachPoints).length;
                        $scope.teachPoint.name="Position "+(setNum+1);
                        $scope.teachPoint.id=setNum+1;
                        $scope.trajectory.teachPoints.push(angular.copy($scope.teachPoint));
                        var index=$scope.find($scope.trajectory.id,$scope.savedTrajectories);
                        $scope.savedTrajectories[index]=angular.copy($scope.trajectory);
                        console.log("the following teach point was added into "+$scope.trajectory.name);
                        console.log($scope.teachPoint);
                },
                function(response){
                    console.log(response);
                });
            }

            $scope.deleteTrajectory=function(trajectory)
            {
                var index=$scope.find(trajectory.id,$scope.savedTrajectories);
                $scope.savedTrajectories.splice(index,1);
                if($scope.trajectory.id==trajectory.id && $scope.savedTrajectories.length>0)
                        $scope.trajectory=$scope.savedTrajectories[0];
                else if($scope.trajectory.id==trajectory.id && $scope.savedTrajectories.length==0)
                        $scope.emptyTrajectory();
            }
            $scope.deleteTeachPoint=function(teachPoint)
            {
                var index=$scope.find(teachPoint.id,$scope.trajectory.teachPoints)
                $scope.trajectory.teachPoints.splice(index,1);
            }

            $scope.find=function(id,array)
            {
                for(var index=0;index<array.length;index++)
                    if(array[index].id==id)
                        return index;
            }
            $scope.editSavedTrajectories=function(index)
            {
                $scope.savedTrajectories[index].readable=false;
                $(".menu-input:eq("+index+")").focus();
                console.log($scope.trajectory);
            }

            $scope.change=function(index)
            {
                if($scope.savedTrajectories[index].id==$scope.trajectory.id)
                {
                    $scope.trajectory.name=$scope.savedTrajectories[index].name;
                }

            }

            $scope.downloadTrajectory=function()
            {
                var data = "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify($scope.trajectory));
                var href="data:"+data;
                var download=$scope.trajectory.name+".json"
                $(".trajectory-component__downloadBtn").attr("href",href);
                $(".trajectory-component__downloadBtn").attr("download",download);
            }




        },







        compile: function CompilingFunction(tElement, tAttrs) {
            //can only manipulate DOM here (can't access scope yet)


            return function LinkingFunction(scope, element, attrs, ctrl) {
                //can access scope now

             /*   $(element).bind('focusout', function () {

                    for(var i=0;i<scope.savedTrajectories.length;i++)
                    {
                        scope.savedTrajectories[i].readable=true;

                    }
                });*/



            }
        }
    }
}]);