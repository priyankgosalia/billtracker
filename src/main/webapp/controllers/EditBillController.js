// File: EditBillController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 2nd January 2016

(function () {
    'use strict';
    angular.module('billApp').controller('EditBillController', EditBillController);
    ViewBillController.$inject = ['$location', '$scope', 'BillService', 'CompanyService', 'MetadataService', 'AuthenticationService','$window', '$timeout'];

    function EditBillController($location, $scope, BillService, CompanyService, MetadataService, AuthenticationService, $window, $timeout) {
	   	var cm = {};
	   	cm.AuthenticationService = AuthenticationService;
	   	cm.CompanyService = CompanyService;
	   	cm.currentUser = AuthenticationService.GetUsername();
	   	cm.currentUserFirstName = AuthenticationService.GetUserFirstName();
	   	cm.getBillInfo = getBillInfo;
	   	cm.updateBill = updateBill;
	   	cm.dataLoading = false;
	   	
	   	$scope.companyList = getCompanyList();
        $scope.billTypeList = getBillFreqList();
        
	    angular.element(document).ready(function () {
	    	getBillInfo($scope.billId);
	    	$('#dateRangePicker').datepicker({
	    	    format: 'dd-mm-yyyy'
	    	});
	    	$timeout(function(){
	    		$('#companypicker').selectpicker();
		    	$('#companypicker').selectpicker("refresh");
		    	$('#billtypepicker').selectpicker();
		    	$('#billtypepicker').selectpicker("refresh");
	    	},1);
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

        return cm;
        
        function updateBill() {
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
        			$scope.closeThisDialog(serv.addBillId);
        			alert("Bill added successfully. Bill ID is "+serv.addBillId);
        		}
        	});
        }

        function getBillInfo(billId) {
        	cm.dataLoading = true;
        	BillService.getBillInfoForEdit(billId,function (response) {
                if (response) {
                    $scope.billInfo = response.data;
                    $timeout(function(){
                    	// set data in the ng-models
                    	cm.location = $scope.billInfo.location;
                    	cm.amount = $scope.billInfo.amount;
                    	cm.paymentMode = $scope.billInfo.paymentMode;
                    	cm.description = $scope.billInfo.description;
                    	cm.billType = $scope.billInfo.frequency;
                    	cm.company = $scope.billInfo.company;
                    	cm.recurrence = $scope.billInfo.recurring;
                    	cm.reminderDays = $scope.billInfo.reminderDays;
                    	
                    	if (cm.reminderDays!=null && cm.reminderDays>0) {
                    		$('#remNumber').prop("checked", true);
                    		$('#reminderDaysPicker').val(cm.reminderDays);
                    	} else {
                    		$('#remAlways').prop("checked", true);
                    	}
                    	
                    	$('#recurcheckbox').prop("checked", cm.recurrence);
	    	        	$('#recurrence').val(cm.recurrence);
                    	$('select[name=companylist]').val(cm.company);
                    	$('select[name=billfreq]').val(cm.billType);
                    	$('.selectpicker').selectpicker('refresh');
                    	$('#recurrence').val(cm.recurrence);
                    	
                    	var d = new Date($scope.billInfo.dueDate),
                    	
                        month = '' + (d.getMonth() + 1),
                        day = '' + d.getDate(),
                        year = d.getFullYear();
	                    if (month.length < 2) month = '0' + month;
	                    if (day.length < 2) day = '0' + day;
	                    $scope.billInfo.dueDate=[day, month, year].join('-');
	                    $('#dueDt').val($scope.billInfo.dueDate);
	                    $("#dateRangePicker").datepicker("update", d);
	                    if ($scope.billInfo.recurring == true) {
	                    	$scope.recurringText = "(Recurring Bill)";
	                    } else {
	                    	$scope.recurringText = "(Non-Recurring Bill)";
	                    }
	                    if ($scope.billInfo.status == "Paid") {
	                    	$scope.paid = true;
	                    } else {
	                    	$scope.paid = false;
	                    }
        	    	},100);
                } else {
                	$scope.billInfo = null;
                }
                cm.dataLoading = false;
                return $scope.billInfo;
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