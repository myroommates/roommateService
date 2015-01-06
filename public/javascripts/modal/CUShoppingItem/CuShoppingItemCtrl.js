myApp.controller('CuShoppingItemCtrl', function ($scope, $http, $flash, $modalInstance, shoppingItem, addItem, data) {

    $scope.onlyForMe = false;
    $scope.loading = false;
    $scope.mySelf = data.mySelf;

    $scope.fields = {
        description: {
            fieldTitle: "generic.description",
            validationRegex: "[a-zA-Z0-9-]{1,255}",
            validationMessage: ['generic.validation.size', '1', '255'],
            focus: function () {
                return true;
            },
            disabled: function () {
                return $scope.loading;
            }
        }
    };


    $scope.close = function () {
        $modalInstance.close();
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

    $scope.initialize = function () {

        //load old data
        if (!!shoppingItem) {
            $scope.edit = true;
            $scope.fields.description.field = angular.copy(shoppingItem.description);
            $scope.onlyForMe = angular.copy(shoppingItem.onlyForMe);

        }
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


            var request = "POST";
            var url = '/rest/shoppingItem';
            var dto;
            if (!!shoppingItem) {
                dto = shoppingItem;
                dto.description = $scope.fields.description.field;
                dto.onlyForMe = $scope.onlyForMe;
                request = 'PUT';
                url += "/" + shoppingItem.id;
            }
            else {
                dto = {
                    description: $scope.fields.description.field,
                    onlyForMe: $scope.onlyForMe
                }
            }
            $scope.loading = true;

            $http({
                'method': request,
                'url': url,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.loading = false;
                addItem(data);
                $scope.close();
            })
                .error(function (data, status) {
                    $scope.loading = false;
                    $flash.error(data.message);
                });
        }
    }
});