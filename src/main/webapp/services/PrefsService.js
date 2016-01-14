(function () {
    'use strict';
 
    angular
        .module('billApp')
        .factory('PrefsService', PrefsService);
 
    PrefsService.$inject = ['$http','AuthenticationService'];
    function PrefsService($http,AuthenticationService) {
        var cmservice = this;
        cmservice.getUserPrefs = getUserPrefs;
        cmservice.saveUserPrefs = saveUserPrefs;
        return cmservice;
 
        function getUserPrefs(user,callback) {
        	$http({
        		  method: 'GET',
        		  url: 'ws/prefs/get?username='+user
        	}).then(function successCallback(response) {
				console.log("getUserPrefs(Success) = "+response);
		        callback(response);
        	}, function errorCallback(response) {
        		console.log("getUserPrefs(Error) = "+response);
        	});
        }
        
        function saveUserPrefs(username,firstName,lastName,password,confirmPassword,callback) {
        	$http.post('ws/prefs/save', {username:username,firstName:firstName,lastName:lastName,password:password,confirmPassword:confirmPassword})
			.success(function (response) {
				console.log(response);
				callback(response);
			});
        }
    }
})();