myApp.controller('ChangeEmailModalCtrl', function ($scope, $http, $flash, $modalInstance,roommate,setEmail) {


    $scope.fields = {
        oldPassword: {
            name:'password',
            fieldTitle: "USER_IDENTIFIER",
            validationRegex: "^[a-zA-Z0-9-_%]{6,18}$",
            validationMessage: "IDENTIFIER_CHECK_WRONG",
            fieldType:'password',
            focus: function(){
                return true;
            }
        },
        newEmail: {
            name:'email',
            fieldTitle: "USER_IDENTIFIER",
            validationRegex: /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
            validationMessage: "IDENTIFIER_CHECK_WRONG"
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
                newEmail: $scope.fields.newEmail.field
            };

            $http({
                'method': "PUT",
                'url': "/rest/roommate/email/"+roommate.id,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.close();
                setEmail(data.email);
            })
            .error(function (data, status) {
                $flash.error(data.message);
            });
        }
    }


});