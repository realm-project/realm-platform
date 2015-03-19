'use strict';

angular.module('REALM').directive('uiComponent', function($compile) 
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
        "poseVisualization":"<pose-visualization-component></pose-visualization-component>"

        
    }    
    
    return {
        require:'^uiSection',
        restrict: 'E',
        replace: false,
        scope: {
            component: '=component',
            sectionOptions: '=sectionOptions',
            toggleComponentShowing: '&toggleComponentShowing'
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
            return {
                pre: function(scope, element, attrs, sectionController) {

                },
                post: function(scope, element, attrs, sectionController) {
                  
                }
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