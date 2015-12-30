// File: AddBillController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 29th December 2015

(function () {
    'use strict';
 
    angular.module('billApp').controller('AddBillController', AddBillController);
    AddBillController.$inject = ['$location', '$scope', 'BillService', 'AuthenticationService', 'CompanyService', '$window', '$timeout'];
	
    function AddBillController($location, $scope, BillService, AuthenticationService, CompanyService, $window, $timeout) {
	   	var serv = {};
	   	serv.AuthenticationService = AuthenticationService;
	   	serv.CompanyService = CompanyService;
	   	serv.currentUser = AuthenticationService.GetUsername();
	   	serv.currentUserFirstName = AuthenticationService.GetUserFirstName();
	   	serv.addCompany = addCompany;
	   	serv.addBill = addBill;
	   	
        $scope.companyList = getCompanyList();
	    
	    angular.element(document).ready(function () {
	    	$timeout(function(){
	    		$('.selectpicker').selectpicker();
		    	$('.selectpicker').selectpicker("refresh");
	    	},50);
	    	
	    });

        return serv;
        
        function addBill(cl) {
        	console.log("ADD bill invoked!!"+cl);
        }
        
        function addCompany() {
        	CompanyService.addCompany(serv.company,function (response) {
        		console.log(response);
                if (response && response.code == 200) {
                    $scope.companyList = getCompanyList();
                    
                    $timeout(function(){
                    	console.log($scope.companyList);
                    	var id;
                    	var name;
                    	var finalid;
                    	$scope.companyList.forEach( function( item ) {
                    	    id = item.id;
                    	    name = item.name;
                    	    if (name == serv.company) {
                    	    	finalid = id;
                    	    }
                    	});
                    	console.log("final id = "+finalid);
        	    		$('.selectpicker').selectpicker();
        	    		$('.selectpicker').val(finalid);
        		    	$('.selectpicker').selectpicker("refresh");
        	    	},100);
                    $scope.addCompanyResult = response.message;
                    $scope.addCompanyStatus = "OK";
                } else {
                	$scope.addCompanyResult = response.message;
                    $scope.addCompanyStatus = "ERROR";
                }
            });
        }
        
        function getCompanyList() {
        	CompanyService.getCompanyList(function (response) {
                if (response) {
                    $scope.companyList = response.data;
                } else {
                	$scope.companyList = null;
                }
                return $scope.companyList;
            });
        }
        
	}
 
})();