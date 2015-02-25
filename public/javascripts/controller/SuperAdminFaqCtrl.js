myApp.controller('SuperAdminFaqCtrl', function ($scope, $http, $flash, $modal, translationService, $locale, tmhDynamicLocale) {

    $scope.faqs = faqs;
    $scope.langs = langs;

    $scope.createNewFaq = function () {

        var resolve = {
            langs: function () {
                return $scope.langs;
            }
        };

        $modal.open({
            templateUrl: "/assets/javascripts/modal/CreateFaq/view.html",
            controller: "CreateFaqModalCtrl",
            size: 'lg',
            resolve: resolve
        });
    };

    $scope.createNewSurvey = function () {

        var resolve = {
            langs: function () {
                return $scope.langs;
            }
        };

        $modal.open({
            templateUrl: "/assets/javascripts/modal/CreateSurveyModal/view.html",
            controller: "CreateSurveyModalCtrl",
            size: 'lg',
            resolve: resolve
        });
    };

});