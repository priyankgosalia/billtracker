// File: LoginController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 12th December 2015

(function () {
    'use strict';
 
    angular
        .module('billApp')
        .controller('LoginController', LoginController);
 
    LoginController.$inject = ['$location', 'AuthenticationService'];
    function LoginController($location, AuthenticationService) {
        var vm = this;
        vm.login = login;
 
        (function initController() {
            // reset login status
        	console.log("Init called for LoginController")
            AuthenticationService.ClearCredentials();
        })();
 
        function login() {
            vm.dataLoading = true;
            AuthenticationService.Login(vm.username, vm.password, function (response) {
                if (response.result) {
                    AuthenticationService.SetCredentials(vm.username, vm.password);
                    AuthenticationService.SetUsername(vm.username);
                    AuthenticationService.SetUserFirstName(response.userFirstName);
                    vm.loginFailure = false;
                    $location.path('/');
                } else {
                    vm.dataLoading = false;
                    vm.loginFailure = true;
                    vm.failureMessage = response.message;
                }
            });
        };
    }
 
})();