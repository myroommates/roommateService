myApp.controller('ChangePasswordModalCtrl', function ($scope, $http, $flash, $modalInstance, roommate) {

    $scope.fields = {
        oldPassword: {
            name:'password',
            fieldTitle: "OLD_PASSWORD",
            validationRegex: "^[a-zA-Z0-9-_%]{6,18}$",
            validationMessage: "IDENTIFIER_CHECK_WRONG",
            fieldType: 'password',
            focus: function () {
                return true;
            }
        },
        newPassword: {
            fieldTitle: "USER_IDENTIFIER",
            validationRegex: "^[a-zA-Z0-9-_%]{6,18}$",
            validationMessage: "IDENTIFIER_CHECK_WRONG",
            fieldType: 'password'
        },
        repeatPassword: {
            fieldTitle: "USER_IDENTIFIER",
            fieldType: 'password',
            validationMessage: "IDENTIFIER_CHECK_WRONG",
            validation: function () {
                return $scope.o.newPassword === $scope.o.repeatPassword;
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

    $scope.save = function () {

        for (var key in $scope.fields) {
            var field = $scope.fields[key];
            if ($scope.fields.hasOwnProperty(key)) {
                field.firstAttempt = false;
            }
        }
        if ($scope.allFieldValid()) {

            var dto = {
                oldPassword: $scope.fields.oldPassword.field,
                newPassword: $scope.fields.newPassword.field
            };

            $http({
                'method': "PUT",
                'url': "/rest/roommate/password/" + roommate.id,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.close();
            })
                .error(function (data, status) {
                    console.log(data);
                    $flash.error(data.message);
                });
        }
    }


});