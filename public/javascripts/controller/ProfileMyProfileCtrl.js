myApp.controller('ProfileMyProfileCtrl', function ($scope, $http, $flash, $modal,translationService,$window) {


    $scope.roommate = data.mySelf;
    $scope.languages = data.languages;
    $scope.moneySymbol= data.home.moneySymbol;
    $scope.loading=false;

    $scope.languagesList = [];

    for(var key in $scope.languages){
        if($scope.languages.hasOwnProperty(key)){
            $scope.languagesList.push({
                key:$scope.languages[key].code,
                value:$scope.languages[key].language
            });
        }
    }

    $scope.fields = {

        languages: {
            fieldTitle: "generic.yourLanguage",
            options:$scope.languagesList,
            field:data.langId,
            disabled:function(){
                return $scope.loading;
            }
        },
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
        },
        email: {
            fieldType:"email",
            name:'email',
            fieldTitle: "changeEmailModal.email",
            validationRegex: /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
            validationMessage: "generic.validation.email",
            field:$scope.roommate.email,
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
                nameAbrv: $scope.fields.nameAbrv.field,
                languageCode:$scope.fields.languages.field,
                email:$scope.fields.email.field
            };

            $scope.loading=true;

            $http({
                'method': "PUT",
                'url': "/rest/roommate/"+$scope.roommate.id,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.loading=false;
                if($scope.fields.languages.field != data.langId){
                    $window.location.reload();
                }
            })
            .error(function (data, status) {
                    $scope.loading=false;
                $flash.error(data.message);
            });
        }
    };


});