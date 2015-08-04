'use strict';

angular.module('REALM').directive('uiSection', function() {
  return {
    restrict: 'E',
    replace: false, 
    scope: {
        section: '=',
        layoutOptions: '='  
    },
    template:   "<div class='ui-section__container'>" +
                    "<div class='ui-section__content'>" + 
                        "<ui-component ng-repeat='component in section.components' index='{{$index}}' component='component' section-options='section.sectionOptions' layout-options='layoutOptions'>"+
                        "</ui-component>" +
                    "</div>" +
                "</div>",
    compile: function CompilingFunction(tElement, tAttrs)
    {
      //can only manipulate DOM here (can't access scope yet)
      
      return function LinkingFunction(scope, element, attrs, ctrl) {
        /*var vScroll = new IScroll($(element).find('.ui-section__container').get(0), { scrollX: false, scrollY: true, mouseWheel: true, disableMouse:true, disablePointer:true, scrollbars:true, interactiveScrollbars:true, snap:true});
        window.vScrolls.push(vScroll);
        
        setTimeout(function(){
          vScroll.refresh();
        },50);*/

      }
    },
    controller: function SectionController($scope, $element, $attrs)
    {

        /*var vScroll = new IScroll($($element).find('.ui-section__container').get(0), { scrollX: false, scrollY: true, mouseWheel: true, disableMouse:true, disablePointer:true, scrollbars:true, interactiveScrollbars:true, snap:true});
        window.vScrolls.push(vScroll);

        this.refreshSection = function() {
           vScroll.refresh();
        }

        setTimeout(function(){that.refreshSection()}, 500);*/
    }    
  }
})