// File: BillsController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 21st December 2015

(function () {
    'use strict';
 
    angular.module('billApp').controller('BillsController', BillsController);
    BillsController.$inject = ['$location', '$scope', 'CompanyService', 'AuthenticationService'];
	
    function BillsController($location, $scope, CompanyService, AuthenticationService) {
	   	var cmp = {};
	   	cmp.getCompanyList = getCompanyList;
	   	cmp.addCompany = addCompany;
	   	cmp.logout = logout;
	   	cmp.AuthenticationService = AuthenticationService;
	   	cmp.currentUser = AuthenticationService.GetUsername();
        cmp.currentUserFirstName = AuthenticationService.GetUserFirstName();
	   	getCompanyList();
	   	
	    $scope.sortType     = 'name'; // set the default sort type
	    $scope.sortReverse  = false;  // set the default sort order
	    $scope.searchFish   = '';     // set the default search/filter term
	    
        return cmp;
        
        function getCompanyList() {
        	CompanyService.getCompanyList(function (response) {
                if (response) {
                    $scope.companyList = response.data;
                } else {
                	$scope.companyList = null;
                }
            });
        }
        
        function addCompany() {
        	CompanyService.addCompany(cmp.company,function (response) {
        		console.log(response);
                if (response && response.code == 200) {
                    $scope.companyList = getCompanyList();
                    $scope.addCompanyResult = response.message;
                    $scope.addCompanyStatus = "OK";
                } else {
                	$scope.addCompanyResult = response.message;
                    $scope.addCompanyStatus = "ERROR";
                }
            });
        }
        
        function logout() {
        	AuthenticationService.ClearCredentials();
        	$location.path('/#/login');
        }
	}
 
})();