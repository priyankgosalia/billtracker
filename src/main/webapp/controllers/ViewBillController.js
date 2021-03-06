// File: ViewBillController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 2nd January 2016

(function () {
    'use strict';
    angular.module('billApp').controller('ViewBillController', ViewBillController);
    ViewBillController.$inject = ['$location', '$scope', 'BillService', 'AuthenticationService','$window', '$timeout'];

    function ViewBillController($location, $scope, BillService, AuthenticationService, $window, $timeout) {
	   	var cm = {};
	   	cm.AuthenticationService = AuthenticationService;
	   	cm.currentUser = AuthenticationService.GetUsername();
	   	cm.currentUserFirstName = AuthenticationService.GetUserFirstName();
	   	cm.getBillInfo = getBillInfo;
	   	cm.dataLoading = false;

	    angular.element(document).ready(function () {
	    	getBillInfo($scope.billId);
	    });

        return cm;

        function getBillInfo(billId) {
        	cm.dataLoading = true;
        	BillService.getBillInfo(billId,function (response) {
                if (response) {
                    $scope.billInfo = response.data;
                    $timeout(function(){
                    	var d = new Date($scope.billInfo.dueDate),
                        month = '' + (d.getMonth() + 1),
                        day = '' + d.getDate(),
                        year = d.getFullYear();
	                    if (month.length < 2) month = '0' + month;
	                    if (day.length < 2) day = '0' + day;
	                    $scope.billInfo.dueDate=[day, month, year].join('/');
	                    if ($scope.billInfo.recurring == true) {
	                    	$scope.recurringText = "Recurring Bill";
	                    } else {
	                    	$scope.recurringText = "Non-Recurring Bill";
	                    }
	                    if ($scope.billInfo.status == "Paid") {
	                    	$scope.paid = true;
	                    } else {
	                    	$scope.paid = false;
	                    }
	                    if ($scope.billInfo.autoGenerated == true) {
	                    	$scope.autoGen = true;
	                    } else {
	                    	$scope.autoGen = false;
	                    }
        	    	},10);
                } else {
                	$scope.billInfo = null;
                }
                cm.dataLoading = false;
                return $scope.billInfo;
            });
        }
	}
})();