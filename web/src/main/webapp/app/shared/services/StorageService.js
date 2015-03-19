'use strict';

angular.module('REALM')
    .service('StorageService',function($http, $q, $cookies, $timeout, $rootScope, AUTH_EVENTS){

    	this.isAvailable= function(){
    		return (typeof(Storage) !== undefined && window.location.protocol !== 'file:')
    	}
});