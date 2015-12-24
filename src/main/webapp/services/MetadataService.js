(function () {
    'use strict';
 
    angular
        .module('billApp')
        .factory('MetadataService', MetadataService);
 
    MetadataService.$inject = ['$http'];
    function MetadataService($http) {
        var cmservice = this;
        cmservice.getAppInfo = getAppInfo;
        return cmservice;
 
        function getAppInfo(callback) {
        	$http({
        		  method: 'GET',
        		  url: 'ws/metadata/about'
        	}).then(function successCallback(response) {
				console.log("getAppInfo(Success) = "+response);
		        callback(response);
        	}, function errorCallback(response) {
        		console.log("getAppInfo(Error) = "+response);
        	});
        }
    }
})();