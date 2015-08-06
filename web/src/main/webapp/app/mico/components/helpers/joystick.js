'use strict';

angular.module('REALM').directive('joystick', function() {
  return {
    restrict: 'E',
    replace: false,
    require:'^joystickInputComponent',
    scope: {
        gamepad:'=',
        stick:'=',
        xenabled:'=',
        yenabled:'='
    },
    template: '<div class="joystick__container">' +
              '</div>', 
    compile: function CompilingFunction(tElement, tAttrs)
    {
      //can only manipulate DOM here (can't access scope yet)
      
      return function LinkingFunction(scope, element, attrs, parentCtrl) {



        var containerElement = $(element).find('.joystick__container');
        
        var joystick = new VirtualJoystick({
          container: containerElement[0],
          mouseSupport:true,
          stationaryBase:true,
          baseX:100,
          baseY:100,
          limitStickTravel:true,
          stickRadius:50,
          allowX: scope.xenabled,
          allowY: scope.yenabled,
          strokeStyle:"#007aff"

        });

        window.joysticks.push(joystick);

        var stickMovedThreshold = 0.2;
       /* var stickStillThreshold = 0.4;

        var returnFlag = false;*/

         scope.$on('LEFT-SCOPE',function()
          {
              joystick._onUp();
          });
        
        scope.$on('GAMEPAD-MOVED', function(event,padElement, isZeroCommand, x, y){

          parentCtrl.moveJoystick(joystick, containerElement, isZeroCommand, x, y ,padElement);

        });
        
        scope.$on('JOYSTICK-MOVED', function(event , isZeroCommand){

          if (isZeroCommand){
            parentCtrl.moveRobot(0, 0, 0, 0, false, false, false, false);

          }else{
            var axis0 = 0;
            var axis1 = 0;
            var axis2 = 0;
            var axis3 = 0;
            if (scope.stick ==1){
              axis0 = window.joysticks[0].getX();
              axis1 = window.joysticks[0].getY();
            }
            else if (scope.stick ==2){
              axis2 = window.joysticks[1].getX();
              axis3 = window.joysticks[1].getY();
            }

            parentCtrl.moveRobot(axis0, axis1, axis2, axis3, false, false, false, false);
          }
        })
        
        var isZeroed = false;
        function runAnimation()
        {

          var gamepads;
          var pad;

           //to make sure if there is any gamepad is supported
          if (!!navigator.getGamepads){
            gamepads = navigator.getGamepads();
            pad = gamepads[scope.gamepad - 1];
          }
          
          if(joystick._pressed)
          {
            var x = joystick.getX();
            var y = joystick.getY();
            
            if((Math.abs(x) > stickMovedThreshold || Math.abs(y) > stickMovedThreshold))
            {
              isZeroed = false;
              scope.$broadcast('JOYSTICK-MOVED' , false);
            }
          }
          else if(!isZeroed)
          {
            isZeroed = true;
            scope.$broadcast('JOYSTICK-MOVED', true); 
          }

          
          if(pad !== undefined)
          {

            var xAxisIndex = 2*(scope.stick - 1);
            var yAxisIndex = 2*(scope.stick - 1) + 1;
            var x = pad.axes[xAxisIndex];
            var y = pad.axes[yAxisIndex];    

            if((Math.abs(x) > stickMovedThreshold || Math.abs(y) > stickMovedThreshold ) && joystick._pressed === false)
            {
              isZeroed = false;
              scope.$broadcast('GAMEPAD-MOVED', pad, false, x, y);
            }
            else if(!isZeroed)
            {
              isZeroed = true;
              scope.$broadcast('GAMEPAD-MOVED', pad ,true , 0 , 0);
            }
          
          }
          window.requestAnimationFrame(runAnimation);
        } 

        window.requestAnimationFrame(runAnimation);
      }
    },
    controller: function ControllerFunction($scope, $element, $attrs)
    {
        //console.log($($element).find('.joystick__container')[0]);
        


        
        

    }
  }
})