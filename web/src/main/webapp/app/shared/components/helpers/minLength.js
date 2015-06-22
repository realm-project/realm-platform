'use strict';

angular.module('REALM').directive('minLength', function() {
  return {
    restrict: 'A', // only activate on element attribute
    require: '?ngModel', // get a hold of NgModelController
    link: function(scope, elem, attrs, ngModel) {
      if(!ngModel) return; // do nothing if no ng-model

      // watch own value and re-validate on change
      scope.$watch(attrs.ngModel, function() {
        validate();
      });

      var validate = function() {
        // set validity
        if (ngModel.$viewValue === undefined){
          ngModel.$setValidity('minLength',false);
        }else{
          ngModel.$setValidity('minLength', ngModel.$viewValue.length >= parseInt(attrs.minLength));
        }
      };
    }
  }
});