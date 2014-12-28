myApp.controller('CuTicketCtrl', function ($scope, $http, $flash, $modalInstance, roommateList, ticket, addItem, categoryList) {

    $scope.roommateList = roommateList;
    $scope.balanced = true;
    $scope.categoryList = categoryList;

    $scope.fields = {
        description: {
            fieldTitle: "generic.description",
            validationRegex: "[a-zA-Z0-9-]{1,255}",
            validationMessage: ['generic.validation.size','1','255'],
            focus: function () {
                return true;
            }
        },
        date: {
            fieldTitle: "generic.date",
            validationMessage: "generic.validation.date",
            field: new Date()
        },
        value: {
            fieldTitle: "generic.price",
            validationRegex: ".+",
            validationMessage: "generic.validation.notNull",
            numbersOnly: "double"
        },
        category: {
            fieldTitle: "generic.category",
            autoCompleteValue: categoryList
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

    $scope.initialize = function () {

        for (var key in $scope.roommateList) {

            roommate = $scope.roommateList[key];


            $scope.fields[roommate.id] = {
                'numbersOnly': 'double',
                field: "",
                isDebtor: true,
                isValid: true
            };

            $scope.addWatch(roommate.id);

        }

        //load old data
        if (!!ticket) {
            $scope.edit=true;
            $scope.fields.description.field = angular.copy(ticket.description);
            $scope.fields.date.field = angular.copy(ticket.date);
            $scope.fields.category.field = angular.copy(ticket.category);
            var totalValue = 0;

            for (var key in roommateList) {
                for (var key2 in ticket.debtorList) {

                    var founded = false;

                    if (ticket.debtorList[key2].roommateId == roommateList[key].id) {

                        var debtor = ticket.debtorList[key2];
                        totalValue += debtor.value;
                        $scope.fields[debtor.roommateId].field = debtor.value;

                        founded = true;

                        break;
                    }
                }
                if (founded == false) {
                    $scope.fields[roommateList[key].id].isPayer = false;
                }
            }

            $scope.fields.value.field = totalValue;
        }
    };

    $scope.addWatch = function (id) {

        $scope.$watch('fields[' + id + '].isDebtor', function (n, o) {
            $scope.fields[id].disabled = !$scope.fields[id].isDebtor;
            computeValues();
        });
    };

    $scope.initialize();


    $scope.$watch('fields.value.field', function (n, o) {
        computeValues();
    });

    var computeValues = function () {

        //compute debtors number
        var debtorNumber = 0;
        for (var key in $scope.roommateList) {
            var roommate = $scope.roommateList[key];
            if ($scope.fields[roommate.id].isDebtor) {
                debtorNumber++;
            }
        }

        var value = "";
        if (!!$scope.fields.value.field) {
            var value = ($scope.fields.value.field / debtorNumber) + "";
        }

        for (var key in $scope.roommateList) {
            var roommate = $scope.roommateList[key];
            if ($scope.fields[roommate.id].isDebtor) {
                $scope.fields[roommate.id].field = value;
            }
            else {
                $scope.fields[roommate.id].field = "";
            }
        }

    };

    $scope.save = function () {

        for (var key in $scope.fields) {
            var field = $scope.fields[key];
            if ($scope.fields.hasOwnProperty(key)) {
                field.firstAttempt = false;
            }
        }
        if ($scope.allFieldValid()) {

            var listTicketDebtor = [];

            for (var key in $scope.roommateList) {
                var roommate = $scope.roommateList[key];
                if ($scope.fields[roommate.id].isDebtor) {
                    var debtor = {
                        roommateId: roommate.id,
                        value: $scope.fields[roommate.id].field
                    };

                    listTicketDebtor.push(debtor);
                }
            }

            //build dto
            var dto = {
                date: $scope.fields.date.field,
                description: $scope.fields.description.field,
                debtorList: listTicketDebtor,
                payerId: mySelf.id,
                category: $scope.fields.category.field
            }

            var request = "POST";
            var url = '/rest/ticket';
            if (!!ticket) {
                request = 'PUT';
                url += "/" + ticket.id;
            }

            $http({
                'method': request,
                'url': url,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                addItem(data);
                $scope.close();
            })
                .error(function (data, status) {
                    $flash.error(data.message);
                });
        }
    }


});