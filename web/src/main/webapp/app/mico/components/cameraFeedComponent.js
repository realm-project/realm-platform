'use strict';

app.directive('cameraFeedComponent' , function($timeout, $http, $q, CameraFeedService) {
        return {
            restrict: 'E',
            replace: false,
            scope: true,
            template: "<div class='camera-feed-component__container' ng-switch='streamType'>" +
                        "<canvas ng-switch-when='imageArray' class='camera-feed-component__canvas'> </canvas>" +
                        "<img ng-switch-when='MJPEG' class='camera-feed-cameraImage' src='{{streamPath}}'>"+
                        "<canvas ng-switch-default class='camera-feed-component__canvas'> </canvas>" +
                      "</div>",
            controller: function CameraFeedComponentController($scope, $element, $attrs)
            {

                var cameraPath = $scope.component.componentOptions.url;
                $scope.streamPath = localStorage.basePath + cameraPath;
                $scope.streamType = "imageArray";
                if ($scope.component.componentOptions.streamType !== undefined){
                   $scope.streamType = $scope.component.componentOptions.streamType;
                }

                if ($scope.streamType === "MJPEG"){


                }else{
                    // imageArray streamType

                    var componentContent = $element.parent();
                    var cameraFeedComponent = $element;
                    var canvas = $($element).find('.camera-feed-component__canvas');
                    var canvasContainer = $($element).find('.camera-feed-component__container');
                    var canvasElement = $($element).find(".camera-feed-component__canvas").get(0);
                    var canvasContext = canvasElement.getContext('2d');
                    var ComponentController = $scope.ComponentController;
                    
                    //CameraFeedService.setSize(viewerWidth);
                    
                    var imgData;
                    var img = new Image();
                    
                    var renderVideo = function(){
                        var promise = CameraFeedService.getCurrentFrame(cameraPath);
                        promise.then(function(message){
                            imgData = message;
                            img.src = imgData;

                            canvasContext.canvas.width = canvasContainer.width();
                            canvasContext.canvas.height = canvasContainer.height();
                            
                            var computedHeight = (img.height/img.width)*canvasContext.canvas.width;
                            canvasContext.drawImage(img,0,0,canvasContainer.width(),computedHeight);
                            
                            canvasContainer.height(computedHeight);
                            setTimeout(renderVideo,30);
                        },function(response){
                            console.log('Failed to get camera feed image, error code: ');
                            console.log(response.status);
                            setTimeout(renderVideo,30);  
                        });
                    }
                    renderVideo();
                }
            },
            compile: function CompilingFunction(tElement, tAttrs)
            {
                //can only manipulate DOM here (can't access scope yet)
                //tElement.replaceWith(this.template);
                return{
                    pre: function preLink(scope, iElement, iAttrs, controller) {
                        // it is the best place to append extra DOMs based on the scope options
                    },
                    post: function postLink(scope, iElement, iAttrs, controller) {

                    }
                }
            }
        }
    });
    



