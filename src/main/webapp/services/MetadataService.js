(function () {
    'use strict';
 
    angular
        .module('billApp')
        .factory('MetadataService', MetadataService);
 
    MetadataService.$inject = ['$http'];
    function MetadataService($http) {
        var cmservice = this;
        cmservice.getAppInfo = getAppInfo;
        cmservice.getBillFreqList = getBillFreqList;
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
        
        function getBillFreqList(callback) {
        	$http({
        		  method: 'GET',
        		  url: 'ws/metadata/billType'
        	}).then(function successCallback(response) {
				console.log("getBillFreqList(Success) = "+response);
		        callback(response);
        	}, function errorCallback(response) {
        		console.log("getBillFreqList(Error) = "+response);
        	});
        }
    }
})();