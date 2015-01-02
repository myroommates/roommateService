myApp.controller('HomeCtrl', function ($scope, $modal) {


    $scope.mySelf = data.mySelf;
    $scope.moneySymbol = data.home.moneySymbol;
    $scope.ticketList = ticketList;
    $scope.shoppingItemList = shoppingItemList;
    $scope.roommateList = data.roommateList;
    $scope.moneySymbol = data.home.moneySymbol;

    $scope.sortTicketList = function () {
        $scope.shoppingItemList = _.sortBy($scope.shoppingItemList, function (element) {
            return -element.creationDate;
        });
    };
    $scope.sortTicketList();

    $scope.computeCountBalance = function () {
        var spend = 0, dept = 0;

        for (var key in $scope.ticketList) {
            for (var key2 in $scope.ticketList[key].debtorList) {
                if ($scope.ticketList[key].payerId == $scope.mySelf.id) {
                    spend += $scope.ticketList[key].debtorList[key2].value;
                }
                if ($scope.ticketList[key].debtorList[key2].roommateId == $scope.mySelf.id) {
                    dept += $scope.ticketList[key].debtorList[key2].value;
                }
            }
        }
        return spend - dept;
    };


    $scope.getTotalItemSelected = function () {
        var total = 0;
        for (var key in $scope.shoppingItemList) {
            if (!!$scope.shoppingItemList[key].selected && $scope.shoppingItemList[key].selected == true) {
                total++;
            }
        }
        return total;
    };

    $scope.addItem = function (ticket) {
        $scope.ticketList.push(ticket);

        for (var key in $scope.shoppingItemList) {
            if (!!$scope.shoppingItemList[key].selected && $scope.shoppingItemList[key].selected == true) {
                $scope.shoppingItemList.splice(key, 1);
            }
        }
    };

    $scope.resolveSelectedItem = function () {
        var listSelected = [];
        for (var key in $scope.shoppingItemList) {
            if (!!$scope.shoppingItemList[key].selected && $scope.shoppingItemList[key].selected == true) {
                listSelected.push($scope.shoppingItemList[key]);
            }
        }


        //open modal


        //build category list
        var categoryList = [];
        for (var key in $scope.ticketList) {
            if (!!$scope.ticketList[key].category && _.contains(categoryList, $scope.ticketList[key].category) == false) {
                categoryList.push($scope.ticketList[key].category);
            }
        }

        var resolve = {
            roommateList: function () {
                return roommateList;
            },
            ticket: function () {
                return null;
            },
            addItem: function () {
                return $scope.addItem;
            },
            categoryList: function () {
                return categoryList;
            },
            moneySymbol: function () {
                return $scope.moneySymbol;
            },
            shoppingItemList: function () {
                return listSelected;
            }
        };

        $modal.open({
            templateUrl: "/assets/javascripts/modal/CUTicket/view.html",
            controller: "CuTicketCtrl",
            size: 'lg',
            resolve: resolve
        });
    };

    $scope.createTicket = function () {

        //build category list
        var categoryList = [];
        for (var key in $scope.ticketList) {
            if (!!$scope.ticketList[key].category && _.contains(categoryList, $scope.ticketList[key].category) == false) {
                categoryList.push($scope.ticketList[key].category);
            }
        }

        var resolve = {
            roommateList: function () {
                return roommateList;
            },
            ticket: function () {
                return null;
            },
            addItem: function () {
                return function (ticket) {
                    $scope.ticketList.push(ticket);
                };
            },
            categoryList: function () {
                return categoryList;
            },
            moneySymbol: function () {
                return $scope.moneySymbol;
            },
            shoppingItemList: function () {
                return null;
            }
        };

        $modal.open({
            templateUrl: "/assets/javascripts/modal/CUTicket/view.html",
            controller: "CuTicketCtrl",
            size: 'lg',
            resolve: resolve
        });
    };

    $scope.createShoppingItem = function () {

        var resolve = {
            shoppingItem: function () {
                return null;
            },
            addItem: function () {
                return function (shoppingItem) {
                    $scope.shoppingItemList.push(shoppingItem);
                    $scope.sortTicketList();
                };
            },
            data: function () {
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