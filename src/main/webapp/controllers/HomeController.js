// File: CompaniesController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 12th December 2015

(function () {
    'use strict';
 
    angular
        .module('billApp')
        .controller('HomeController', HomeController);
 
    HomeController.$inject = ['$location', 'AuthenticationService','$rootScope', '$window'];
    function HomeController($location, AuthenticationService, $rootScope, $window) {
        var vm = this;
        vm.currentUser = AuthenticationService.GetUsername();
        vm.currentUserFirstName = AuthenticationService.GetUserFirstName();
        vm.logout = logout;
        vm.AuthenticationService = AuthenticationService;
        
        return vm;
        
        function logout() {
        	if ($window.confirm('Are you sure you want to Logout?')) {
        		AuthenticationService.ClearCredentials();
            	$location.path('/#/login');
        	}
        }
    }
 
})();