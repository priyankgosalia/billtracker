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
	   	serv.getDeletedBillsList = getDeletedBillsList;
	   	serv.showAddBillDialog = showAddBillDialog;
	   	serv.showViewBillDialog = showViewBillDialog;
	   	serv.showEditBillDialog = showEditBillDialog;
	   	serv.showDeleteBillDialog = showDeleteBillDialog;
	   	serv.showMarkPaidBillDlg = showMarkPaidBillDlg;
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
	                      {headerName: "Company", field: "company", width: 150, filter: 'set'},
	                      {headerName: "Location", field: "location", width: 100, filter: 'set'},
	                      {headerName: "Type", field: "frequency", width: 80, filter: 'set'},
	                      {headerName: "Rec.", field: "recurring", width: 60, filter: 'set',cellRenderer: function(params) {
	                    	  if (params.data.recurring == true) {
	                    		  return "Y";
	                    	  } else {
	                    		  return "N";
	                    	  }
	                      }},
	                      {headerName: "Due Date", field: "dueDate", width: 90, filter: 'set', cellRenderer: function(params) {
	                    	  var date = new Date(params.data.dueDate);
	                    	  var text = date.getDate() + '-' + (date.getMonth()+1) + '-' + date.getFullYear();
	                    	  return text;
	                      }},
	                      {headerName: "Owner", field: "user", width: 75, filter: 'set'},
	                      {headerName: "Status", field: "status", width: 75, filter: 'set', suppressSizeToFit:true, cellStyle: function(params) {
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
	                      {headerName: "Actions", field: "id", width: 150, cellRenderer: function(params) {
	                    	  var a = '<a ng-click="bm.showViewBillDialog('+params.data.id+');">View</a>';
	                    	  if (AuthenticationService.isAdmin() == "true") {
	                    		  if ($scope.deletedBillsRadioActive==false) {
		                    		  var b = '<a ng-click="bm.showEditBillDialog('+params.data.id+');">Edit</a>';
		                    		  var c = '<a ng-click="bm.showDeleteBillDialog('+params.data.id+');">Delete</a>';
		                    		  var d = '<a ng-click="bm.showMarkPaidBillDlg('+params.data.id+');"><b>Mark Paid</b></a>';
	                    		  } else {
	                    			  var b = '';
		                    		  var c = '';
		                    		  var d = '';
	                    		  }
	                    	  } else {
	                    		  var b = '';
	                    		  var c = '';
	                    		  var d = '';
	                    	  }
	                    	  return a+'&nbsp;&nbsp;'+b+'&nbsp;&nbsp;'+c+'&nbsp;&nbsp;'+d;
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
	            case 'Deleted':
	            	return (node.data.deleted == true);
	            case 'DueThisMonth': 
	            	var currentDate = new Date();
	            	var dueDate = node.data.dueDate
	            	var dueDateDate = new Date(dueDate); 
	            	//console.log(node.data);
	            	console.log((dueDateDate.getMonth()+1)+","+(currentDate.getMonth()+1));
	            	console.log((dueDateDate.getFullYear())+","+(currentDate.getFullYear()));
	            	var val1 = (dueDateDate.getMonth() == currentDate.getMonth() && dueDateDate.getFullYear() == currentDate.getFullYear());
	            	var val2 = node.data.status;
	            	var val3 = node.data.deleted;
	            	if (val1 == true && val2 == "Unpaid" && val3 == false) {
		            	return true;
	        		} else {
	        			return false;
	        		}
	            case 'All': return true;
	            default: return true;
	        }
	    }
	    
	    $scope.deletedBillsClicked = function () {
	    	$scope.deletedBillsRadioActive = true;
	    	getDeletedBillsListWithCallback(function(response){
	    		$scope.gridOptions.api.setRowData($scope.deletedBillsList);
		    });
	    };
	    
	    $scope.anyOtherBillClicked = function () {
	    	$scope.deletedBillsRadioActive = false;
	    	getBillsListWithCallback(function(response){
		    	$scope.gridOptions.api.setRowData($scope.billsList);
		    });
	    }
	    
	    $scope.externalFilterChanged = function () {
	        // inform the grid that it needs to filter the data
	        $scope.gridOptions.api.onFilterChanged();
	    };
	    
	    getBillsListWithCallback(function(response){
	    	$scope.gridOptions.api.setRowData($scope.billsList);
	    });
	    
	    $scope.deletedBillsRadioActive=false;
	    
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
        	    closeByDocument:false,
        	    className: 'ngdialog-theme-default dialogwidth800',
        	    cache:false,
        	    scope:$scope
        	});
	        addDlg.closePromise.then(function (data) {
	        	getBillsListWithCallback(function(response){
	    	    	$scope.gridOptions.api.setRowData($scope.billsList);
	    	    });
	        });
        }
        
        function showMarkPaidBillDlg(billId) {
        	$scope.billId = billId;
        	var dlg = ngDialog.open({
        	    template: 'pages/markPaid.html',
        	    controller: 'MarkPaidBillController',
        	    controllerAs: 'pbcm',
        	    closeByEscape:true,
        	    closeByDocument:false,
        	    className: 'ngdialog-theme-default dialogwidth800',
        	    cache:false,
        	    scope:$scope
        	});
        	dlg.closePromise.then(function (data) {
        		getBillsListWithCallback(function(response){
        	    	$scope.gridOptions.api.setRowData($scope.billsList);
        	    });
	        });
        }
        
        function showViewBillDialog(billId) {
        	$scope.billId = billId;
        	ngDialog.open({
        	    template: 'pages/viewBill.html',
        	    controller: 'ViewBillController',
        	    controllerAs: 'vbcm',
        	    closeByEscape:true,
        	    closeByDocument:false,
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
        	    closeByDocument:false,
        	    className: 'ngdialog-theme-default dialogwidth800',
        	    cache:false,
        	    scope:$scope
        	});
        	delDlg.closePromise.then(function (data) {
        		getBillsListWithCallback(function(response){
        	    	$scope.gridOptions.api.setRowData($scope.billsList);
        	    }); 
	        });
        }
        
        function showEditBillDialog(billId) {
        	$scope.billId = billId;
        	var editDlg = ngDialog.open({
        	    template: 'pages/editBill.html',
        	    controller: 'EditBillController',
        	    controllerAs: 'ebcm',
        	    closeByEscape:true,
        	    closeByDocument:false,
        	    className: 'ngdialog-theme-default dialogwidth800',
        	    cache:false,
        	    scope:$scope
        	});
        	editDlg.closePromise.then(function (data) {
        		getBillsListWithCallback(function(response){
        	    	$scope.gridOptions.api.setRowData($scope.billsList);
        	    });
        	});
        }
        
        function getBillsList() {
        	BillService.getAllBills(function (response) {
                if (response) {
                    $scope.billsList = response.data;
                    //$scope.gridOptions.api.setRowData($scope.billsList);
                } else {
                	$scope.billsList = null;
                }
            });
        }
        
        function getBillsListWithCallback(cb) {
        	BillService.getAllBills(function (response) {
                if (response) {
                    $scope.billsList = response.data;
                    //$scope.gridOptions.api.setRowData($scope.billsList);
                } else {
                	$scope.billsList = null;
                }
                cb($scope.billsList);
            });
        }
        
        function getDeletedBillsList() {
        	BillService.getAllDeletedBills(function (response) {
                if (response) {
                    $scope.deletedBillsList = response.data;
                    //$scope.gridOptions.api.setRowData($scope.deletedBillsList);
                } else {
                	$scope.deletedBillsList = null;
                }
            });
        }
        
        function getDeletedBillsListWithCallback(cb) {
        	BillService.getAllDeletedBills(function (response) {
                if (response) {
                    $scope.deletedBillsList = response.data;
                    //$scope.gridOptions.api.setRowData($scope.deletedBillsList);
                } else {
                	$scope.deletedBillsList = null;
                }
                cb($scope.deletedBillsList);
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