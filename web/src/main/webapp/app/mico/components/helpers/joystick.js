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
        
        scope.$on('GAMEPAD-MOVED', function(event,x,y){
          parentCtrl.moveJoystick(joystick, containerElement, x, y);
          
          
        });
        
        scope.$on('JOYSTICK-MOVED', function(event){

          //console.log("****************************");
          var x = window.joysticks[0].getX();
          var x2= window.joysticks[0].deltaX();
          var y = window.joysticks[0].getY();
          var y2= window.joysticks[0].deltaY();
          var z = window.joysticks[1].getY();
          var z2= window.joysticks[1].deltaY();
         //console.log("joystick.js: "+'x: ' +  x +  " y: " +  y + " z: " + z);
         //console.log("---(delta): "+'x2: ' +  x2 +  " y2: " +  y2 + " z2: " + z2);
        // console.log(window.joysticks[0]);
          
          parentCtrl.moveRobot(x, y, z);
          
        })
        
        var isZeroed = false;
        function runAnimation()
        {
            window.requestAnimationFrame(runAnimation);

            var gamepads = navigator.webkitGetGamepads();

            var pad = gamepads[scope.gamepad - 1];
            
            if(joystick._pressed)
            {
              var x = joystick.getX();
              var y = joystick.getY();
              
              if((Math.abs(x) > stickMovedThreshold || Math.abs(y) > stickMovedThreshold))
              {
                isZeroed = false;
                scope.$broadcast('JOYSTICK-MOVED');
              }
            }
            else if(!isZeroed)
            {
              isZeroed = true;
              scope.$broadcast('GAMEPAD-MOVED',0,0); 
            }
            if(pad !== undefined)
            {

              var xAxisIndex = 2*(scope.stick - 1);
              var yAxisIndex = 2*(scope.stick - 1) + 1;

              var x = pad.axes[xAxisIndex];
              var y = pad.axes[yAxisIndex];

              if((Math.abs(x) > stickMovedThreshold || Math.abs(y) > stickMovedThreshold) && joystick._pressed === false)
              {
                isZeroed = false;
                scope.$broadcast('GAMEPAD-MOVED',x,y);
              }
              else if(!isZeroed)
              {
                isZeroed = true;
                scope.$broadcast('GAMEPAD-MOVED',0,0);
              }

            }
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