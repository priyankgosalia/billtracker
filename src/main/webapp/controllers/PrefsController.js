// File: PrefsController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 14th January 2016

(function () {
    'use strict';
 
    angular.module('billApp').controller('PrefsController', PrefsController);
    PrefsController.$inject = ['$location', '$scope', 'PrefsService', 'AuthenticationService', '$window'];
	
    function PrefsController($location, $scope, PrefsService, AuthenticationService, $window) {
	   	var cmp = {};
	   	cmp.getUserPrefs = getUserPrefs;
	   	cmp.savePrefs = savePrefs;
	   	cmp.logout = logout;
	   	cmp.AuthenticationService = AuthenticationService;
	   	cmp.PrefsService = PrefsService;
	   	cmp.currentUser = AuthenticationService.GetUsername();
        cmp.currentUserFirstName = AuthenticationService.GetUserFirstName();
	   	getUserPrefs(cmp.currentUser);
        return cmp;
        
        function getUserPrefs(user) {
        	PrefsService.getUserPrefs(user,function (response) {
                if (response) {
                	cmp.username = response.data.username;
                	cmp.firstName = response.data.firstName;
                	cmp.lastName = response.data.lastName;
                	cmp.password = response.data.password;
                	cmp.confirmPassword = response.data.confirmPassword;
                } else {
                	cmp.username = "";
                	cmp.firstName = "";
                	cmp.lastName = "";
                	cmp.password = "";
                	cmp.confirmPassword = "";
                }
            });
        }
        
        function savePrefs() {
        	PrefsService.saveUserPrefs(cmp.username,cmp.firstName,cmp.lastName,cmp.password,cmp.confirmPassword,function (response) {
        		console.log(response);
                if (response && response.code == 200) {
                    $scope.savePrefsResult = response.message;
                    $scope.savePrefsStatus = "OK";
                    getUserPrefs(cmp.username);
                } else {
                	$scope.savePrefsResult = response.message;
                    $scope.savePrefsStatus = "ERROR";
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