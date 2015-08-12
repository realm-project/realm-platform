'use strict';
angular.module('REALM').directive('leftSideNavigator', function($rootScope, $compile){
	return{
		restrict: 'E',
		template: '<span></span>',
		controller: function($scope,$element,$attrs){
			$scope.$watch( function () { return localStorage.currentRole; }, function(){
				var navigatorElement ="";
				if (localStorage.currentRole =="student"){
					navigatorElement = '<a class="list-group-item" ui-sref="studentHome"><i class="left-sidebar-icon fa fa-home"></i>Home<i class="fa fa-chevron-right pull-right"></i></a>' + '<a class="list-group-item" ng-click="logoutFunction()"><i class="left-sidebar-icon fa fa-sign-out"></i>Log Out<i class="fa fa-chevron-right pull-right"></i></a>';
				}else if (localStorage.currentRole== "teacher" || localStorage.currentRole=="admin"){
					navigatorElement = '<a class="list-group-item" ui-sref="teacherHome"><i class="left-sidebar-icon fa fa-home"> Home</i></a>' + '<a class="list-group-item" ng-click="logoutFunction()"><i class="left-sidebar-icon fa fa-sign-out"> Sign out</i></a>';
				}else{
					// not loged in
					navigatorElement = '<a class="list-group-item" ui-sref="login">Login<i class="left-sidebar-icon fa fa-chevron-right pull-right"></i></a>' + '<a class="list-group-item" ui-sref="signup">Sign Up <i class="left-sidebar-icon fa fa-chevron-right pull-right"></i></a>';
				}
				var compiledElement = $compile(navigatorElement)($scope);
					$($element).empty().append(compiledElement);
			});
				
		}
	};
});