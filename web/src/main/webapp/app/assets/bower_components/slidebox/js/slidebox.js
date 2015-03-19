/**
* Slidebox Directive
*
* version 0.9
* http://github.com/keithjgrant/slidebox
*
* by Keith J Grant
* http://elucidblue.com
*/
angular.module('Slidebox', [])

.directive('slidebox', function slideboxDirective () {
    return {
        template: '<div class="slidebox-container" style="height:100%">' +
                    '<div class="slidebox" style="height:100%">' +
                      '<div ng-transclude class="slidebox-content" style="height:100%; margin-right:auto"></div>' +
                    '</div>' +
                    '<div class="slidebox-controls slidebox-left"></div>' +
                    '<div class="slidebox-controls slidebox-right"></div>' +
                  '</div>',
        replace: true,
        transclude: true,
        restrict: 'AE',
        scope: false,
        link: function (scope, element, attrs) {
            var content = element.children()[0],
                leftEl = element.children()[1],
                rightEl = element.children()[2],
                velocity = 0,
                defaultOpacity = Number(getComputedStyle(leftEl).opacity),
                maxVelocity = Number(attrs.speed) || 25,
                interval,
                didScroll = true; // trigger an initial check on load

            if (attrs.contentWidth) {
                scope.$watch(attrs.contentWidth, function (value) {
                    if (value == Number(value)) {
                        value += 'px';
                    }
                    content.children[0].style.width = value;
                    didScroll = true;
                });
            }
            if (attrs.contentClass) {
                content.children[0].className += ' ' + attrs.contentClass;
            }

            function startScroll (isLeft) {
                // set default velocity for touchdevices
                if (isLeft) {
                    velocity = maxVelocity / -2;
                } else {
                    velocity = maxVelocity / 2;
                }
                interval = setInterval(function () {
                    content.scrollLeft += velocity;
                    didScroll = true;
                }, 50);
            }

            function stopScroll (controlEl) {
                clearInterval(interval);
                controlEl.style.opacity = defaultOpacity;
                velocity = 0;
            }

            function touchStart (controlEl, isLeft) {
                startScroll();
                if (isLeft) {
                    velocity = maxVelocity / -2;
                } else {
                    velocity = maxVelocity / 2;
                }
                controlEl.style.opacity = 1.0;
            }

            /**
             * Get scalar between 0 and 1 based on mouse position in element
             */
            function getVelocityScalar (element, clientX) {
                var width = element.offsetWidth,
                    leftSide = element.getBoundingClientRect().left;
                return (clientX - leftSide) / width;
            };

            function updateVelocity (controlEl, xPos, isLeft) {
                var scale = getVelocityScalar(controlEl, xPos),
                    round,
                    opacityScalar;

                if (isLeft) {
                    // scale is % from the left side; convert to negative %
                    // from the right:
                    scale -= 1;

                    controlEl.style.opacity = defaultOpacity + scale * -1;
                    round = Math.floor;
                } else {
                    controlEl.style.opacity = defaultOpacity + scale;
                    round = Math.ceil;
                }

                velocity = round(scale * maxVelocity);
            }

            /**
             * Hide control when scrolled all the way to the end
             */
            function updateControlVisability () {
                if (!didScroll) {
                    return;
                }

                if (content.scrollLeft === 0) {
                    leftEl.style.display = 'none';
                } else {
                    leftEl.style.display = 'block';
                }

                if (content.scrollLeft === content.scrollWidth - content.offsetWidth) {
                    rightEl.style.display = 'none';
                } else {
                    rightEl.style.display = 'block';
                }
                didScroll = false;
            }

            leftEl.addEventListener('mouseover', function () {
                startScroll();
            }, false);
            leftEl.addEventListener('mousemove', function (event) {
                updateVelocity(leftEl, event.clientX, true);
            }, false);
            leftEl.addEventListener('mouseout', function () {
                stopScroll(leftEl);
            }, false);
            leftEl.addEventListener('touchstart', function (event) {
                event.preventDefault(); // cancel mouse event
                touchStart(leftEl, true);
            }, false);
            leftEl.addEventListener('touchend', function (event) {
                event.preventDefault(); // cancel mouse event
                stopScroll(leftEl);
            }, false);

            rightEl.addEventListener('mouseover', function () {
                startScroll();
            });
            rightEl.addEventListener('mousemove', function (event) {
                updateVelocity(rightEl, event.clientX, false);
            });
            rightEl.addEventListener('mouseout', function  () {
                stopScroll(rightEl);
            }, false);
            rightEl.addEventListener('touchstart', function (event) {
                event.preventDefault(); // cancel mouse event
                touchStart(rightEl, false);
            }, false);
            rightEl.addEventListener('touchend', function (event) {
                event.preventDefault(); // cancel mouse event
                stopScroll(rightEl);
            }, false);

            content.addEventListener('scroll', function () {
                didScroll = true;
            });
            setInterval(updateControlVisability, 250);
        }
    };
})

;