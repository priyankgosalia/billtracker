// File: CompaniesController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 12th December 2015

(function () {
    'use strict';
 
    angular.module('billApp').controller('HomeController', HomeController);
    HomeController.$inject = ['$location', '$scope', 'AuthenticationService','BillService','$rootScope', '$window', 'ngDialog'];
    function HomeController($location, $scope, AuthenticationService, BillService, $rootScope, $window, ngDialog) {
        var vm = {};
        vm.currentUser = AuthenticationService.GetUsername();
        vm.currentUserFirstName = AuthenticationService.GetUserFirstName();
        vm.logout = logout;
        vm.getRemindersList = getRemindersList;
        vm.AuthenticationService = AuthenticationService;
        vm.showViewBillDlg = showViewBillDlg;
        vm.showMarkPaidBillDlg = showMarkPaidBillDlg;
        vm.BillService = BillService;
        
        var columnDefs = [
	                      {headerName: "Bill #", field: "id", width: 65, filter: 'number', suppressSizeToFit:true },
	                      {headerName: "Company", field: "company", width: 170, filter: 'set'},
	                      {headerName: "Location", field: "location", width: 100, filter: 'set' },
	                      {headerName: "Type", field: "frequency", width: 90, filter: 'set' },
	                      {headerName: "Due Date", field: "dueDate", width: 100, filter: 'set'},
	                      {headerName: "Amount", field: "amount", width: 90, filter: 'number', cellRenderer: function(params) {
	                    	  var text = '&#8377; '+params.data.amount;
	                    	  return text;
	                      }},
	                      {headerName: "Actions", field: "id", width: 120, cellRenderer: function(params) {
	                    	  params.$scope.showViewBillDlg = vm.showViewBillDlg;
	                    	  var a = '<a ng-click="showViewBillDlg('+params.data.id+');">View Bill</a>';
	                    	  return a + '&nbsp;&nbsp;';
	                      }},
	                      {headerName: "Payment", field: "id", width: 100, cellRenderer: function(params) {
	                    	  params.$scope.showMarkPaidBillDlg = vm.showMarkPaidBillDlg;
	                    	  var a = '<a ng-click="showMarkPaidBillDlg('+params.data.id+');">Mark Paid</a>';
	                    	  return a;
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
                    var jsonArr = [];
                    for (var i = 0; i < response.data.length; i++) {
                        jsonArr.push({
                            id: response.data[i].bill.id,
                            company: response.data[i].bill.company, 
                            location: response.data[i].bill.location,
                            frequency: response.data[i].bill.frequency,
                            dueDate: response.data[i].bill.dueDate,
                            amount: response.data[i].bill.amount,
                        });
                    	console.log(response.data[i]);
                    }
                    $scope.gridOptions.api.setRowData(jsonArr);
                    $scope.billsList = jsonArr;
                } else {
                	$scope.billsList = null;
                }
            });
        }
	    
	    function showViewBillDlg(billId) {
        	$scope.billId = billId;
        	ngDialog.open({
        	    template: 'pages/viewBill.html',
        	    controller: 'ViewBillController',
        	    controllerAs: 'vbcm',
        	    closeByEscape:true,
        	    className: 'ngdialog-theme-default dialogwidth800',
        	    cache:false,
        	    scope:$scope
        	});
        }
	    
	    function showMarkPaidBillDlg(billId) {
        	$scope.billId = billId;
        	ngDialog.open({
        	    template: 'pages/markPaid.html',
        	    controller: 'MarkPaidBillController',
        	    controllerAs: 'pbcm',
        	    closeByEscape:true,
        	    className: 'ngdialog-theme-default dialogwidth800',
        	    cache:false,
        	    scope:$scope
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