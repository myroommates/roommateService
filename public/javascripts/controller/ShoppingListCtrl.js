myApp.controller('ShoppingListCtrl', function ($scope, $http, $flash, $modal,translationService,$locale,tmhDynamicLocale) {

    $scope.myself = data.mySelf;
    $scope.shoppingItemList = shoppingItemList.list;

    $scope.sortSelection = [
        {ref: 'date', label: 'generic.date'},
        {ref: 'creator', label: 'generic.creator'}
    ];

    $scope.sortOption = 'date';

    $scope.sort = function () {
        $scope.sortBy($scope.sortOption);
    };

    $scope.sortBy = function (critera) {

        $scope.shoppingItemList = _.sortBy($scope.shoppingItemList, function (element) {
            if (critera == 'creator') {
                return element.creatorId;
            }
            else if (critera == 'date') {
                return -element.creationDate;
            }
        });
    };

    $scope.sort();

    $scope.$watch('sortOption', function (o, n) {
        if (o != n) {
            $scope.sortBy($scope.sortOption);
        }
    });

    $scope.getRoommate = function (id) {
        for (var key in roommateList) {
            if (roommateList[key].id == id) {
                return roommateList[key];
            }
        }
        return null;
    };

    $scope.addItem = function (item) {

        for (var key in $scope.shoppingItemList) {
            if ($scope.shoppingItemList[key].id == item.id) {
                $scope.shoppingItemList.splice(key, 1, item);
                return;
            }
        }
        $scope.shoppingItemList.push(item);
        $scope.sort();

    };

    $scope.remove = function (item) {
        item.isLoading = true;
        $http({
            'method': "DELETE",
            'url': '/rest/shoppingItem/' + item.id,
            'headers': "Content-Type:application/json"
        }).success(function (data, status) {
            item.isLoading = false;

            for (var key in $scope.shoppingItemList) {
                if ($scope.shoppingItemList[key].id == item.id) {
                    $scope.shoppingItemList.splice(key, 1);
                }
            }

        })
            .error(function (data, status) {
                item.isLoading = false;
                $flash.error(data.message);
            });
    };

    $scope.createOrEditShoppingItem = function (item) {

        var resolve = {
            shoppingItem: function () {
                return item;
            },
            addItem: function () {
                return $scope.addItem;
            },
            data:function(){
                return data;
            }
        };

        $modal.open({
            templateUrl: "/assets/javascripts/modal/CUShoppingItem/view.html",
            controller: "CuShoppingItemCtrl",
            size: 'lg',
            resolve: resolve
        });
    };


});