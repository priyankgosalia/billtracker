// File: app.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 12th December 2015

(function () {
    'use strict';
 
    var billApp = angular
        .module('billApp', ['ngRoute', 'ngCookies', 'ngDialog', 'agGrid'])
        .config(config)
        .run(run);
 
    config.$inject = ['$routeProvider', '$locationProvider'];
    function config($routeProvider, $locationProvider) {
        $routeProvider
            .when('/', {
                controller: 'HomeController',
                templateUrl: 'pages/home.html',
                controllerAs: 'hm'
            })
 
            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'pages/login.html',
                controllerAs: 'vm'
            })
            
            .when('/companies', {
                controller: 'CompaniesController',
                templateUrl: 'pages/companies.html',
                controllerAs: 'cm'
            })
            
            .when('/bills', {
                controller: 'BillsController',
                templateUrl: 'pages/bills.html',
                controllerAs: 'bm'
            })
            
            .when('/about', {
            	controller: 'AboutController',
                templateUrl: 'pages/about.html',
                controllerAs: 'am'
            })
 
            .otherwise({ redirectTo: '/login' });
    }
 
    run.$inject = ['$rootScope', '$location', '$cookieStore', '$http'];
    function run($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }
 
        $rootScope.$on('$locationChangeStart', function (event, next, current) {
        	$rootScope.globals = $cookieStore.get('globals') || {};
            // redirect to login page if not logged in and trying to access a restricted page
            var restrictedPage = $.inArray($location.path(), ['/login', '/about']) === -1;
            var loggedIn = $rootScope.globals.currentUser;
            
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }
        });
    }
 
})();