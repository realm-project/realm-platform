angular.module('ionic.service.view', ['ui.router', 'ionic.service.platform'])


.run(     ['$rootScope', '$state', '$location', '$document', '$animate', '$ionicPlatform', 
  function( $rootScope,   $state,   $location,   $document,   $animate,   $ionicPlatform) {

  // init the variables that keep track of the view history
  $rootScope.$viewHistory = {
    histories: { root: { historyId: 'root', parentHistoryId: null, stack: [], cursor: -1 } },
    backView: null,
    forwardView: null,
    currentView: null,
    disabledRegistrableTagNames: []
  };

  $rootScope.$on('viewState.changeHistory', function(e, data) {
    if(!data) return;

    var hist = (data.historyId ? $rootScope.$viewHistory.histories[ data.historyId ] : null );
    if(hist && hist.cursor > -1 && hist.cursor < hist.stack.length) {
      // the history they're going to already exists
      // go to it's last view in its stack
      var view = hist.stack[ hist.cursor ];
      return view.go(data);
    }

    // this history does not have a URL, but it does have a uiSref
    // figure out its URL from the uiSref
    if(!data.url && data.uiSref) {
      data.url = $state.href(data.uiSref);
    }
    
    if(data.url) {
      // don't let it start with a #, messes with $location.url()
      if(data.url.indexOf('#') === 0) {
        data.url = data.url.replace('#', '');
      }
      if(data.url !== $location.url()) {
        // we've got a good URL, ready GO!
        $location.url(data.url);
      }
    }
  });

  // Set the document title when a new view is shown
  $rootScope.$on('viewState.viewEnter', function(e, data) {
    if(data && data.title) {
      $document[0].title = data.title;
    }
  });

  // Triggered when devices with a hardware back button (Android) is clicked by the user
  // This is a Cordova/Phonegap platform specifc method
  function onHardwareBackButton(e) {
    if($rootScope.$viewHistory.backView) {
      // there is a back view, go to it
      $rootScope.$viewHistory.backView.go();
    } else {
      // there is no back view, so close the app instead
      ionic.Platform.exitApp();
    }
    e.preventDefault();
    return false;
  }
  $ionicPlatform.onHardwareBackButton(onHardwareBackButton);

}])

