'use strict';

app.directive('joystickInputComponent', ['RobotService', function(RobotService) {
  return {
    restrict: 'E',
    replace: false,
    template:
        "<div class='joystick-input-component__labels'>"+
            "<span ng-if='component.componentOptions.stick1X || component.componentOptions.stick1Y' class='joystick-input-component__label'>{{component.componentOptions.stick1Caption}}" +
            "<span class='glyphicon glyphicon-info-sign' ng-controller='joystickModalCtrl' ng-click='open(xyLabel)' ></span>"+
            "</span>"+
            "<span ng-if='component.componentOptions.stick2Y' class='joystick-input-component__label'>{{component.componentOptions.stick2Caption}}"+
            "<span class='glyphicon glyphicon-info-sign' ng-controller='joystickModalCtrl' ng-click='open(zLabel)' ></span>"+
            "</span> "+
        "</div>"+

        "<div class='joystick-input-component__container'>" +
                        "<joystick ng-class='{\"single\": !component.componentOptions.stick2Y}' ng-if='component.componentOptions.stick1X || component.componentOptions.stick1Y' gamepad='1' stick='1' xenabled='component.componentOptions.stick1X' yenabled='component.componentOptions.stick1Y' ></joystick>" +
                        "<joystick ng-class='{\"single\": !component.componentOptions.stick1X && !component.componentOptions.stick1Y}' ng-if='component.componentOptions.stick2Y' gamepad='1' stick='2' xenabled='false' yenabled='true' ></joystick>" +
        "</div>"+
            "<div class='joystick-input-component__buttons'>"+
                 "<button class='btn btn-primary' ng-click='goHome()' ng-show='component.componentOptions.showHomeButton'>Go Home</button>" +
            "</div>",

    compile: function CompilingFunction(tElement, tAttrs)
    {
      //can only manipulate DOM here (can't access scope yet)
      
      return function LinkingFunction(scope, element, attrs, ctrl)
      {
           $(element).mouseleave(function()
           {
              scope.$broadcast('LEFT-SCOPE');

           });
      }
    },
    controller: function ControllerFunction($scope, $element, $attrs)
    {
      // stick1 defaults
      if ($scope.component.componentOptions.stick1X === undefined)
        $scope.component.componentOptions.stick1X = true;
      if ($scope.component.componentOptions.stick1Y === undefined)
        $scope.component.componentOptions.stick1Y = true;
      if ($scope.component.componentOptions.stick1Caption === undefined)
        $scope.component.componentOptions.stick1Caption = "X, Y:";
      // stick2 defaults
      if ($scope.component.componentOptions.stick2Y === undefined)
        $scope.component.componentOptions.stick2Y = true;
      if ($scope.component.componentOptions.stick2Caption === undefined)
        $scope.component.componentOptions.stick2Caption = "Z:";


      var robotPath = $scope.component.componentOptions.url;
      
      window.joysticks = [];
      $scope.xyLabel="X, Y:";
      $scope.zLabel="Z:";

      this.moveJoystick = function(joystick, container, x, y){
        
        joystick._pressed = true;
        joystick._stickEl.style.display="";

        joystick._onMove(container.offset().left + 100 + (x*joystick._stickRadius),container.offset().top + 100 + (y*joystick._stickRadius));
        
        var x1 = window.joysticks[0].getX();
        var y1 = window.joysticks[0].getY();
        var y2 =0;
        if (window.joysticks.length >1){
          y2 = window.joysticks[1].getY();
        }
        
        this.moveRobot(x1, y1, y2);

        joystick._pressed=false;
      }
      

      this.moveRobot = function(x,y,z){
        RobotService.move(robotPath,x,y,z);
      }

      $scope.goHome=function()
      {
          RobotService.goHome(robotPath);
      }
    }    
  }
}])