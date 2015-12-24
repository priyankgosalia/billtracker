// File: AboutController.js
// Author: Priyank Gosalia <priyank.gosalia@gmail.com>
// Created on: 24th December 2015

(function () {
    'use strict';
 
    angular.module('billApp').controller('AboutController', AboutController);
    AboutController.$inject = ['$location', '$scope', 'MetadataService'];
	
    function AboutController($location, $scope, MetadataService) {
	   	var am = {};
	   	am.getAppInfo = getAppInfo;
	   	getAppInfo();
        return am;
        
        function getAppInfo() {
        	MetadataService.getAppInfo(function (response) {
                if (response) {
                	console.log(response.data);
                    $scope.x = response.data;
                    $scope.appName = response.data.appName;
                    $scope.appVersion = response.data.appVersion;
                    $scope.appBuildTime = response.data.appBuildTime;
                }
            });
        }
	}
 
})();