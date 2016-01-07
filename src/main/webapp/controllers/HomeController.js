// File: CompaniesController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 12th December 2015

(function () {
    'use strict';
 
    angular
        .module('billApp')
        .controller('HomeController', HomeController);
 
    HomeController.$inject = ['$location', '$scope', 'AuthenticationService','BillService','$rootScope', '$window'];
    function HomeController($location, $scope, AuthenticationService, BillService, $rootScope, $window) {
        var vm = this;
        vm.currentUser = AuthenticationService.GetUsername();
        vm.currentUserFirstName = AuthenticationService.GetUserFirstName();
        vm.logout = logout;
        vm.getRemindersList = getRemindersList;
        vm.AuthenticationService = AuthenticationService;
        vm.BillService = BillService;
        
        var columnDefs = [
	                      {headerName: "Bill #", width: 65, filter: 'number', suppressSizeToFit:true, cellRenderer: function(params) {
	                    	  return params.data.bill.id;
	                      }},
	                      {headerName: "Company", width: 170, filter: 'set', cellRenderer: function(params) {
	                    	  return params.data.bill.company;
	                      }},
	                      {headerName: "Location", width: 100, filter: 'set', cellRenderer: function(params) {
	                    	  return params.data.bill.location;
	                      }},
	                      {headerName: "Type", width: 90, filter: 'set', cellRenderer: function(params) {
	                    	  return params.data.bill.frequency;
	                      }},
	                      {headerName: "Due Date", width: 100, filter: 'set', cellRenderer: function(params) {
	                    	  var d = new Date(params.data.bill.dueDate);
	                    	  return "X="+d;
	                      }},
	                      {headerName: "Amount", width: 90, filter: 'set', cellRenderer: function(params) {
	                    	  var text = '&#8377; '+params.data.bill.amount;
	                    	  return text;
	                      }, cellStyle: function(params) {
	                          if (params.value == "Paid") {
	                              return {'color': 'darkgreen'};
	                          } else {
	                        	  return {'color': 'darkred'};
	                          }
	                      }},
	                      {headerName: "Actions", field: "id", width: 120, cellRenderer: function(params) {
	                    	  var a = '<a ng-click="bm.showViewBillDialog('+params.data.id+');">View Bill</a>';
	                    	  if (AuthenticationService.isAdmin() == "true") {
	                    		  var b = '<a ng-click="bm.showEditBillDialog('+params.data.id+');">Edit</a>';
	                    		  var c = '<a ng-click="bm.showDeleteBillDialog('+params.data.id+');">Delete</a>';
	                    	  } else {
	                    		  var b = '';
	                    		  var c = '';
	                    	  }
	                    	  return a+'&nbsp;&nbsp;'+b+'&nbsp;&nbsp;'+c;
	                      }}
	                  ];
	    $scope.gridOptions = {
	            columnDefs: columnDefs,
	            rowData: null,
	            enableFilter: true,
	            enableSorting: true,
	            rowHeight: 23,
	            rowSelection: 'single',
	            enableColResize: true,
	            angularCompileRows:true
	    };
	    
	    getRemindersList();
	    
	    angular.element(document).ready(function () {
	    	$scope.gridOptions.api.sizeColumnsToFit();
	    	$scope.gridOptions.api.onFilterChanged();
	    });
	    
	    return vm;
	    
	    function getRemindersList() {
        	BillService.getAllReminders(function (response) {
                if (response) {
                    $scope.billsList = response.data;
                    $scope.gridOptions.api.setRowData($scope.billsList);
                } else {
                	$scope.billsList = null;
                }
            });
        }
	    
        function logout() {
        	if ($window.confirm('Are you sure you want to Logout?')) {
        		AuthenticationService.ClearCredentials();
            	$location.path('/#/login');
        	}
        }
    }
 
})();