
angular.module("tmh.dynamicLocale").config(function (tmhDynamicLocaleProvider) {
    tmhDynamicLocaleProvider.localeLocationPattern('/assets/components/angular-i18n/angular-locale_{{locale}}.js');
});

var myApp = angular.module('app', ['ui.bootstrap.datetimepicker', 'ui.bootstrap', "angucomplete", 'tmh.dynamicLocale']);

myApp.controller('MainCtrl', function ($scope,$locale, tmhDynamicLocale,translationService,$modal) {

    if ("data" in window && data!=undefined && data!=null) {
        tmhDynamicLocale.set(data.langId);

        translationService.set(data.translations);
    }

    $scope.helpDisplayed=false;

    $scope.displayHelp = function(){
        $scope.helpDisplayed = true;
    };

    $scope.maskHelp = function(){
        $scope.helpDisplayed = false;
    };

    $scope.openHelp = function(message){

        var resolve = {
            message: function () {
                return message;
            }
        };

        $modal.open({
            templateUrl: "/assets/javascripts/modal/HelpModal/view.html",
            controller: "HelpModalCtrl",
            size: 'sm',
            resolve: resolve
        });
    };
});