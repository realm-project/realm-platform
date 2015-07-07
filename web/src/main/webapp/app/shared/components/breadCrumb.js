'use strict';
angular.module('REALM').directive('breadCrumb', function($rootScope, $compile, $state, AuthService){
	return{
		restrict: 'E',
		template: '<span></span>',
		controller: function($scope,$element,$attrs){
			$scope.beginState="";
			$scope.endState="";

			$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){ 
				if ($scope.beginState===fromState.name && $scope.endState===toState.name){
					return;
				}

				$scope.beginState = fromState.name;
				$scope.endState = toState.name;
				$scope.createElement(toState);
			});

			$scope.createElement = function(toState){
				$($element).empty();
				if (toState===""){
					return;
				}

				var breadCrumbElement ="";
				if (AuthService.isAuthenticated){
					var parentState = toState;
					while(parentState){
						breadCrumbElement = "<a class='breadCrumb-element' ui-sref='"+ parentState.name +"'> > "+ parentState.name+ "</a>"  + breadCrumbElement;
						// get parent of current parent
						parentState = $scope.getParent(parentState);
					}
				}else{
					// no navigation menu before login to the system
				}
				if (breadCrumbElement !==""){
					var compiledElement = $compile(breadCrumbElement)($scope);
					$($element).append(compiledElement);
				}
			};

			$scope.getParent = function(state){
				var tempPlace = state.name.lastIndexOf(".");
				if (tempPlace === -1){
					return null;
				}else{
					return $state.get(state.name.substring(0,tempPlace));
				}
			};
		}

	};
});