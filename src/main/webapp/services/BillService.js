(function () {
    'use strict';
 
    angular
        .module('billApp')
        .factory('BillService', BillService);
 
    BillService.$inject = ['$http','AuthenticationService'];
    function BillService($http,AuthenticationService) {
        var cmservice = this;
        cmservice.getAllBills = getAllBills;
        cmservice.getAllReminders = getAllReminders;
        cmservice.addBill = addBill;
        cmservice.updateBill = updateBill;
        cmservice.deleteBill = deleteBill;
        cmservice.getBillInfo = getBillInfo;
        cmservice.markPaid = markPaid;
        cmservice.getBillInfoForEdit = getBillInfoForEdit;
        return cmservice;
 
        function getAllBills(callback) {
        	$http({
        		  method: 'GET',
        		  url: 'ws/bill/getAllBills'
        	}).then(function successCallback(response) {
				console.log("getAllBills(Success) = "+response);
		        callback(response);
        	}, function errorCallback(response) {
        		console.log("getAllBills(Error) = "+response);
        	});
        }
        
        function getAllReminders(callback) {
        	$http({
        		  method: 'GET',
        		  url: 'ws/bill/getAllReminders'
        	}).then(function successCallback(response) {
				console.log("getAllReminders(Success) = "+response);
		        callback(response);
        	}, function errorCallback(response) {
        		console.log("getAllReminders(Error) = "+response);
        	});
        }
        
        function getBillInfo(billId,callback) {
        	$http({
        		  method: 'GET',
        		  url: 'ws/bill/billInfo'+'?id='+billId
        	}).then(function successCallback(response) {
				console.log("getBillInfo(Success) = "+response);
		        callback(response);
        	}, function errorCallback(response) {
        		console.log("getBillInfo(Error) = "+response);
        	});
        }
        
        function getBillInfoForEdit(billId,callback) {
        	$http({
        		  method: 'GET',
        		  url: 'ws/bill/billInfoForEdit'+'?id='+billId
        	}).then(function successCallback(response) {
				console.log("getBillInfo(Success) = "+response);
		        callback(response);
        	}, function errorCallback(response) {
        		console.log("getBillInfo(Error) = "+response);
        	});
        }
        
        function markPaid(billId,callback) {
        	$http({
      		  method: 'GET',
      		  url: 'ws/bill/markPaid'+'?id='+billId
	      	}).then(function successCallback(response) {
				console.log("markPaid(Success) = "+response);
		        callback(response);
	      	}, function errorCallback(response) {
	      		console.log("markPaid(Error) = "+response);
	      	});
        }
        
        function addBill(companyId,billType,dueDate,location,amount,desc,paymentMode,userId,recurrence,reminder,callback) {
            $http.post('ws/bill/addBill', { companyId: companyId, 
            								billType: billType,
            								dueDate: dueDate,
            								amount: amount,
            								location:location,
            								description:desc,
            								paymentMode:paymentMode,
            								userId:userId,
            								paid:0,
            								recurrence:recurrence,
            								reminderDays:reminder})
                .success(function (response) {
                	console.log(response);
                    callback(response);
                });
        }
        
        function updateBill(billId,companyId,billType,dueDate,location,amount,desc,paymentMode,userId,recurrence,reminder,callback) {
            $http.post('ws/bill/updateBill', { 
            								billId : billId,
            								companyId: companyId, 
            								billType: billType,
            								dueDate: dueDate,
            								amount: amount,
            								location:location,
            								description:desc,
            								paymentMode:paymentMode,
            								userId:userId,
            								paid:0,
            								recurrence:recurrence,
            								reminderDays:reminder})
                .success(function (response) {
                	console.log(response);
                    callback(response);
                });
        }
        
        function deleteBill(billId,deleteRecur,callback) {
            $http.post('ws/bill/deleteBill', { billId: billId, 
            									deleteRecur:deleteRecur})
                .success(function (response) {
                	console.log(response);
                    callback(response);
                });
        }
    }
})();