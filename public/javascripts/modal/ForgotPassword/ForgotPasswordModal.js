myApp.controller('ForgotPasswordModalCtrl', function ($scope, $http, $flash, $modalInstance,$filter) {

    $scope.loading=false;

    $scope.fields = {
        email: {
            fieldType:"email",
            name:'email',
            fieldTitle: "changeEmailModal.email",
            validationRegex: /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
            validationMessage: "generic.validation.email",
            focus: function(){
                return true;
            },
            disabled:function(){
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

    $scope.save = function () {

        for (var key in $scope.fields) {
            var field = $scope.fields[key];
            if ($scope.fields.hasOwnProperty(key)) {
                field.firstAttempt = false;
            }
        }
        if ($scope.allFieldValid()) {

            var dto = {
                email: $scope.fields.email.field
            };

            $scope.loading=true;

            $http({
                'method': "PUT",
                'url': "/rest/password",
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.loading=false;
                $scope.close();
                $flash.success($filter('translateText')('forgotPassword.success'));
            })
            .error(function (data, status) {
                $scope.loading=false;
                $flash.error(data.message);
            });
        }
    }


});