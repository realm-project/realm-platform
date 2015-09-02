'use strict';

angular.module('REALM').directive('uiExperiment',['$timeout', '$http', '$q','RobotService' ,function($timeout, $http, $q,RobotService) {
  return {
    restrict: 'E',
    replace: false,
    scope: {
        layout: '=layout'
    },
    template: '<div class="js-packery ui-experiment__container" data-packery-options=\'{ "itemSelector": ".ui-section", "gutter": 10 }\'>' +
                    "<ui-section class='ui-section' ng-repeat='section in layout.sections' section='section' layout-options='layout.options'>" +
                    "</ui-section>" +
              "</div>",

    compile: function CompilingFunction(tElement, tAttrs)
    {
        //can only manipulate DOM here (can't access scope yet)
        return function LinkingFunction(scope, element, attrs, ctrl, transcludeFn) {
            
        }
      
    },
    controller: function ControllerFunction($scope, $rootScope, $element, $attrs)
    {
        
        
        if ($scope.layout.options != null && $scope.layout.options.supportRobotMode !== undefined && $scope.layout.options.supportRobotMode === false){
            // skip the robot mode request if supportRobotMode===false
        }else{
            // get the robot mode in a timeout
            // skip the robot mode if robotPath was undefined
            if ($scope.layout.options != null && $scope.layout.options.robotPath !== undefined){
                var robotPath = $scope.layout.options.robotPath;
                $scope.robotMode="IDLE";
                $rootScope.robotStatus="IDLE";

                var robotTimeout;
                var getRobotState = function(){
                    RobotService.getMode(robotPath).then(function(mode){
                        $scope.robotMode=mode;
                        $rootScope.robotStatus=mode;
                        robotTimeout = setTimeout(getRobotState,200);
                    }, function(errorResponse){
                        console.log(errorResponse);
                        robotTimeout = setTimeout(getRobotState,200);
                    });
                };
                getRobotState();
            }
        }

        $scope.$on("$destroy", function(){
            clearTimeout(robotTimeout);
        });


        $scope.$watch('robotMode',function()
        {
            if($scope.robotMode=="IDLE")
            {
                $("body").css("cursor", "default");
                console.log("robot stopped | Mode: "+$scope.robotMode);
            }
            if($scope.robotMode=="BUSY")
            {
                $("body").css("cursor", "progress");
                console.log("robot started moving.... | Mode: "+$scope.robotMode);
            }
        });

    }
  }
}])