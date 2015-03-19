'use strict';

app.directive('joystickInputComponent', ['RobotService', function(RobotService) {
  return {
    restrict: 'E',
    replace: false,
    template:
        "<div class='joystick-input-component__labels'>"+
            "<span class='joystick-input-component__label'> X, Y:" +
            "<span class='glyphicon glyphicon-info-sign' ng-controller='joystickModalCtrl' ng-click='open(xyLabel)' ></span>"+
            "</span>"+
            "<span class='joystick-input-component__label'>Z:"+
            "<span class='glyphicon glyphicon-info-sign' ng-controller='joystickModalCtrl' ng-click='open(zLabel)' ></span>"+
            "</span> "+
        "</div>"+

        "<div class='joystick-input-component__container'>" +
                        "<joystick gamepad='1' stick='1' xenabled='true' yenabled='true' ></joystick>" +
                        "<joystick gamepad='1' stick='2' xenabled='false' yenabled='true' ></joystick>" +
        "</div>"+
            "<div class='joystick-input-component__buttons'>"+
                 "<button class='btn btn-primary' ng-click='goHome()'>Go Home</button>" +
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
          var y2 = window.joysticks[1].getY();
          
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