'use strict';

angular.module('REALM')
    .service('RobotService',function($http, $q){         
        
        var that = this;

        /*********HELPER FUNCTIONS***********/
        this.hashToArray = function(object) {
            var returnArray = [];

            for (var key in object) {
                returnArray.push(object[key]);
            }

            return returnArray;
        }

        /********ROBOT API***********/


            /**************GETTERS**************/
            this.getJoints = function(devicePath){
                var angleSet = $q.defer();
                
                $http.get(localStorage.basePath + devicePath).then(function(response){

                    var jointData = {
                        degrees: that.hashToArray(response.data.joints),
                        radians: response.data.jointState.position
                    }
                    
                    angleSet.resolve(jointData);
                
                }, function(response){
                    console.log('Failed to get joint angles, error code:');
                    console.log(response.status);

                    angleSet.reject(response.status);
                });
                
                return angleSet.promise;
            };

            this.getPose = function(devicePath) {
                var pose = $q.defer();

                $http.get(localStorage.basePath + devicePath).then(function(response){
                    //console.log(response.data);
                    var poseData = {
                        position:response.data.position.linear,
                        orientation:response.data.position.angular

                       //position:response.data.pose.position,
                       //orientation:response.data.pose.orientation
                    }

                    pose.resolve(poseData);
                },function(response){
                    console.log('Failed to get pose, error: ' + response.status);
                    pose.reject(response.status);
                });

                return pose.promise;
            };
            /****************CURRENT STATUS ROBOT*****************/
            this.getMode = function(devicePath) {
                var robotMode = $q.defer();
                $http.get(localStorage.basePath + devicePath).then(function(response){
                  //  console.log(response.data);
                    var mode = response.data.mode;
                    robotMode.resolve(mode);
                },function(response){
                    console.log('Failed to get the robot mode, error: ' + response.status);
                    robotMode.reject(response.status);
                });
                return robotMode.promise;
            };
            /********************SETTERS********************/
            this.setJoints = function(devicePath, jointAnglesArray){
                var postData = {
                    "record":false,
                    "action": "setJoints",
                    "arguments": {
                        
                    }
                }

                for(var i=0; i<jointAnglesArray.length; i++)
                {
                    postData.arguments['Angle_J' + (i+1)] = jointAnglesArray[i];
                };

                console.log('Set Joints POST object:')
                console.log(postData);
                $http.post(localStorage.basePath + devicePath,postData).then(function(response){
                    console.log('Sent robot setJoints command successfully');
                },function(response){
                    console.log('Failed to send robot setJoints command, error: ' + response.status);
                });
            }
//-------------------------------------------------------------------------------------------------------
            this.setPose = function(devicePath, twist) {
                $http.post(localStorage.basePath + devicePath, twist).then(function(){
                    console.log('Sent the following pose to arm successfully:');
                    console.log(twist);
                    //console.log('Sent robot setPose command successfully')
                },function(response){
                    //if(response.status=="429") {
                        //that.setPose(devicePath,twist); //this is to make sure that the request is not filtered
                        //console.log('Failed to send robot setPose command, error: ' + response);
                    //}
                    console.log('Failed to send robot setJoints command, error: ' + response.status);
                });
            }
//--------------------------------------------------------------------------------------------------------------
        this.setTrajectory = function(devicePath, twists) {
            var defered=$q.defer();
            var postData=
            {
                "action":"setTrajectory",
                "arguments": {
                    "numtwist": 0,
                    "twists": {}
                },
                "record":true
            }
            postData.arguments.numtwist=twists.length;
            postData.arguments.twists=twists;

            $http.post(localStorage.basePath + devicePath, postData).then(function(response){
                console.log('Sent the following trajectory to arm successfully:');
                console.log(postData);
                console.log('in RobotService: command number is:');
                console.log(response.data);
                defered.resolve(response)
            },function(error){
                console.log('Failed to send robot setJoints command, error: ' + error.status);
                defered.reject(error.status);
            });
            return defered.promise;
        }
 //-------------------------------------------------------------------------------------------------------------
        this.setTrajectoryPID = function(devicePath, twists, PID) {
            var defered=$q.defer();
            var postData=
            {
                "action": "setTrajectoryGains",
                "arguments": {
                    "numtwist": 0,
                    "twists": {},
                    "PIDs":[]

                }
            }

            /*postData.arguments.ActuatorPID.P=PID.P;
            postData.arguments.ActuatorPID.I=PID.I;
            postData.arguments.ActuatorPID.D=PID.D;
            postData.arguments.ActuatorPID.ActuatorID=PID.ActuatorID;*/
            for(var i=16;i<22;i++)
            {
                var PIDElement=new Object();
                PIDElement.P=PID.P;
                PIDElement.I=PID.I;
                PIDElement.D=PID.D;
                PIDElement.ActuatorID=i;
                postData.arguments.PIDs.push(PIDElement);
            }
            postData.arguments.numtwist=twists.length;
            postData.arguments.twists=twists;

            $http.post(localStorage.basePath + devicePath, postData).then(function(response){
                console.log('Sent the following trajectory to arm successfully:');
                console.log(postData);
                console.log('in RobotService: command number is:');
                console.log(response.data);
                defered.resolve(response)
            },function(error){
                console.log('Failed to send robot setJoints command, error: ' + error.status);
                defered.reject(error.status);
            });
            return defered.promise;
        }
//---------------------------------------------------------------------------------------------------------------
        this.goHome = function(devicePath){
                var postData = {
                    "action": "home"
                };

                $http.post(localStorage.basePath + devicePath,postData).then(function(response){
                    console.log('Sent robot to home position successfully');
                },function(response){
                    console.log('Failed to send robot to home position, error: ' + response.status);
                });
            }
//--------------------------------------------------------------------------------------------------------------
        this.grip = function(devicePath, fingers){
            var postData = {
                "action": "grip"
            };
            postData.arguments=fingers;
            $http.post(localStorage.basePath + devicePath,postData).then(function(response){
                console.log('Gripper activated using the following:');
                console.log(postData);
            },function(response){
                console.log('Failed to activate the gripper, error: ' + response.status);
            });
        }
//---------------------------------------------------------------------------------------------------------------
            //JOYSTICK
            this.move = function(devicePath,axis0, axis1, axis2, axis3, button0, button1, button2, button3) {

                var postData = {
                    'record':false,
                    'action': "move",
                    'arguments': {
                        'axes': {
                            'axis0':axis0,
                            'axis1':axis1,
                            'axis2':axis2,
                            'axis3':axis3
                        },
                        'buttons': {
                            'button0':button0,
                            'button1':button1,
                            'button2':button2,
                            'button3':button3
                        }
                    }
                };
                
                $http.post(localStorage.basePath + devicePath, postData).then(function(response){
                    // we sent move-command successfully
                    // do nothing now :)
                },function(errorResponse){
                    // resend the zero command in any condition
                    if( axis0===0 && axis1 ===0 && axis2 ===0 && axis3 ===0 && button0===false && button1===false && button2===false && button3 === false){
                        // "that" is a reference to current service
                        console.log("resend the zero command")
                        that.move(devicePath,axis0, axis1, axis2, axis3, button0, button1, button2, button3);
                    }
                    else{
                        console.log("Robot 'move' action failed");
                        //console.log(errorResponse);
                    }
                });
            }

    });