'use strict'

app.directive('numericInput', function () {
    return    {
        restrict: 'E',
        replace: true,

        scope: {
            min: '=',
            max: '=',
            step: '=',
            value: '='
        },
        template:
                    '<input class="numeric-input__spinner" ng-model="inputValue">',

        controller: function ControllingFunction($scope, $element, $attrs, $rootScope) {
            $scope.inputValue = $scope.value;
            //watching the value of the numeric input
            $scope.$watch('value',function(newVal)
             {
                 //console.log("value changed to:"+newVal);
                 $scope.inputValue=newVal;
             });
        },

        compile: function CompilingFunction(element, attrs) {


            return function LinkingFunction(scope, element, attrs, ctrl) {

               $(element).spinner({
                   //spinner options
                    min: scope.min,
                    max: scope.max,
                    step: scope.step,
                    icons:
                    { up: "ui-icon-plus",
                      down: "ui-icon-minus"
                    },
                    //spinner event regiser
                    spin: function (event, ui) {
                        scope.inputValue = ui.value;
                    },
                    change: function (event, ui) {
                        //scope.inputValue = ui.value;
                    }

                });

                $(element).bind("focusout", function (event, ui) {

                    scope.$apply();
                    $(element).removeClass("numeric-input__invalid");
                    if (!$.isNumeric(scope.inputValue)) {
                        scope.inputValue = 0;
                    }
                    if (scope.inputValue > scope.max) {
                        scope.inputValue = scope.max;
                    }
                    if (scope.inputValue < scope.min) {
                        scope.inputValue = scope.min;
                    }
                    scope.value = parseFloat(scope.inputValue);

                    //to stop spinner when the focus in out before the keyup
                    $(element).trigger( "keyup" );

            });

                $(element).on("spinchange", function (event, ui) {
                    scope.$apply();
                    scope.value = scope.inputValue;
                }),//spinstop

                    $(element).on("spinstop", function (event, ui) {

                        scope.$apply();
                        //alert("changed")
                        $(element).removeClass("numeric-input__invalid");
                        if (!$.isNumeric(scope.inputValue)) {
                            $(element).addClass("numeric-input__invalid");
                            //console.log($(element).attr("class"));
                        }
                        if (scope.inputValue > scope.max) {
                            $(element).addClass("numeric-input__invalid");
                        }
                        if (scope.inputValue < scope.min) {
                            $(element).addClass("numeric-input__invalid");
                        }

                    }),

                    $(element).on('input', function() {
                        scope.$apply();
                        $(element).removeClass("numeric-input__invalid");
                        if (!$.isNumeric(scope.inputValue)) {
                            $(element).addClass("numeric-input__invalid");
                            //console.log($(element).attr("class"));
                        }
                        if (scope.inputValue > scope.max) {
                            $(element).addClass("numeric-input__invalid");
                        }
                        if (scope.inputValue < scope.min) {
                            $(element).addClass("numeric-input__invalid");
                        }
                       // scope.value = parseFloat(scope.inputValue);
                    });
            }
        }
    }
});
