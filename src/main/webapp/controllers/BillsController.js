// File: BillsController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 21st December 2015

(function () {
    'use strict';
 
    angular.module('billApp').controller('BillsController', BillsController);
    BillsController.$inject = ['$location', '$scope', 'CompanyService', 'AuthenticationService', '$window', 'ngDialog'];
	
    function BillsController($location, $scope, CompanyService, AuthenticationService, $window, ngDialog) {
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
        	console.log("foo");
        	ngDialog.open({ template: 'pages/about.html' });
        }
        
        function logout() {
        	if ($window.confirm('Are you sure you want to Logout?')) {
        		AuthenticationService.ClearCredentials();
            	$location.path('/#/login');
        	}
        }
	}
 
})();