'use strict';

angular.module('REALM')
    .service('CameraFeedService',function($http, $q){
        
        var that = this;

        this.getCurrentFrame = function(cameraPath){
            var src = $q.defer();
            
            $http.get(localStorage.basePath + cameraPath).then(function(response){
                
                if( angular.isDefined(response.data) && angular.isDefined(response.data.image) && response.data.image !== null)
                {
                    src.resolve("data:image/png;base64," + response.data.image);
                }
                else
                {
                    src.reject("problem loading image");
                }
            },function(response){
                console.log('Failed to load camera feed image, error code:');
                console.log(response.status);
            });

            return src.promise;
        }

        this.setSize = function(cameraPath, width, height){
            
            var deferred = $q.defer();

            var postData = {
                "action":"setSize",
                "arguments":{
                    "width":width,
                    "height":height
                }
            };

            $http.post(localStorage.basePath + cameraPath, postData).then(function(response){
                deferred.resolve();
            }, function(response){
                deferred.reject();
            });

            return deferred.promise;
        };

        this.setQuality = function(cameraPath, quality)
        {
            var deferred = $q.defer();

            if(quality > 1.0 || quality < 0)
            {
                console.log('Invalid Quality Value, must be between 0 - 1.0');
                deferred.reject('Invalid quality value');
            }

            var postData = {
                "action":"setQuality",
                "arguments":{
                    "quality": quality
                }
            }

            $http.post(localStorage.basePath + cameraPath, postData).then(function(response){
                deferred.resolve();
            },function(response){
                deferred.reject();
            });

            return deferred.promise;
        }
    });