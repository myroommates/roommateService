myApp.controller('RoommateManagementCtrl',  function ($scope, $http, $flash,translationService,$locale) {

    $scope.roommateList = initialData;
    $scope.moneySymbol= data.home.moneySymbol;

    $scope.edit = function (roommate) {
        roommate.edit = !roommate.edit;
    };

    $scope.remove = function (roommate) {

        if (!!roommate.id) {
            roommate.isLoading = true;
            $http({
                'method': 'DELETE',
                'url': '/rest/roommate/' + roommate.id,
                'headers': "Content-Type:application/json"
            }).success(function (data, status) {

                for (var i = 0; i < $scope.roommateList.length; i++) {
                    if ($scope.roommateList[i] == roommate) {
                        $scope.roommateList.splice(i, 1);

                    }
                }
                roommate.isLoading = false;
            })
                .error(function (data, status) {
                    $flash.error(data.message);
                    roommate.isLoading = false;
                });
        }
        else {
            for (var i = 0; i < $scope.roommateList.length; i++) {
                if ($scope.roommateList[i] == roommate) {
                    $scope.roommateList.splice(i, 1);

                }
            }
        }
    };

    $scope.save = function (roommate) {

        var method = "";
        var url = "/rest/roommate";


        if (roommate.id != null && roommate.id != undefined) {
            method = 'PUT';
            url += "/" + roommate.id;
        }
        else {
            method = 'POST';
        }

        roommate.isLoading = true;
        $http({
            'method': method,
            'url': url,
            'headers': "Content-Type:application/json",
            'data': roommate
        }).success(function (data, status) {

            for (var i = 0; i < $scope.roommateList.length; i++) {
                if ($scope.roommateList[i] == roommate) {
                    $scope.roommateList.splice(i, 1, data);

                }
            }
            roommate.edit = false;
            roommate.isLoading = false;
        })
        .error(function (data, status) {
            $flash.error(data.message);
            if (roommate.id != null) {
                // TODO restore last valid data
                roommate.edit = false;
            }
            roommate.isLoading = false;
        });
    };

    $scope.add = function () {
        $scope.roommateList.push({
            'id': null,
            'nameAbrv': '',
            'name': '',
            'email': '',
            'edit': true
        });
    };

});