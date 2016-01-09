// File: BillsController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 21st December 2015

(function () {
    'use strict';
 
    angular.module('billApp').controller('BillsController', BillsController);
    BillsController.$inject = ['$location', '$scope', 'BillService', 'AuthenticationService', 'CompanyService', '$window', 'ngDialog'];
	
    function BillsController($location, $scope, BillService, AuthenticationService, CompanyService, $window, ngDialog) {
	   	var serv = {};
	   	serv.getBillsList = getBillsList;
	   	serv.showAddBillDialog = showAddBillDialog;
	   	serv.showViewBillDialog = showViewBillDialog;
	   	serv.showEditBillDialog = showEditBillDialog;
	   	serv.showDeleteBillDialog = showDeleteBillDialog;
	   	serv.logout = logout;
	   	serv.AuthenticationService = AuthenticationService;
	   	serv.CompanyService = CompanyService;
	   	serv.currentUser = AuthenticationService.GetUsername();
	   	serv.currentUserFirstName = AuthenticationService.GetUserFirstName();
        $scope.companyList = getCompanyList();
        serv.isAdmin = AuthenticationService.isAdmin();
        $scope.isAdmin = serv.isAdmin;
	    
	    var columnDefs = [
	                      {headerName: "Bill #", field: "id", width: 65, filter: 'number', suppressSizeToFit:true},
	                      {headerName: "Company", field: "company", width: 170, filter: 'set'},
	                      {headerName: "Location", field: "location", width: 100, filter: 'set'},
	                      {headerName: "Type", field: "frequency", width: 90, filter: 'set'},
	                      {headerName: "Recur.", field: "recurring", width: 80, filter: 'set',cellRenderer: function(params) {
	                    	  if (params.data.recurring == true) {
	                    		  return "Y";
	                    	  } else {
	                    		  return "N";
	                    	  }
	                      }},
	                      {headerName: "Due Date", field: "dueDate", width: 100, filter: 'set', cellRenderer: function(params) {
	                    	  var date = new Date(params.data.dueDate);
	                    	  var text = date.getDate() + '-' + (date.getMonth()+1) + '-' + date.getFullYear();
	                    	  return text;
	                      }},
	                      {headerName: "Owner", field: "user", width: 80, filter: 'set'},
	                      {headerName: "Status", field: "status", width: 80, filter: 'set', suppressSizeToFit:true, cellStyle: function(params) {
	                          if (params.value == "Paid") {
	                              return {'color': 'darkgreen'};
	                          } else {
	                        	  return {'color': 'darkred'};
	                          }
	                      }},
	                      {headerName: "Amount", field: "amount", width: 90, filter: 'set', cellRenderer: function(params) {
	                    	  var text = '&#8377; '+params.data.amount;
	                    	  return text;
	                      }, cellStyle: function(params) {
	                          if (params.value == "Paid") {
	                              return {'color': 'darkgreen'};
	                          } else {
	                        	  return {'color': 'darkred'};
	                          }
	                      }},
	                      {headerName: "Actions", field: "id", width: 120, cellRenderer: function(params) {
	                    	  var a = '<a ng-click="bm.showViewBillDialog('+params.data.id+');">View</a>';
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
	            isExternalFilterPresent: isExternalFilterPresent,
	            doesExternalFilterPass: doesExternalFilterPass,
	            angularCompileRows:true
	    };
	    
	    function isExternalFilterPresent() {
	        return $scope.billStatus != 'All';
	    }
	    
	    function doesExternalFilterPass(node) {
	        switch ($scope.billStatus) {
	            case 'Paid': return node.data.status == "Paid";
	            case 'Unpaid': return node.data.status == "Unpaid";
	            case 'DueThisMonth': 
	            	var currentDate = new Date();
	            	var dueDate = node.data.dueDate;
	            	var pattern = currentDate.getFullYear() + "-" + (currentDate.getMonth() + 1);
	            	return node.data.status == "Unpaid" && dueDate.indexOf(pattern)!=-1;
	            case 'All': return true;
	            default: return true;
	        }
	    }
	    
	    $scope.externalFilterChanged = function () {
	        // inform the grid that it needs to filter the data
	        $scope.gridOptions.api.onFilterChanged();
	    };
	    
	    getBillsList();
	    
	    angular.element(document).ready(function () {
	    	$scope.gridOptions.api.sizeColumnsToFit();
	    	$scope.billStatus = "Unpaid";
	    	$scope.gridOptions.api.onFilterChanged();
	    });

        return serv;
        
        function showAddBillDialog() {
        	var addDlg = ngDialog.open({
        	    template: 'pages/addBill.html',
        	    controller: 'AddBillController',
        	    controllerAs: 'abcm',
        	    closeByEscape:true,
        	    className: 'ngdialog-theme-default dialogwidth800',
        	    cache:false,
        	    scope:$scope
        	});
	        addDlg.closePromise.then(function (data) {
	        	getBillsList(); 
	        });
        }
        
        function showViewBillDialog(billId) {
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
        
        function showDeleteBillDialog(billId) {
        	$scope.billId = billId;
        	var delDlg = ngDialog.open({
        	    template: 'pages/deleteBill.html',
        	    controller: 'DeleteBillController',
        	    controllerAs: 'dbcm',
        	    closeByEscape:true,
        	    className: 'ngdialog-theme-default dialogwidth800',
        	    cache:false,
        	    scope:$scope
        	});
        	delDlg.closePromise.then(function (data) {
	        	getBillsList(); 
	        });
        }
        
        function showEditBillDialog(billId) {
        	$scope.billId = billId;
        	var editDlg = ngDialog.open({
        	    template: 'pages/editBill.html',
        	    controller: 'EditBillController',
        	    controllerAs: 'ebcm',
        	    closeByEscape:true,
        	    className: 'ngdialog-theme-default dialogwidth800',
        	    cache:false,
        	    scope:$scope
        	});
        	editDlg.closePromise.then(function (data) {
        		getBillsList(); 
        	});
        }
        
        function getBillsList() {
        	BillService.getAllBills(function (response) {
                if (response) {
                    $scope.billsList = response.data;
                    $scope.gridOptions.api.setRowData($scope.billsList);
                } else {
                	$scope.billsList = null;
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
        
        function logout() {
        	if ($window.confirm('Are you sure you want to Logout?')) {
        		AuthenticationService.ClearCredentials();
            	$location.path('/#/login');
        	}
        }
	}
 
})();