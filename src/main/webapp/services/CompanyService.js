(function () {
    'use strict';
 
    angular
        .module('billApp')
        .factory('CompanyService', CompanyService);
 
    CompanyService.$inject = ['$http','AuthenticationService'];
    function CompanyService($http,AuthenticationService) {
        var cmservice = this;
        cmservice.getCompanyList = getCompanyList;
        cmservice.addCompany = addCompany;
        return cmservice;
 
        function getCompanyList(callback) {
        	$http({
        		  method: 'GET',
        		  url: 'ws/company/getCompanyList'
        	}).then(function successCallback(response) {
				console.log("getCompanyList(Success) = "+response);
		        callback(response);
        	}, function errorCallback(response) {
        		console.log("getCompanyList(Error) = "+response);
        	});
        }
        
        function addCompany(companyName,callback) {
        	var datax = $.param({name:companyName});
        	$http({
        		  method: 'PUT',
        		  url: 'ws/company/add?'+datax
        	}).then(function successCallback(response) {
				console.log("addCompany(Success) = "+response.data);
		        callback(response.data);
        	}, function errorCallback(response) {
        		console.log("addCompany(Error) = "+response);
        	});
        }
    }
})();