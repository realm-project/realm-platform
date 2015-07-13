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

                $scope.streamPath = localStorage.basePath + $scope.component.componentOptions.url;
                $scope.streamType = "imageArray";
                if ($scope.component.componentOptions.streamType !== undefined){
                   $scope.streamType = $scope.component.componentOptions.streamType;
                }

            },
            compile: function CompilingFunction(tElement, tAttrs)
            {
                //can only manipulate DOM here (can't access scope yet)
                
                return function linkingFunction(scope, iElement, iAttrs, controller) {
                    $timeout(function(){ 
                        if (scope.streamType === "MJPEG"){
                            // do nothing
                        }else{
                            // imageArray streamType
                            
                            var cameraPath = scope.component.componentOptions.url;

                            var componentContent = iElement.parent();
                            var cameraFeedComponent = iElement;
                            var canvas = $(iElement).find('.camera-feed-component__canvas');
                            var canvasContainer = $(iElement).find('.camera-feed-component__container');
                            var canvasElement = $(iElement).find(".camera-feed-component__canvas").get(0);
                            var canvasContext = canvasElement.getContext('2d');
                            var ComponentController = scope.ComponentController;
                            
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
                        // Packery reload after changing the camera size(newUI)
                        var $container = $('.ui-experiment__container');
                        $container.packery({
                            itemSelector: '.ui-section',
                            gutter: 0,
                            percentPosition: true
                        });
                      
                        // support camera resizing
                        if (scope.component.componentOptions.resizable !== undefined && scope.component.componentOptions.resizable === true && scope.streamType === "MJPEG"){

                        }
                    }); // end of timeout 
                }
            }
        }
    });
    



