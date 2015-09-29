'use strict';

angular.module('REALM')
    .service('StorageService',function($http, $q, $timeout, $rootScope){

    	this.isAvailable= function(){
    		return (typeof(Storage) !== undefined && window.location.protocol !== 'file:')
    	}
});