'use strict';

angular.module('REALM').directive('uiComponent', function($compile,$timeout) 
{  
    var componentContentTemplates = 
    {
        "cameraFeed": "<camera-feed-component></camera-feed-component>",
        "angleInput": "<angle-input-component></angle-input-component>",
        "poseInput": "<pose-input-component></pose-input-component>",
        "angleState": "<angle-state-component></angle-state-component>",
        "poseState": "<pose-state-component></pose-state-component>",
        "simulation": "<simulation-component></simulation-component>",
        "joystickInput": "<joystick-input-component></joystick-input-component>",
        "trajectorySaveComponent": "<trajectory-save-component></trajectory-save-component>",
        "trajectorySaveReplayComponent": "<trajectory-save-replay-component></trajectory-save-replay-component>",
        "playBack": "<play-back-component></play-back-component>",
        "poseVisualization":"<pose-visualization-component></pose-visualization-component>",
        "fingerControl":"<finger-control-component></finger-control-component>"

        
    }    
    
    return {
        require:'^uiSection',
        restrict: 'E',
        replace: false,
        scope: {
            component: '=',
            sectionOptions: '=',
            toggleComponentShowing: '&toggleComponentShowing',
            layoutOptions: '='

        },
        template:   "<div class='ui-component__container'>" + 
                        "<div class='ui-component__header'>" +
                            "<h3 class='ui-component__title'>{{component.componentOptions.title}}</h3>" +
                        "</div>" +
                        "<div class='ui-component__content'>" +
                        "</div>" +
                        "<div class='ui-component__footer'>" +
                        "</div>" +
                    "</div>",
        compile: function CompilingFunction(tElement, tAttrs)
        {
            return function linkingFunction(scope, element, attrs, sectionController) {
                // packery reload (new UI)
                $timeout(function(){ 

                    var $container = $('.ui-experiment__container');
                    
                    $container.packery({
                      itemSelector: '.ui-section',
                      gutter: 0,
                      percentPosition: true
                    });
              
                    var $itemElems = $($container.packery('getItemElements'));
                    
                    if (scope.layoutOptions === undefined || scope.layoutOptions === null || scope.layoutOptions.draggable === undefined || scope.layoutOptions.draggable!==false){
                        // make item elements draggable
                        $itemElems.draggable({
                            handle: ".ui-component__header"
                        });
                        // bind Draggable events to Packery
                        $container.packery( 'bindUIDraggableEvents', $itemElems );
                    }
                });

            }
        },
        controller: function ComponentController($scope, $element, $attrs)
        {
            $scope.ComponentController = this;
            
            var componentContentHtmlString = componentContentTemplates[$scope.component.componentOptions.type];
            var element_componentContent = $($element).find('.ui-component__content');
                
            var compiledComponentContentHtmlString = $compile(componentContentHtmlString)($scope);
            element_componentContent.append(compiledComponentContentHtmlString);
        }
    }
})