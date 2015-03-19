'use strict';

app.directive('cameraFeedComponent' , function($timeout, $http, $q, CameraFeedService) {
        return {
            restrict: 'E',
            replace: false,
            scope: true,
            template: "<div class='camera-feed-component__container'>" +
                          "<canvas class='camera-feed-component__canvas'>" + 
                          "</canvas>" +
                      "</div>",
            controller: function CameraFeedComponentController($scope, $element, $attrs)
            {
                var cameraPath = $scope.component.componentOptions.url;
                
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
            },
            compile: function CompilingFunction(tElement, tAttrs)
            {
                //can only manipulate DOM here (can't access scope yet)
                //tElement.replaceWith(this.template);
                
                return function LinkingFunction(scope, element, attrs, parentController) {
                }
            }
        }
    });
    



