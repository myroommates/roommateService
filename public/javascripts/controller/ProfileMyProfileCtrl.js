myApp.controller('ProfileMyProfileCtrl', function ($scope, $http, $flash, $modal) {

    $scope.roommate = mySelf;

    $scope.fields = {
        name: {
            fieldTitle: "USER_IDENTIFIER",
            validationRegex: "^[a-zA-Z0-9-_%]{1,50}$",
            validationMessage: "IDENTIFIER_CHECK_WRONG",
            field:$scope.roommate.name,
            focus: function () {
                return true;
            }
        },
        nameAbrv: {
            fieldTitle: "USER_IDENTIFIER",
            validationRegex: "^[a-zA-Z0-9-_%]{1,3}$",
            validationMessage: "IDENTIFIER_CHECK_WRONG",
            field:$scope.roommate.nameAbrv
        }
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

    $scope.changePassword = function () {
        var resolve = {
            roommate: function () {
                return $scope.roommate;
            }
        }
        $modal.open({
            templateUrl: "/assets/javascripts/modal/ChangePassword/view.html",
            controller: "ChangePasswordModalCtrl",
            size: 'lg',
            resolve: resolve
        });
    };

    $scope.setEmail = function (newEmail) {
        $scope.roommate.email = newEmail;
    };

    $scope.changeEmail = function () {
        var resolve = {
            roommate: function () {
                return $scope.roommate;
            },
            setEmail: function () {
                return $scope.setEmail;
            }
        }
        $modal.open({
            templateUrl: "/assets/javascripts/modal/ChangeEmail/view.html",
            controller: "ChangeEmailModalCtrl",
            size: 'lg',
            resolve: resolve
        });
    };

    $scope.save = function () {
        if ($scope.allFieldValid()) {

            //save
            var dto = {
                name: $scope.fields.name.field,
                nameAbrv: $scope.fields.nameAbrv.field
            };

            $http({
                'method': "PUT",
                'url': "/rest/roommate/"+$scope.roommate.id,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.close();
            })
            .error(function (data, status) {
                $flash.error(data.message);
            });
        }
    };


});