.factory('$ionicViewService', ['$rootScope', '$state', '$location', '$window', '$injector', 
                      function( $rootScope,   $state,   $location,   $window,   $injector) {
  var $animate = $injector.has('$animate') ? $injector.get('$animate') : false;

  var View = function(){};
  View.prototype.initialize = function(data) {
    if(data) {
      for(var name in data) this[name] = data[name];
      return this;
    }
    return null;
  };
  View.prototype.go = function(opts) {

    if(this.stateName) {
      return $state.go(this.stateName, this.stateParams);
    }

    if(this.url && this.url !== $location.url()) {

      if($rootScope.$viewHistory.backView === this) {
        return $window.history.go(-1);
      } else if($rootScope.$viewHistory.forwardView === this) {
        return $window.history.go(1);
      }

      $location.url(this.url);
      return;
    }

    return null;
  };
  View.prototype.destory = function() {
    if(this.scope) {
      this.scope.destory && this.scope.destory();
      this.scope = null;
    }
  };

  function createViewId(stateId) {
    return ('_' + stateId + '_' + Math.round(Math.random() * 99999999)).replace(/\./g, '_');
  }

  return {

    register: function(containerScope, element) {

      var viewHistory = $rootScope.$viewHistory,
          currentStateId = this.getCurrentStateId(),
          hist = this._getHistory(containerScope),
          currentView = viewHistory.currentView,
          backView = viewHistory.backView,
          forwardView = viewHistory.forwardView,
          rsp = {
            viewId: null,
            navAction: null,
            navDirection: null,
            historyId: hist.historyId
          };

      if(element && !this.isTagNameRegistrable(element)) {
        // first check to see if this element can even be registered as a view.
        // Certain tags are only containers for views, but are not views themselves.
        // For example, the <tabs> directive contains a <tab> and the <tab> is the
        // view, but the <tabs> directive itself should not be registered as a view.
        rsp.navAction = 'disabledByTagName';
        return rsp;
      }

      if(currentView && 
         currentView.stateId === currentStateId &&
         currentView.historyId === hist.historyId) {
        // do nothing if its the same stateId in the same history
        rsp.navAction = 'noChange';
        return rsp;
      }

      if(backView && backView.stateId === currentStateId) {
        // they went back one, set the old current view as a forward view
        rsp.viewId = backView.viewId;
        rsp.navAction = 'moveBack';
        if(backView.historyId === currentView.historyId) {
          // went back in the same history
          rsp.navDirection = 'back';
        }

      } else if(forwardView && forwardView.stateId === currentStateId) {
        // they went to the forward one, set the forward view to no longer a forward view
        rsp.viewId = forwardView.viewId;
        rsp.navAction = 'moveForward';
        if(forwardView.historyId === currentView.historyId) {
          rsp.navDirection = 'forward';
        }

        var parentHistory = this._getParentHistoryObj(containerScope);
        if(forwardView.historyId && parentHistory.scope) {
          // if a history has already been created by the forward view then make sure it stays the same
          parentHistory.scope.$historyId = forwardView.historyId;
          rsp.historyId = forwardView.historyId;
        }

      } else if(currentView && currentView.historyId !== hist.historyId && 
                hist.cursor > -1 && hist.stack.length > 0 && hist.cursor < hist.stack.length &&
                hist.stack[hist.cursor].stateId === currentStateId) {
        // they just changed to a different history and the history already has views in it
        rsp.viewId = hist.stack[hist.cursor].viewId;
        rsp.navAction = 'moveBack';

      } else {

        // set a new unique viewId
        rsp.viewId = createViewId(currentStateId);

        if(currentView) {
          // set the forward view if there is a current view (ie: if its not the first view)
          currentView.forwardViewId = rsp.viewId;

          // its only moving forward if its in the same history
          if(hist.historyId === currentView.historyId) {
            rsp.navDirection = 'forward';
          }
          rsp.navAction = 'newView';

          // check if there is a new forward view
          if(forwardView && currentView.stateId !== forwardView.stateId) {
            // they navigated to a new view but the stack already has a forward view
            // since its a new view remove any forwards that existed
            var forwardsHistory = this._getView(forwardView.historyId);
            if(forwardsHistory) {
              // the forward has a history
              for(var x=forwardsHistory.stack.length - 1; x >= forwardView.index; x--) {
                // starting from the end destory all forwards in this history from this point
                forwardsHistory.stack[x].destory();
                forwardsHistory.stack.splice(x);
              }
            }
          }

        } else {
          // there's no current view, so this must be the initial view
          rsp.navAction = 'initialView';
        }

        // add the new view to the stack
        viewHistory.histories[rsp.viewId] = this.createView({ 
          viewId: rsp.viewId,
          index: hist.stack.length,
          historyId: hist.historyId,
          backViewId: (currentView && currentView.viewId ? currentView.viewId : null),
          forwardViewId: null,
          stateId: currentStateId,
          stateName: this.getCurrentStateName(),
          stateParams: this.getCurrentStateParams(),
          url: $location.url()
        });

        // add the new view to this history's stack
        hist.stack.push(viewHistory.histories[rsp.viewId]);
      }

      this.setNavViews(rsp.viewId);

      hist.cursor = viewHistory.currentView.index;

      return rsp;
    },

    setNavViews: function(viewId) {
      var viewHistory = $rootScope.$viewHistory;

      viewHistory.currentView = this._getView(viewId);
      viewHistory.backView = this._getBackView(viewHistory.currentView);
      viewHistory.forwardView = this._getForwardView(viewHistory.currentView);

      $rootScope.$broadcast('$viewHistory.historyChange', {
        showBack: (viewHistory.backView && viewHistory.backView.historyId === viewHistory.currentView.historyId)
      });
    },

    registerHistory: function(scope) {
      scope.$historyId = 'h' + Math.round(Math.random() * 99999999999);
    },

    createView: function(data) {
      var newView = new View();
      return newView.initialize(data);
    },

    getCurrentView: function() {
      return $rootScope.$viewHistory.currentView;
    },

    getBackView: function() {
      return $rootScope.$viewHistory.backView;
    },

    getForwardView: function() {
      return $rootScope.$viewHistory.forwardView;
    },

    getNavDirection: function() {
      return $rootScope.$viewHistory.navDirection;
    },

    getCurrentStateName: function() {
      return ($state && $state.current ? $state.current.name : null);
    },

    isCurrentStateNavView: function(navView) {
      return ($state && 
              $state.current && 
              $state.current.views && 
              $state.current.views[navView] ? true : false);
    },

    getCurrentStateParams: function() {
      var rtn;
      if ($state && $state.params) {
        for(var key in $state.params) {
          if($state.params.hasOwnProperty(key)) {
            rtn = rtn || {};
            rtn[key] = $state.params[key];
          }
        }
      }
      return rtn;
    },

    getCurrentStateId: function() {
      var id;
      if($state && $state.current && $state.current.name) {
        id = $state.current.name;
        if($state.params) {
          for(var key in $state.params) {
            if($state.params.hasOwnProperty(key) && $state.params[key]) {
              id += "_" + key + "=" + $state.params[key];
            }
          }
        }
        return id;
      }
      // if something goes wrong make sure its got a unique stateId
      return 'r' + Math.round(Math.random() * 9999999);
    },

    _getView: function(viewId) {
      return (viewId ? $rootScope.$viewHistory.histories[ viewId ] : null );
    },

    _getBackView: function(view) {
      return (view ? this._getView(view.backViewId) : null );
    },

    _getForwardView: function(view) {
      return (view ? this._getView(view.forwardViewId) : null );
    },

    _getHistory: function(scope) {
      var histObj = this._getParentHistoryObj(scope);

      if( !$rootScope.$viewHistory.histories[ histObj.historyId ] ) {
        // this history object exists in parent scope, but doesn't
        // exist in the history data yet
        $rootScope.$viewHistory.histories[ histObj.historyId ] = { 
          historyId: histObj.historyId, 
          parentHistoryId: this._getParentHistoryObj(histObj.scope.$parent).historyId,
          stack: [],
          cursor: -1
        };
      }

      return $rootScope.$viewHistory.histories[ histObj.historyId ];
    },

    _getParentHistoryObj: function(scope) {
      var parentScope = scope;
      while(parentScope) {
        if(parentScope.hasOwnProperty('$historyId')) {
          // this parent scope has a historyId
          return { historyId: parentScope.$historyId, scope: parentScope };
        }
        // nothing found keep climbing up
        parentScope = parentScope.$parent;
      }
      // no history for for the parent, use the root
      return { historyId: 'root', scope: $rootScope };
    },

    getRenderer: function(navViewElement, navViewAttrs, navViewScope) {
      var service = this;
      var registerData;
      var doAnimation;

      // climb up the DOM and see which animation classname to use, if any
      var animationClass = null;
      var el = navViewElement[0];
      while(!animationClass && el) {
        animationClass = el.getAttribute('animation');
        el = el.parentElement;
      }
      el = null;

      function setAnimationClass() {
        // add the animation CSS class we're gonna use to transition between views
        navViewElement[0].classList.add(animationClass);

        if(registerData.navDirection === 'back') {
          // animate like we're moving backward
          navViewElement[0].classList.add('reverse');
        } else {
          // defaults to animate forward
          // make sure the reverse class isn't already added
          navViewElement[0].classList.remove('reverse');
        }
      }

      return function(shouldAnimate) {

        return {
          
          enter: function(element) {

            if(doAnimation && shouldAnimate) {
              // enter with an animation
              setAnimationClass();

              element.addClass('ng-enter');
              document.body.classList.add('disable-pointer-events');

              $animate.enter(element, navViewElement, null, function() {
                document.body.classList.remove('disable-pointer-events');
              }); 
              return;
            }

            // no animation
            navViewElement.append(element);
          },

          leave: function() {
            var element = navViewElement.contents();

            if(doAnimation && shouldAnimate) {
              // leave with an animation
              setAnimationClass();

              $animate.leave(element, function() { 
                element.remove(); 
              });
              return;
            }

            // no animation
            element.remove();
          },

          register: function(element) {
            // register a new view
            registerData = service.register(navViewScope, element);
            doAnimation = (animationClass !== null && registerData.navDirection !== null);
          }

        };
      };
    },

    disableRegisterByTagName: function(tagName) {
      // not every element should animate betwee transitions
      // For example, the <tabs> directive should not animate when it enters,
      // but instead the <tabs> directve would just show, and its children
      // <tab> directives would do the animating, but <tabs> itself is not a view
      $rootScope.$viewHistory.disabledRegistrableTagNames.push(tagName.toUpperCase());
    },

    isTagNameRegistrable: function(element) {
      // check if this element has a tagName (at its root, not recursively)
      // that shouldn't be animated, like <tabs> or <side-menu>
      var x, y, disabledTags = $rootScope.$viewHistory.disabledRegistrableTagNames;
      for(x=0; x<element.length; x++) {
        if(element[x].nodeType !== 1) continue;
        for(y=0; y<disabledTags.length; y++) {
          if(element[x].tagName === disabledTags[y]) {
            return false;
          }
        }
      }
      return true;
    },

    clearHistory: function() {
      var historyId, x, view,
      histories = $rootScope.$viewHistory.histories,
      currentView = $rootScope.$viewHistory.currentView;

      for(historyId in histories) {

        if(histories[historyId].stack) {
          histories[historyId].stack = [];
          histories[historyId].cursor = -1;
        }

        if(currentView.historyId === historyId) {
          currentView.backViewId = null;
          currentView.forwardViewId = null;
          histories[historyId].stack.push(currentView);
        } else if(histories[historyId].destroy) {
          histories[historyId].destroy();
        }

      }

      this.setNavViews(currentView.viewId);
    }

  };

}]);
