'use strict';

app.directive('trajectorySaveReplayComponent', ['$timeout', '$http', '$q', 'RobotService', 'RepoService', function ($timeout, $http, $q, RobotService, RepoService) {

    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template: '<div class="trajectory-component__container">' +
            //Menu start
            '<div class="menu-container">' +
            '<div class="menu-content">' +
            '<div class="btn-group" dropdown is-open="status.isopen">' +
            '<button type="button" class="btn dropdown-toggle" ng-disabled="disabled">  <span class="menu-icon"></span></button>' +
            '<ul class="dropdown-menu" role="menu">' +
            '<li><a class="sub-menu" ng-click="createTrajectory()">New Trajectory</a></li>' +
            '<li class="divider"></li>' +

            //if something is already saved
            '<li ng-show="savedTrajectories.length" class="dropdown-submenu">' +
            ' <a class="sub-menu" tabindex="-1">Trajectories</a>' +
            '<ul class="dropdown-menu" ng-click="$event.stopPropagation()">' +
            '<li ng-repeat="trajectory in savedTrajectories" ng-click="$event.stopPropagation()">' +

            '<span class="sub-menu" ng-click="$event.stopPropagation()">' +

            '<div class="sub-menu-content">' +
            '<input id="field" class="menu-input" ng-model="trajectory.name" ng-change="change($index)" ng-readonly="trajectory.readable" ng-click="$event.stopPropagation()">' +
            '<span class="glyphicon glyphicon-trash"  ng-click="deleteTrajectory(trajectory);event.stopPropagation()"></span>  ' +
            '<span class="glyphicon glyphicon-pencil"  ng-show="device" ng-click="editSavedTrajectories($index);$event.stopPropagation()"></span>  ' +
            '<span class="glyphicon glyphicon-refresh"  ng-click="loadTrajectory(trajectory);$event.stopPropagation()" ></span>  ' +
            '</div>' +

            '</li>' +
            '</ul>' +

            '</li>' +

            //if nothing is saved
            '<li ng-show="!savedTrajectories.length"> <a class="sub-menu">Trajectories (no trajectory)</a> </li>' +


            '</ul>' +
            '</div>' +
            '</div>' +
            '</div>' +
            //Menu end

            '<div class="trajectory-component__title"><h4>{{trajectory.name}}</h4></div>' +
            '<div class="trajectory-component__explanation" ng-show="!savedTrajectories.length" >use the menu to add a new trajectory</div>' +
            '<div ng-show="trajectory.teachPoints.length" class="trajectory-component__pointContainer">' +
            '<ul>' +
            '<li ng-repeat="teachPoint in trajectory.teachPoints">' +
            '{{teachPoint.name}} - ' +
            '<span class="trajectory-component__savedNum">' +
            '({{teachPoint.position[0].label}}: {{teachPoint.position[0].value}}, ' +
            '{{teachPoint.position[1].label}}: {{teachPoint.position[1].value}}, ' +
            '{{teachPoint.position[2].label}}: {{teachPoint.position[2].value}})' +
            '</span>' +
            '<span class="glyphicon glyphicon-trash"  style="display:inline-block; float:right; padding-top: 4px; padding-right:10px;" ng-click="deleteTeachPoint(teachPoint);event.stopPropagation()"></span>  ' +
            '</li>' +
            '</ul>' +
            '</div>' +
            '<div class="trajectory-component__btnContainer">' +
            '<button ng-disabled="!savedTrajectories.length" class="trajectory-component__addBtn btn btn-primary"  ng-click="addTeachPoint()">Save Teach Point</button>' +
            //'<a ng-disabled="!trajectory.teachPoints.length" class="trajectory-component__downloadBtn btn btn-primary"  ng-click="downloadTrajectory()">Download Trajectory</a>' +
            '</div>' +

            '<hr>'+

            '<div class="pid-inputs-container">' +
            '<div class="pid-input-container">' +
            '<div class="pid-input-label-container">' +
            '<h4 class="pid-input-label">P</h4>' +
            '</div>' +
            '<div class="numeric-input-container">' +
            '<numeric-input min="-6.28" max="6.28" step="0.0001" value=pValue></numeric-input>' +
            '</div>' +
            '</div>' +

            '<div class="pid-input-container">' +
            '<div class="pid-input-label-container">' +
            '<h4 class="pid-input-label">I</h4>' +
            '</div>' +
            '<div class="numeric-input-container">' +
            '<numeric-input min="-6.28" max="6.28" step="0.0001" value=iValue></numeric-input>' +
            '</div>' +
            '</div>' +

            '<div class="pid-input-container">' +
            '<div class="pid-input-label-container">' +
            '<h4 class="pid-input-label">D</h4>' +
            '</div>' +
            '<div class="numeric-input-container">' +
            '<numeric-input min="-6.28" max="6.28" step="0.0001" value=dValue></numeric-input>' +
            '</div>' +
            '</div>' +
            '</div>' +


            '<div class="angle-input-component__buttons">' +
            '<button class="angle-input-component__submitBtn btn btn-primary" ng-disabled="!trajectory.teachPoints.length || readFlag || logFlag" ng-click="submitTrajectory()">Replay Trajectory</button>' +
            //'<button class="angle-input-component__submitBtn btn btn-primary"  ng-click="getCommandInfo(997)">Test Command</button>' +
            '</div>' +


           '<div>'+
               '<alert ng-repeat="alert in alerts track by $index" type={{alert.type}} class={{alert.class}} close="closeAlert($index)">{{alert.msg}}</alert>'+
            '</div>'+

            '</div>',//end of template container,

        controller: function ControllerFunction($scope, $element, $attrs, $rootScope) {
            var robotPath = $scope.component.componentOptions.url;
            $scope.device = true;
            if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
                $scope.device = false;
            }

            $scope.prevLogLength=0;
            $scope.logFlag=false;
            $scope.readFlag=false;
            $scope.replayReady=false;

            $scope.logAlert={ type: 'log', msg: 'Arm status is being logged...' , class:'alert-process'};
            $scope.readAlert={ type: 'read', msg: 'Reading arm status data...', class:'alert-process'};
            $scope.successAlert={ type: 'success', msg: 'Data is ready and visualized!', class:'alert-done' };
            $scope.alerts=[  ];


            $scope.closeAlert = function(index) {
                $scope.alerts.splice(index, 1);
            };

            $scope.findAlert=function(type) {
                for (var i=0;i<$scope.alerts.length;i++){
                    if($scope.alerts[i].type==type)
                        return i
                }
                return -1;
            }
//-------------------------------------------------------------
/*            $scope.$watch('logFlag',function(newVal)
            {
                if(newVal)
                    $scope.alerts.push($scope.logAlert)
                else
                    $scope.closeAlert($scope.findAlert("log"))
            });
            $scope.$watch('readFlag',function(newVal)
            {
                if(newVal)
                    $scope.alerts.push($scope.readAlert)
                else
                    $scope.closeAlert($scope.findAlert("read"))
            });
            $scope.$watch('replayReady',function(newVal)
            {
                if(newVal)
                    $scope.alerts.push($scope.successAlert)
                else
                    $scope.closeAlert($scope.findAlert("success"))
            });*/
 //---------------------------------------------------------------------

            $scope.pValueDefault=2;
            $scope.iValueDefault=0;
            $scope.dValueDefault=0.05;

            $scope.pValue=$scope.pValueDefault;
            $scope.iValue=$scope.iValueDefault;
            $scope.dValue=$scope.dValueDefault;


            $scope.savedTrajectories = [];
            $scope.teachPoints = [];
            $scope.replays=[];
            //gets all the states from command
            $scope.states=[];
            //get each state from server and push it to detailed states
            $scope.statesJSON=[];

  //---------------------------------------------------
            $scope.trajectory = {
                readable: true,
                id: '0',
                name: 'No Trajectory Available',
                teachPoints: [],
                replays:[]
            }
 //-----------------------------------------------------
            $scope.teachPoint = {
                id: '0',
                name: 'Untitled Name',
                position: [
                    {label: 'x', value: '0.00'},
                    {label: 'y', value: '0.00'},
                    {label: 'z', value: '0.00'}
                ],
                orientation:[
                    {label:'Roll',value:0},
                    {label:'Pitch',value:0},
                    {label:'Yaw',value:0}
            ]
            };//end of teach point declaration
//----------------------------------------------------------
            $scope.replay={
                id:0,
                PID:{},
                positions:[
                  /*  {
                    timestamp:0,
                    position:[
                        {label: "x", value: 0},
                        {label: "y", value: 0},
                        {label: "z", value: 0}
                    ]
                }*/]
            }
//---------------------------------------------------------
            $scope.PID={
                ActuatorID: 0,
                P: $scope.pValue,
                I: $scope.iValue,
                D:$scope.dValue
            }
 //-------------------------------------------------------------
            $scope.$watch('savedTrajectories',function()
            {
                console.log("savedTrajectories is updated!...sending to visualization component...");
                $rootScope.$broadcast('savedTrajectoryUpdate',$scope.savedTrajectories);
            },true);
 //-------------------------------------------------------------
            $scope.$watch('[pValue,iValue,dValue]',function()
            {
                $scope.PID={
                    ActuatorID: 0,
                    P: $scope.pValue,
                    I: $scope.iValue,
                    D:$scope.dValue
                }
                console.log("PID is updated")
            },true);
            //--------------------------------------------
            function asyncLoop(iterations, func, callback) {
                var index = 0;
                var done = false;
                var loop = {
                    next: function() {
                        if (done) {
                            return;
                        }
                        if (index < iterations) {
                            index++;
                            func(loop);
                        } else {
                            done = true;
                            callback();
                        }
                    },
                    iteration: function() {
                        return index - 1;
                    },
                    break: function() {
                        done = true;
                        callback();
                    }
                };
                loop.next();
                return loop;
            }
            //--------------------------------
            function someFunction(callback) {
                //console.log('in the loop!');
                callback();
            }
            //-----------------------------------------
            $scope.run=function(iterations) {
                console.log("started reading | readFlag true")
                $scope.readFlag=true;
                $scope.alerts.push($scope.readAlert)
                asyncLoop(iterations, function (loop) {
                        someFunction(function (result) {

                            $scope.getDeviceIOInfo($scope.states[loop.iteration()].loc);
                            // log the iteration
                            console.log(loop.iteration());
                            // Okay, for cycle could continue
                            loop.next();
                        })
                    },
                    function () {
                        console.log('cycle ended')
                    }
                );
            }
            //--------------------------------------------------------------
            $scope.getDeviceIOInfo=function(deviceIOLocation)
            {
                RepoService.getObject("DeviceIO",deviceIOLocation).then(function(response){
                    var jsonString=response.data.value.json;
                    var jsonObject=jQuery.parseJSON( jsonString );
                    $scope.statesJSON.push(jsonObject);
                    console.log("added one more!")
                    if($scope.states.length==$scope.statesJSON.length) {
                        console.log("added all JSONS & finished reading | readFlag false");
                        console.log($scope.statesJSON);
                        $scope.readFlag=false;
                        $scope.closeAlert($scope.findAlert("read"))
                        $scope.createReplay();
                    }

                },function(error){})
            }
//------------------------------------------------------------
            $scope.createReplay=function()
            {
                $scope.replay={};
                $scope.replay.positions=[];
                $scope.replay.PID=angular.copy($scope.PID);
               // $scope.replay.ID=$scope.trajectory.replays.length+1;
               // $scope.replay.name="Gains "+$scope.replay.ID+" ("+$scope.PID.P+","+$scope.PID.I+","+$scope.PID.D+")";
                for(var i=0;i<$scope.statesJSON.length;i++)
                {
                    var positionObject={};
                    positionObject.position=[];
                    positionObject.timestamp=i+1;
                    var linear=$scope.statesJSON[i].position.linear;
                    var x={}; var y={}; var z={};
                    x.label="x"; x.value=linear.x;
                    y.label="y"; y.value=linear.y;
                    z.label="z"; z.value=linear.z;
                    positionObject.position.push(x); positionObject.position.push(y); positionObject.position.push(z);
                    $scope.replay.positions.push(positionObject);
                }
                $scope.trajectory.replays.push(angular.copy($scope.replay));
                //add replay to saved trajectories
                var index = $scope.find($scope.trajectory.id, $scope.savedTrajectories);
                $scope.savedTrajectories[index] = angular.copy($scope.trajectory);
                console.log($scope.trajectory);
                console.log($scope.savedTrajectories);
                console.log("finished creating replay | replayReady true")
                $scope.replayReady=true;
                $scope.alerts.push($scope.successAlert)
            }

//-------------------------------------------------------------
            $scope.collectTwists=function()
            {

                var twistList=[];
                for(var i= 0; i<$scope.trajectory.teachPoints.length; i++)
                {
                    var twist=new Object();
                    twist.angular=new Object();
                    twist.linear=new Object();

                    twist.linear.x=$scope.trajectory.teachPoints[i].position[0].value;
                    twist.linear.y=$scope.trajectory.teachPoints[i].position[1].value;
                    twist.linear.z=$scope.trajectory.teachPoints[i].position[2].value;

                    twist.angular.x=$scope.trajectory.teachPoints[i].orientation[0].value;
                    twist.angular.y=$scope.trajectory.teachPoints[i].orientation[1].value;
                    twist.angular.z=$scope.trajectory.teachPoints[i].orientation[2].value;

                    twistList.push(twist)
                }
                return twistList;
            }
//-------------------------------------------------------------
            $scope.submitTrajectory=function()
            {
                console.log("trajectory is being submitted | replayReady false")
                $scope.replayReady=false;
                $scope.closeAlert($scope.findAlert("success"))
                if($scope.pValue==$scope.pValueDefault && $scope.iValue==$scope.iValueDefault && $scope.dValue==$scope.dValueDefault) {
                    var command=RobotService.setTrajectory(robotPath, $scope.collectTwists())
                    command.then(
                        function(response){
                            $scope.getCommandInfo(response.data.label)
                        },
                        function(error){}
                    )
                }
                else {
                    $scope.PID.P=$scope.pValue;
                    $scope.PID.I=$scope.iValue;
                    $scope.PID.D=$scope.dValue;
                    var command=RobotService.setTrajectoryPID(robotPath, $scope.collectTwists(), $scope.PID);
                    command.then(
                        function(response){
                            $scope.getCommandInfo(response.data.label)
                        },
                        function(error){}
                    )
                }
            }
//------------------------------------------------------------------------
            $scope.getCommandInfo=function(label)
            {
                //label=103;
                var command=RepoService.getObject("DeviceCommand","DeviceCommand-"+label);
                command.then(function(response) {
                        $scope.states=response.data.value.states.value;
                        $scope.statesJSON=[];
                        console.log("states from command label: ");
                        console.log($scope.states);
                        if($scope.states.length!=$scope.prevLogLength || $scope.states.length==0 || $rootScope.robotStatus=="BUSY")
                        {
                            //$rootScope.robotStatus
                            console.log("either states length is zero or is different from previous length | logFlag true")
                            $scope.logFlag=true;
                            var index=$scope.findAlert("log")
                            if (index==-1)
                                 $scope.alerts.push($scope.logAlert)
                            $scope.prevLogLength=$scope.states.length;
                            setTimeout($scope.getCommandInfo(label),10000);
                        }
                        else {
                            console.log("logging condition (states length equals prev length) is fulfilled | logFlag false")
                            $scope.logFlag=false;
                            $scope.closeAlert($scope.findAlert("log"))
                            console.log("started reading data...")
                            $scope.run($scope.states.length);
                        }
                    },
                    function(error){}
                )
            }
//----------------------------------------------------------------------------
            $scope.emptyTrajectory = function () {
                $scope.readable = true;
                $scope.trajectory.id = '0';
                $scope.trajectory.name = 'No Trajectory Available';
                $scope.trajectory.teachPoints = [];
            }
//---------------------------------------------------------------------------
            //creates a new trajectory
            $scope.createTrajectory = function () {

                var setNumber = (($scope.savedTrajectories).length) + 1;
                $scope.trajectory.name = "Trajectory " + setNumber;
                $scope.trajectory.id = setNumber;
                $scope.trajectory.teachPoints = [];
                $scope.trajectory.replays=[];
                $scope.savedTrajectories.push(angular.copy($scope.trajectory));
            }
//---------------------------------------------------------------------------
            $scope.createTrajectory();
            //loads a new trajectory
            $scope.loadTrajectory = function (loadedTrajectory) {
                $scope.trajectory = angular.copy(loadedTrajectory);
            }
//--------------------------------------------------------------------------
            //add a teach point to the teach point set
            $scope.addTeachPoint = function () {
                RobotService.getPose(robotPath).then(function (poseState) {

                        /*console.log("this is the teach point");
                        console.log(poseState);*/
                        $scope.teachPoint.position[0].value = poseState.position.x.toFixed(4);
                        $scope.teachPoint.position[1].value = poseState.position.y.toFixed(4);
                        $scope.teachPoint.position[2].value = poseState.position.z.toFixed(4);

                        $scope.teachPoint.orientation[0].value = poseState.orientation.x.toFixed(4);
                        $scope.teachPoint.orientation[1].value = poseState.orientation.y.toFixed(4);
                        $scope.teachPoint.orientation[2].value = poseState.orientation.z.toFixed(4);

                        var setNum = ($scope.trajectory.teachPoints).length;
                        $scope.teachPoint.name = "Position " + (setNum + 1);
                        $scope.teachPoint.id = setNum + 1;
                        $scope.trajectory.teachPoints.push(angular.copy($scope.teachPoint));
                        var index = $scope.find($scope.trajectory.id, $scope.savedTrajectories);
                        $scope.savedTrajectories[index] = angular.copy($scope.trajectory);
                        console.log("the following teach point was added into " + $scope.trajectory.name);
                        console.log($scope.teachPoint);
                    },
                    function (response) {
                        console.log(response);
                    });
            }
//------------------------------------------------------------------------------
            $scope.deleteTrajectory = function (trajectory) {
                var index = $scope.find(trajectory.id, $scope.savedTrajectories);
                $scope.savedTrajectories.splice(index, 1);
                if ($scope.trajectory.id == trajectory.id && $scope.savedTrajectories.length > 0)
                    $scope.trajectory = $scope.savedTrajectories[0];
                else if ($scope.trajectory.id == trajectory.id && $scope.savedTrajectories.length == 0)
                    $scope.emptyTrajectory();
            }
//----------------------------------------------------------------------------------
            $scope.deleteTeachPoint = function (teachPoint) {
                var index = $scope.find(teachPoint.id, $scope.trajectory.teachPoints)
                $scope.trajectory.teachPoints.splice(index, 1);
            }
//---------------------------------------------------------------------------------
            $scope.find = function (id, array) {
                for (var index = 0; index < array.length; index++)
                    if (array[index].id == id)
                        return index;
            }
//------------------------------------------------------------------------------------

            $scope.editSavedTrajectories = function (index) {
                $scope.savedTrajectories[index].readable = false;
                $(".menu-input:eq(" + index + ")").focus();
                console.log($scope.trajectory);
            }
//----------------------------------------------------------------------------
            $scope.change = function (index) {
                if ($scope.savedTrajectories[index].id == $scope.trajectory.id) {
                    $scope.trajectory.name = $scope.savedTrajectories[index].name;
                }

            }
//---------------------------------------------------------------------------------------------------
            $scope.downloadTrajectory = function () {
                var data = "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify($scope.trajectory));
                var href = "data:" + data;
                var download = $scope.trajectory.name + ".json"
                $(".trajectory-component__downloadBtn").attr("href", href);
                $(".trajectory-component__downloadBtn").attr("download", download);
            }
//-----------------------------------------------------------------------------------------------

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