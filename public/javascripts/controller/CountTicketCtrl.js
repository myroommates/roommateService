myApp.controller('CountTicketCtrl', function ($scope, $http, $flash, $modal,translationService,$locale,tmhDynamicLocale) {

    $scope.myself = data.mySelf;
    $scope.moneySymbol= data.home.moneySymbol;
    $scope.ticketList = ticketList.list;

    $scope.sortSelection = [
        {ref: 'date', label: 'count.ticket.sort.date'},
        {ref: 'category', label: 'count.ticket.sort.category'},
        {ref: 'payer', label: 'count.ticket.sort.payer'}
    ];

    $scope.sortOption = 'date';


    for (var key in $scope.ticketList) {
        $scope.ticketList[key].debtorList = _.sortBy($scope.ticketList[key].debtorList,
            function (element) {
                return element.roommateId;
            });
    }

    $scope.sort = function () {
        $scope.sortBy($scope.sortOption);
    };

    $scope.sortBy = function (critera) {

        $scope.ticketList = _.sortBy($scope.ticketList, function (element) {
            if (critera == 'payer') {
                return element.payerId;
            }
            else if (critera == 'category') {
                return element.category;
            }
            else if (critera == 'date') {
                return -element.date;
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

    $scope.addItem = function (ticket) {

        for (var key in $scope.ticketList) {
            if ($scope.ticketList[key].id == ticket.id) {
                $scope.ticketList.splice(key, 1, ticket);
                return;
            }
        }
        $scope.ticketList.push(ticket);
        $scope.sort();

    };

    $scope.remove = function (ticket) {
        ticket.isLoading = true;
        $http({
            'method': "DELETE",
            'url': '/rest/ticket/' + ticket.id,
            'headers': "Content-Type:application/json"
        }).success(function (data, status) {
            ticket.isLoading = false;

            for (var key in $scope.ticketList) {
                if ($scope.ticketList[key].id == ticket.id) {
                    $scope.ticketList.splice(key, 1);
                }
            }

        })
            .error(function (data, status) {
                ticket.isLoading = false;
                $flash.error(data.message);
            });
    };

    $scope.getGlobalValue = function (ticket) {
        var total = 0;
        for (var key in ticket.debtorList) {
            total += ticket.debtorList[key].value;
        }
        return total;
    };

    $scope.createOrEditTicket = function (ticket) {

        //build category list
        var categoryList = [];
        for (var key in $scope.ticketList) {
            if (!!$scope.ticketList[key].category && _.contains(categoryList,$scope.ticketList[key].category) == false) {
                categoryList.push($scope.ticketList[key].category);
            }
        }

        var resolve = {
            roommateList: function () {
                return roommateList;
            },
            ticket: function () {
                return ticket;
            },
            addItem: function () {
                return $scope.addItem;
            },
            categoryList: function () {
                return categoryList;
            },
            moneySymbol:function(){
                return $scope.moneySymbol;
            },
            shoppingItemList: function(){
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


});