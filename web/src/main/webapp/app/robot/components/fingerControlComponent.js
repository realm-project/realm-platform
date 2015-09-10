'use strict';

app.directive('fingerControlComponent', ['$timeout', '$http', '$q', 'RobotService', function($timeout, $http, $q, RobotService) {

    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template:
            '<div class="finger-control-component__container">' +
                '<form class="fingerControlForm">'+
                    '<input type="radio" ng-model="fingerStatus" value="open" class="fingerRadioBtn"><label class="radioLabel">Open</label>'+
                    '<input type="radio" ng-model="fingerStatus" value="neutral" class="fingerRadioBtn"><label class="radioLabel">Neutral</label>'+
                    '<input type="radio" ng-model="fingerStatus" value="close" class="fingerRadioBtn"><label class="radioLabel">Close</label>'+
                '</form>'+
                '<div class="fingerControlSubmit">'+
                '<button class="btn btn-primary" ng-click="gripActivate()">Submit</button>'+
                '</div>'+
           '</div>',

        controller: function ControllerFunction($scope,$element,$attrs,$rootScope)
        {
            var robotPath = $scope.component.componentOptions.url;
            $scope.device=true;
            if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
                $scope.device=false;
            }
            $scope.fingerStatus="neutral";
            $scope.fingers = {

                open: {Finger_1:2000, Finger_2:2000, Finger_3:0},
                neutral: {Finger_1:3000, Finger_2:3000, Finger_3:0},
                close: {Finger_1:5000, Finger_2:5000, Finger_3:0}
            };

            $scope.gripActivate=function()
            {
                console.log($scope.fingerStatus);
                console.log($scope.fingers[$scope.fingerStatus]);
                RobotService.grip(robotPath,$scope.fingers[$scope.fingerStatus]);
            };


        },
        compile: function CompilingFunction(tElement, tAttrs)
        {
            //can only manipulate DOM here (can't access scope yet)
            return function LinkingFunction(scope, element, attrs, ctrl) {
                //can access scope now
            }
        },
        link: function(scope, element, attrs)
        {

        }


    }
}]);





