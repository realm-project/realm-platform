'use strict';

angular.module('REALM').service('RobotService',function($http, $q, $state, $rootScope){         
        
    var that = this;
    var handleError = true;

    /*********HELPER FUNCTIONS***********/
    this.hashToArray = function(object) {
        var returnArray = [];

        for (var key in object) {
            returnArray.push(object[key]);
        }

        return returnArray;
    }


    /**************GETTERS**************/
    this.getJoints = function(devicePath){
        var angleSet = $q.defer();
        
        $http.get(localStorage.basePath + devicePath).then(function(response){

            var jointData = {
                degrees: that.hashToArray(response.data.joints),
                radians: response.data.jointState.position
            }
            
            angleSet.resolve(jointData);
        
        }, function(errorResponse){
            console.log('Failed to get joint angles');
            console.log(errorResponse);

            if (handleError){
                resolveError(errorResponse , angleSet);
            }else{
                angleSet.reject(errorResponse);
            }
        });
        
        return angleSet.promise;
    };

    this.getPose = function(devicePath) {
        var pose = $q.defer();

        $http.get(localStorage.basePath + devicePath).then(function(response){
            var poseData = {
                position:response.data.position.linear,
                orientation:response.data.position.angular
            }
            pose.resolve(poseData);
        },function(errorResponse){
            console.log('Failed to get pose');
            console.log(errorResponse);

            if (handleError){
                resolveError(errorResponse, pose);
            }else{
                pose.reject(errorResponse);
            }
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
        },function(errorResponse){
            console.log('Failed to send robot setJoints command');
            console.log(errorResponse);

            if (handleError){
                resolveError(errorResponse, null);
            }
        });
    }
//-------------------------------------------------------------------------------------------------------
    this.setPose = function(devicePath, twist) {
        $http.post(localStorage.basePath + devicePath, twist).then(function(){
            console.log('Sent the following pose to arm successfully:');
            console.log(twist);
        },function(errorResponse){
            
            console.log('Failed to send robot setJoints command');
            console.log(errorResponse);
            if (handleError){
                resolveError(errorResponse, null);
            }
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
            defered.resolve(response)
        },function(errorResponse){
            console.log('Failed to send robot setJoints command');
            console.log(errorResponse);
            
            if (handleError){
                resolveError(errorResponse, defered);
            }else{
                defered.reject(errorResponse);
            }
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
            defered.resolve(response)
        },function(errorResponse){
            console.log('Failed to send robot setJoints command');
            console.log(errorResponse);
            
            if (handleError){
                resolveError(errorResponse, defered);
            }else{
                defered.reject(errorResponse);
            }
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
                console.log('Failed to send robot to home position');
                console.log(errorResponse);
            
                if (handleError){
                    resolveError(errorResponse, null);
                }
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
        },function(errorResponse){
            console.log('Failed to activate the gripper');
            console.log(errorResponse);
            
            if (handleError){
                resolveError(errorResponse, null);
            }
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
                if (errorResponse.status !== undefined && errorResponse.status == 429){
                    // "that" is a reference to current service
                    console.log("resend the zero command")
                    that.move(devicePath,axis0, axis1, axis2, axis3, button0, button1, button2, button3);
                }else{
                    console.log("Robot 'move' action failed (zero command)");
                    console.log(errorResponse);
                    
                    if (handleError){
                        resolveError(errorResponse, null);
                    }
                }
            }else{
                console.log("Robot 'move' action failed");
                console.log(errorResponse);
                if (handleError && !(errorResponse.status !== undefined && errorResponse.status == 429)){
                    resolveError(errorResponse, null);
                }
            }
        });
    }

    // in case of handleError == true, we do not resolve the promise and call resolveError function
    function resolveError(errorResponse, defered){
        if (errorResponse.status !== undefined && errorResponse.status == 401){
            // timeout error
            $state.go('login');
            $rootScope.toggle('generalTimeoutError','on');
        }else{
            // other errors
            $state.go('login');
            $rootScope.toggle('generalError','on');
        }
    }

});