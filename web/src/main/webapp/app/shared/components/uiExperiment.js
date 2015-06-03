'use strict';

angular.module('REALM').directive('uiExperiment',['$timeout', '$http', '$q','RobotService' ,function($timeout, $http, $q,RobotService) {
  return {
    restrict: 'E',
    replace: false,
    scope: {
        layout: '=layout'
    },
    template: "<div class='ui-experiment__container'>" +
            //    "<div class='ui-experiment__scroller'>" +
                  "<div class='ui-experiment__content'>" +
                    "<ui-section class='ui-section' ng-repeat='section in layout.sections' ng-model='layout.sections' section='section'>" +
                    "</ui-section>" +
                  "</div>" +
               // "</div>" +
              "</div>",

    compile: function CompilingFunction(tElement, tAttrs)
    {
      //can only manipulate DOM here (can't access scope yet)

      
      return {
        pre: function(scope, element, attrs, ctrl, transcludeFn) {

        },
        post: function(scope, element, attrs, ctrl, transcludeFn) {
          
        }
      }
    },
    controller: function ControllerFunction($scope, $rootScope, $element, $attrs)
    {
        //window.hScroll = new IScroll($($element).find('.ui-experiment__container').get(0), { scrollX: true, scrollY: false, mouseWheel: false, disableMouse: true, scrollbars: true, interactiveScrollbars:true, snap:true });
        //window.vScrolls = [];
        //**************************BUSY INDICATOR CODE****************************/
        //this should get updated dynamically
        // the default value is for mico
        var robotPath = "rest/device/mico";
        if (typeof($scope.layout.options.robotPath) != "undefined"){
            robotPath = $scope.layout.options.robotPath;
        }
        $scope.robotMode="IDLE";
        $rootScope.robotStatus="IDLE";
        var getRobotState = function(){
            RobotService.getMode(robotPath).then(function(mode){
                $scope.robotMode=mode;
                $rootScope.robotStatus=mode;
                //console.log(mode)
                setTimeout(getRobotState,30);
            }, function(response){
                //console.log(response);
                setTimeout(getRobotState,30);
            });
        };
        getRobotState();

        $scope.$watch('robotMode',function()
        {
            if($scope.robotMode=="IDLE")
            {
               document.body.style.cursor='default';
                $rootScope.robotStatus=$scope.robotMode;
                $("body").css("cursor", "default");
                console.log("robot stopped | Mode: "+$scope.robotMode);
            }
            if($scope.robotMode=="BUSY")
            {
                //document.body.style.cursor='wait';
               // $rootScope.robotStatus=$scope.robotMode;
                $("body").css("cursor", "progress");
                console.log("robot started moving.... | Mode: "+$scope.robotMode);
            }
        });
        //***************************************************************************/

    }
  }
}])