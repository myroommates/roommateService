myApp.controller('AdminPreferenceCtrl', function ($scope, $flash, $http) {


    $scope.home = data.home;

    $scope.fields = {
        currency: {
            fieldTitle: "admin.preferences.currency",
            validationRegex: "^.{1,3}$",
            validationMessage: ['generic.validation.size', '1', '255'],
            disabled: function () {
                return $scope.loading;
            }
        }
    };

    $scope.initialize = function () {

        //load old data
        $scope.fields.currency.field = data.home.moneySymbol;
    };

    $scope.allFieldValid = function () {

        for (var key in $scope.fields) {
            var obj = $scope.fields[key];
            if ($scope.fields.hasOwnProperty(key) && (obj.isValid == null || obj.isValid === false)) {

                return false;
            }
        }
        return true;
    };

    $scope.initialize();

    $scope.save = function () {

        for (var key in $scope.fields) {
            var field = $scope.fields[key];
            if ($scope.fields.hasOwnProperty(key)) {
                field.firstAttempt = false;
            }
        }
        if ($scope.allFieldValid()) {

            //build dto
            var dto = {
                moneySymbol: $scope.fields.currency.field
            };

            $scope.loading = true;

            $http({
                'method': "PUT",
                'url': '/rest/home/' + $scope.home.id,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.loading = false;
            })
                .error(function (data, status) {
                    $scope.loading = false;
                    $flash.error(data.message);
                });
        }
    }

});