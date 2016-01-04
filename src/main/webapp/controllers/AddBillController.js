// File: AddBillController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 29th December 2015

(function () {
    'use strict';
 
    angular.module('billApp').controller('AddBillController', AddBillController);
    // the following is an input tag directive used to allow only numbers in a textbox
    angular.module('billApp').directive('validNumber', function() {
        return {
            require: '?ngModel',
            link: function(scope, element, attrs, ngModelCtrl) {
              if(!ngModelCtrl) {
                return; 
              }

              ngModelCtrl.$parsers.push(function(val) {
                if (angular.isUndefined(val)) {
                    var val = '';
                }
                
                var clean = val.replace(/[^-0-9\.]/g, '');
                var negativeCheck = clean.split('-');
    			var decimalCheck = clean.split('.');
                if(!angular.isUndefined(negativeCheck[1])) {
                    negativeCheck[1] = negativeCheck[1].slice(0, negativeCheck[1].length);
                    clean =negativeCheck[0] + '-' + negativeCheck[1];
                    if(negativeCheck[0].length > 0) {
                    	clean =negativeCheck[0];
                    }
                    
                }
                  
                if(!angular.isUndefined(decimalCheck[1])) {
                    decimalCheck[1] = decimalCheck[1].slice(0,2);
                    clean =decimalCheck[0] + '.' + decimalCheck[1];
                }

                if (val !== clean) {
                  ngModelCtrl.$setViewValue(clean);
                  ngModelCtrl.$render();
                }
                return clean;
              });

              element.bind('keypress', function(event) {
                if(event.keyCode === 32) {
                  event.preventDefault();
                }
              });
            }
          };
        });
    
    AddBillController.$inject = ['$location', '$scope', 'BillService', 'AuthenticationService', 'CompanyService', 'MetadataService','$window', '$timeout'];
	
    function AddBillController($location, $scope, BillService, AuthenticationService, CompanyService, MetadataService, $window, $timeout) {
	   	var serv = {};
	   	serv.AuthenticationService = AuthenticationService;
	   	serv.CompanyService = CompanyService;
	   	serv.currentUser = AuthenticationService.GetUsername();
	   	serv.currentUserFirstName = AuthenticationService.GetUserFirstName();
	   	serv.addCompany = addCompany;
	   	serv.addBill = addBill;
	   	serv.dataLoading = false;
	   	serv.remindersetting="always";
        $scope.companyList = getCompanyList();
        $scope.billTypeList = getBillFreqList();
	    
	    angular.element(document).ready(function () {
	    	$('#dateRangePicker').datepicker({
	    	    format: 'dd-mm-yyyy'
	    	});
	    	$timeout(function(){
	    		$('#companypicker').selectpicker();
		    	$('#companypicker').selectpicker("refresh");
		    	$('#billtypepicker').selectpicker();
		    	$('#billtypepicker').selectpicker("refresh");
		    	$('#reminderDaysPicker').selectpicker();
		    	$('#reminderDaysPicker').selectpicker("refresh");
	    	},10);
	    	$(function() {
	    	    $('#companypicker').change(function() {
	    	        // if changed to, for example, the last option, then
	    	        // $(this).find('option:selected').text() == D
	    	        // $(this).val() == 4
	    	        // get whatever value you want into a variable
	    	        var x = $(this).val();
	    	        // and update the hidden input's value
	    	        $('#companyId').val(x);
	    	    });
	    	});
	    	$(function() {
	    	    $('#billtypepicker').change(function() {
	    	        // if changed to, for example, the last option, then
	    	        // $(this).find('option:selected').text() == D
	    	        // $(this).val() == 4
	    	        // get whatever value you want into a variable
	    	        var x = $(this).val();
	    	        // and update the hidden input's value
	    	        $('#billType').val(x);
	    	        if (x=='O') {
	    	        	$('#recurcheckbox').prop("checked", false);
	    	        	$('#recurrence').val(false);
	    	        } else {
	    	        	$('#recurcheckbox').prop("checked", true);
	    	        	$('#recurrence').val(true);
	    	        }
	    	    });
	    	});
	    	$('#recurcheckbox').on('change', function() {
	    		var billType = $('#billtypepicker').val();
	    		if (billType == 'O') {
	    			if (this.checked) {
	    				alert("This option is not allowed for 'One Time' Bills.");
	    				this.checked = false;
	    				$('#recurrence').val(false);
	    			}
	    		}
	    		$('#recurrence').val(this.checked);
	    	});
	    });

        return serv;
        
        function addBill() {
        	var companyId = $('#companyId').val();
        	var billType = $('#billType').val();
        	var dueDate = $('#dueDt').val();
        	var recurrence = $('#recurrence').val();
        	var reminder = serv.remindersetting;
        	var reminderSetting = 0;
        	if (reminder=="days") {
        		var reminderSetting = $('#reminderDaysPicker').val();
        	} else {
        		reminderSetting = 0;
        	}
        	// validations
        	if (companyId == null || companyId == '') {
        		alert ("You have not selected any Company.");
        		return;
        	}
        	if (billType == null || billType == '') {
        		alert ("You have not selected the type of Bill.");
        		return;
        	}
        	if (dueDate == null || dueDate == '') {
        		alert ("You have not entered a due date for the Bill.");
        		return;
        	}
        	// Do not allow the user to select date
        	// between 29th and 31st of any month
        	var day = dueDate.substring(0,2);
        	if (day!=null) {
        		var d = parseInt(day);
        		if (day >= 29 && day <=31) {
        			alert ("You are not allowed to select a date between 29th and 31st of a month.");
            		return;
        		}
        	}
        	var userId = AuthenticationService.GetUserId()
        	// time to persist the data
        	serv.dataLoading = true;
        	console.log(recurrence);
        	BillService.addBill(companyId,billType,dueDate,serv.location,serv.amount,serv.description,serv.paymentMode,userId,recurrence,reminderSetting,function(response){
        		console.log(response);
        		var result = response.result;
        		serv.dataLoading = false;
        		if (response.result == false) {
        			serv.addBillFailure = true;
        			serv.addBillFailureMessage = response.message;
        		} else {
        			serv.addBillFailure = false;
        			serv.addBillFailureMessage = null;
        			serv.addBillSuccess = true;
        			serv.addBillSuccessMessage = response.message;
        			serv.addBillId = response.billId;
        			$scope.closeThisDialog('v');
        			alert("Bill added successfully. Bill ID is "+serv.addBillId);
        		}
        	});
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
        	    		$('#companypicker').selectpicker();
        	    		$('#companypicker').val(finalid);
        		    	$('#companypicker').selectpicker("refresh");
        		    	$('#companyId').val(finalid);
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
        
        function getBillFreqList() {
        	MetadataService.getBillFreqList(function (response) {
                if (response) {
                    $scope.billTypeList = response.data;
                } else {
                	$scope.billTypeList = null;
                }
                return $scope.billTypeList;
            });
        }
        
	}
 
})();