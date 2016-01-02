(function () {
    'use strict';
 
    angular
        .module('billApp')
        .factory('BillService', BillService);
 
    BillService.$inject = ['$http','AuthenticationService'];
    function BillService($http,AuthenticationService) {
        var cmservice = this;
        cmservice.getAllBills = getAllBills;
        cmservice.addBill = addBill;
        cmservice.getBillInfo = getBillInfo;
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
        
        function addBill(companyId,billType,dueDate,location,amount,desc,paymentMode,userId,paid,callback) {
            $http.post('ws/bill/addBill', { companyId: companyId, 
            								billType: billType,
            								dueDate: dueDate,
            								amount: amount,
            								location:location,
            								description:desc,
            								paymentMode:paymentMode,
            								userId:userId,
            								paid:paid})
                .success(function (response) {
                	console.log(response);
                    callback(response);
                });
        }
    }
})();