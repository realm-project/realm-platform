'use strict';

app.directive('playBackComponent', ['$timeout', '$http', '$q', 'RobotService', 'RepoService', function($timeout, $http, $q, RobotService,RepoService) {

    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template:  '<div class="play-back-component__container"> '  +
            '<span class="play-back-component__uploadContainer">'+
                '<span>Upload a trajectory: </span>'+
                '<input class="play-back-component__upload" accept="file_extension" type="file" id="UploadedTrajectoryFile">'+
            '</span>'+

            '<div class="pid-inputs-container">'+
                '<div class="pid-input-container">' +
                    '<div class="pid-input-label-container">'+
                    '<h4 class="pid-input-label">P</h4>' +
                '</div>'+
                    '<div class="numeric-input-container">'+
                        '<numeric-input min="-6.28" max="6.28" step="0.0001" value=pValue></numeric-input>' +
                    '</div>'+
                '</div>' +

                '<div class="pid-input-container">' +
                    '<div class="pid-input-label-container">'+
                    '<h4 class="pid-input-label">I</h4>' +
                '</div>'+
                    '<div class="numeric-input-container">'+
                         '<numeric-input min="-6.28" max="6.28" step="0.0001" value=iValue></numeric-input>' +
                    '</div>'+
                '</div>' +

                '<div class="pid-input-container">' +
                    '<div class="pid-input-label-container">'+
                    '<h4 class="pid-input-label">D</h4>' +
                '</div>'+
                    '<div class="numeric-input-container">'+
                    '<numeric-input min="-6.28" max="6.28" step="0.0001" value=dValue></numeric-input>' +
                '</div>'+
                '</div>' +
            '</div>'+


            '<div class="angle-input-component__buttons">'+
                '<button class="angle-input-component__submitBtn btn btn-primary" ng-disabled="!file" ng-click="submitTrajectory()">Submit</button>' +
                '<button class="btn btn-primary" ng-click="goHome()">Go Home</button>' +
            '</div>'+

            '</div>'  ,
        controller: function ControllerFunction($scope,$element,$attrs,$rootScope)
        {

            var robotPath = $scope.component.componentOptions.url;
            $scope.pValue=0;
            $scope.iValue=0;
            $scope.dValue=0;
            $scope.file=""; //just to check whether a file is uploaded or not
            $scope.twists=[];
            $scope.uploadedTrajectory=new Object(); //contains the uploaded json file
            /*$scope.robotMode="DEFAULT";
            $scope.modeInterval;
            $scope.poseInterval;*/
            $scope.trajectories=[]; //contains all the played trajectories
            //trajectory structure
            $scope.trajectory= {
                name: 0,
                positions: [
                    {
                        timestamp: 0,
                        position: [
                            {label: "x", value: 0},
                            {label: "y", value: 0},
                            {label: "z", value: 0}
                        ]

                    }
                ]
            }
            //PID structure
            $scope.PID={
                ActuatorID: 0,
                P: 0,
                I: 0,
                D:0
            }
 //----------------------------------------------------------------
  $scope.collectTwists=function(uploaded)
  {
      var twistList=[];
      for(var i= 0; i<uploaded.teachPoints.length; i++)
      {
          var twist=new Object();
          twist.angular=new Object();
          twist.angular.x=1.61;
          twist.angular.y=0;
          twist.angular.z=0;
          twist.linear=new Object();
          twist.linear.x=uploaded.teachPoints[i].position[0].value;
          twist.linear.y=uploaded.teachPoints[i].position[1].value;
          twist.linear.z=uploaded.teachPoints[i].position[2].value;
          twistList.push(twist)
      }
      return twistList;
  }
//-----------------------------------------------------------------
            $element.find('input').bind('change', function(event,ui) {
                var file = event.target.files[0];
                if (file) {
                    var reader = new FileReader();
                    reader.readAsText(file, "UTF-8");
                    reader.onload = function (evt) {
                        $scope.file=evt.target.result;
                        $scope.uploadedTrajectory=JSON.parse($scope.file);
                        console.log($scope.file);
                    }
                    reader.onerror = function (evt) {
                        console.log("error reading the file")
                    }
                }
            });
//----------------------------------------------------------------------
            $scope.goHome=function()
            {
                RobotService.goHome(robotPath);
            }
//-----------------------------------------------------------------------
      /*      $scope.submitTrajectory=function()
            {
                var teachPointsLength=$scope.uploadedTrajectory.teachPoints.length;

                for(var i=0;i<teachPointsLength;i++) {
                    var teachPoint=$scope.uploadedTrajectory.teachPoints[i];
                    $scope.modeInterval=setInterval($scope.sendPose(teachPoint,i==0,i==teachPointsLength-1),30);
                }

            }*/
            $scope.submitTrajectory=function()
            {
                if($scope.pValue==0 && $scope.iValue==0 && $scope.dValue==0) {
                    var command=RobotService.setTrajectory(robotPath, $scope.collectTwists($scope.uploadedTrajectory))
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
                    var command=RobotService.setTrajectoryPID(robotPath, $scope.collectTwists($scope.uploadedTrajectory), $scope.PID);
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
                label=103;
                var command=RepoService.getObject("DeviceCommand","DeviceCommand-"+label);
                command.then(
                    function(response) {console.log(response.data)},
                    function(error){}
                )
            }
 /*           $scope.sendPose=function(teachPoint, first,last)
            {

                if($scope.robotMode=="IDLE") {
                    console.log("In sendPose: RobotMode is IDLE and we can send the pose and stop checking");
                    $scope.submitPose(teachPoint, first, last)
                    clearInterval($scope.modeInterval);
                }
                else
                {
                    console.log("In sendPose: RobotMode is NOT IDLE yet and we can not send the pose and we keep checking");
                }
            }
//------------------------------------------------------------------------
            $scope.submitPose = function(teachPoint, first, last){
                var postData = {
                    action: 'setPose',
                    arguments: {
                        linear: {
                            x: parseFloat(teachPoint.position[0].value),
                            y: parseFloat(teachPoint.position[1].value),
                            z: parseFloat(teachPoint.position[2].value)
                        },
                        angular: {
                            x: 0,
                            y: 0,
                            z: 0
                        }
                    }
                };
                RobotService.setPose(robotPath,postData);
                console.log('Sent the pose to arm');
                //console.log(postData);
                $scope.poseInterval=($scope.savePose(first, last),30); //last is true if the set pose is for the last, same for first
            }
//------------------------------------------------------------------------------
            $scope.savePose=function(first,last)
            {
                    if($scope.robotMode=="BUSY")
                    {
                        $scope.getPoseState(first, last);
                        console.log("in savePose: robot mode is now busy and we start saving new data");
                    }
                    if($scope.robotMode="IDLE")
                    {
                        clearInterval($scope.poseInterval);
                        console.log("in savePose: robot mode is now IDLE and saving has stopped")
                    }
            }
//------------------------------------------------------------------------------
            $scope.getPoseState = function(first, last){
                            RobotService.getPose(robotPath).then(function(poseState){
                                if(first)
                                {
                                    console.log("Adding the first position");
                                    var name=$scope.trajectories.length;
                                    var trajectory=new Object();
                                    trajectory.name=name;
                                    trajectory.positions.push(poseState.position);
                                    $scope.trajectories.push(trajectory);
                                }
                                else
                                {
                                    console.log("Adding the next position...")
                                    var length=$scope.trajectories.length;
                                    $scope.trajectories[length-1].positions.push(poseState.position);
                                }
                            }, function(response){
                                console.log("Unable to get pose: ")
                                console.log(response);
                            });
                        };
//-------------------------------------------------------------------------------
            var getRobotState = function(){
                            RobotService.getMode(robotPath).then(function(mode){
                                $scope.robotMode=mode;
                                setTimeout(getRobotState(),30);
                            }, function(response){
                                console.log("Unable to get robot state: ")
                                console.log(response);
                                setTimeout(getRobotState(),30);
                            });
                        };
              getRobotState();*/
//-------------------------------------------------------------------------------

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


        }//link


    }
}]);





