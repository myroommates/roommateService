
angular.module("tmh.dynamicLocale").config(function (tmhDynamicLocaleProvider) {
    tmhDynamicLocaleProvider.localeLocationPattern('/assets/components/angular-i18n/angular-locale_{{locale}}.js');
});

var myApp = angular.module('app', ['ui.bootstrap.datetimepicker', 'ui.bootstrap', "angucomplete", 'tmh.dynamicLocale']);

myApp.controller('MainCtrl', function ($locale, tmhDynamicLocale,translationService) {

    if ("data" in window && data!=undefined && data!=null) {
        tmhDynamicLocale.set(data.langId);

        translationService.set(data.translations);
    }
});