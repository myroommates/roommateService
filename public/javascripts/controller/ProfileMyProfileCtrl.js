myApp.controller('ProfileMyProfileCtrl', function ($scope, $http, $flash, $modal,translationService,$locale) {


    $scope.roommate = data.mySelf;
    $scope.languages = data.languages;
    $scope.moneySymbol= data.home.moneySymbol;
    $scope.loading=false;

    console.log($scope.languages);

    $scope.fields = {
        name: {
            fieldTitle: "generic.yourName",
            validationRegex: "^[a-zA-Z0-9-_%]{1,50}$",
            validationMessage: ["generic.validation.size",'1','50'],
            field:$scope.roommate.name,
            focus: function () {
                return true;
            },
            disabled:function(){
                return $scope.loading;
            }
        },
        nameAbrv: {
            fieldTitle: "generic.yourNameAbrv",
            validationRegex: "^[a-zA-Z0-9-_%]{1,3}$",
            validationMessage: ["generic.validation.size",'1','3'],
            field:$scope.roommate.nameAbrv,
            disabled:function(){
                return $scope.loading;
            }
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

            $scope.loading=true;

            $http({
                'method': "PUT",
                'url': "/rest/roommate/"+$scope.roommate.id,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.loading=false;
                $scope.close();
            })
            .error(function (data, status) {
                    $scope.loading=false;
                $flash.error(data.message);
            });
        }
    };


});