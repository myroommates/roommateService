
angular.module("tmh.dynamicLocale").config(['tmhDynamicLocaleProvider', function (tmhDynamicLocaleProvider) {
    tmhDynamicLocaleProvider.localeLocationPattern('/assets/components/angular-i18n/angular-locale_{{locale}}.js');
}]);

var myApp = angular.module('app', ['ui.bootstrap.datetimepicker', 'ui.bootstrap', "angucomplete", 'tmh.dynamicLocale']);

myApp.controller('MainCtrl', ['$scope', '$locale', 'tmhDynamicLocale', 'translationService', '$modal', function ($scope,$locale, tmhDynamicLocale,translationService,$modal) {

    if ("data" in window && data!=undefined && data!=null) {
        tmhDynamicLocale.set(data.langId);

        translationService.set(data.translations);
    }

    $scope.helpDisplayed=false;

    $scope.displayHelp = function(){
        $scope.helpDisplayed = true;
    };

    $scope.maskHelp = function(){
        $scope.helpDisplayed = false;
    };

    $scope.openHelp = function(message){

        var resolve = {
            message: function () {
                return message;
            }
        };

        $modal.open({
            templateUrl: "/assets/javascripts/modal/HelpModal/view.html",
            controller: "HelpModalCtrl",
            size: 'sm',
            resolve: resolve
        });
    };
}]);
myApp.controller('HomeCtrl', ['$scope', '$modal', function ($scope, $modal) {


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

        for(var i=$scope.shoppingItemList.length-1;i>=0;i--){

            if (!!$scope.shoppingItemList[i].selected && $scope.shoppingItemList[i].selected == true) {
                $scope.shoppingItemList.splice(i, 1);
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

}]);
myApp.controller('AdminRoommateListCtrl',  ['$scope', '$http', '$flash', 'translationService', '$locale', function ($scope, $http, $flash,translationService,$locale) {

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

}]);
myApp.controller('AdminPreferenceCtrl', ['$scope', '$flash', '$http', function ($scope, $flash, $http) {


    $scope.home = data.home;

    $scope.fields = {
        currency: {
            fieldTitle: "admin.preferences.currency",
            validationRegex: "^.{1,3}$",
            validationMessage: ['generic.validation.size', '1', '255'],
            disabled: function () {
                return $scope.loading;
            }
        }
    };

    $scope.initialize = function () {

        //load old data
        $scope.fields.currency.field = data.home.moneySymbol;
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

    $scope.initialize();

    $scope.save = function () {

        for (var key in $scope.fields) {
            var field = $scope.fields[key];
            if ($scope.fields.hasOwnProperty(key)) {
                field.firstAttempt = false;
            }
        }
        if ($scope.allFieldValid()) {

            //build dto
            var dto = {
                moneySymbol: $scope.fields.currency.field
            };

            $scope.loading = true;

            $http({
                'method': "PUT",
                'url': '/rest/home/' + $scope.home.id,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.loading = false;
            })
                .error(function (data, status) {
                    $scope.loading = false;
                    $flash.error(data.message);
                });
        }
    }

}]);
myApp.controller('LateralMenuCtrl', ['$scope', function ($scope) {

    $scope.itemOpen = [];

    $scope.switchVisibility = function (menuItem) {
        $scope.itemOpen[menuItem] = !$scope.menuItemOpen(menuItem);
    };

    $scope.menuItemOpen = function (menuItem) {
        if ($scope.itemOpen[menuItem] == undefined || $scope.itemOpen[menuItem] == false) {
            return false;
        }
        return true;
    };

}]);
myApp.controller('CountResumeCtrl', ['$scope', '$http', '$flash', '$modal', 'translationService', '$locale', function ($scope, $http, $flash, $modal,translationService,$locale) {

    $scope.ticketList=ticketList.list;
    $scope.moneySymbol= data.home.moneySymbol;

    //sort
    $scope.countResumeList = _.sortBy(countResumeList.list,function(element){
       return element.roommate.id;
    });

    $scope.sub = function(a,b){
        return a - b;
    }

    $scope.totalSpend =function(){
        var total = 0;
        for(var key in $scope.countResumeList){
            total+=$scope.countResumeList[key].spend;
        }
        return total;
    }

    $scope.totalDept = function(){
        var total = 0;
        for(var key in $scope.countResumeList){
            total+=$scope.countResumeList[key].dept;
        }
        return total;
    }


    $scope.computeResult = function(){

        var resultDetailList=[];

        for(var key in $scope.countResumeList){
            var roommate = $scope.countResumeList[key].roommate;
            var resultDetail={
                roommate:roommate,
                payed:0,
                mustPay:0
            };
            for(var key2 in $scope.ticketList){
                var ticket = $scope.ticketList[key2];
                for(var key3 in ticket.debtorList){
                    if(ticket.payerId ==roommate.id ) {
                        resultDetail.payed += ticket.debtorList[key3].value;
                    }
                    if(ticket.debtorList[key3].roommateId == roommate.id){
                        resultDetail.mustPay+= ticket.debtorList[key3].value;
                    }
                }

            }
            resultDetail.resetToReceive = resultDetail.payed - resultDetail.mustPay;
            resultDetailList.push(resultDetail);
        }

        $scope.balanceList = [];

        for(key in resultDetailList){
            var resultDetail = resultDetailList[key];
            var toPay = resultDetail.payed - resultDetail.mustPay;
            if(toPay < 0){
                for(key2 in resultDetailList){
                    var resultDetail2 = resultDetailList[key2];
                    if (resultDetail2.resetToReceive > 0) {
                        var balance = null;
                        for (key3 in  $scope.balanceList) {
                            balanceToFind =$scope.balanceList[key3];
                            if (balanceToFind.from.id ==  resultDetail.roommate.id &&
                                balanceToFind.to.id == resultDetail2.roommate.id) {
                                balance = balanceToFind;
                            }
                        }
                        if (balance == null) {
                            balance = {};
                            balance.from = resultDetail.roommate;
                            balance.to = resultDetail2.roommate;
                            balance.value=0;
                        }
                        $scope.balanceList.push(balance);

                        if (resultDetail2.resetToReceive >= Math.abs(toPay)) {
                            balance.value += Math.abs(toPay);
                            resultDetail2.resetToReceive -= Math.abs(toPay);
                            break;
                        } else {
                            balance.value += resultDetail2.resetToReceive;
                            toPay += resultDetail2.resetToReceive;
                            resultDetail2.resetToReceive = 0;
                        }
                    }
                }
            }

        }
    };

    $scope.computeResult();

}]);
myApp.controller('CountTicketCtrl', ['$scope', '$http', '$flash', '$modal', 'translationService', '$locale', 'tmhDynamicLocale', function ($scope, $http, $flash, $modal,translationService,$locale,tmhDynamicLocale) {

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


}]);
myApp.controller('ProfileMyProfileCtrl', ['$scope', '$http', '$flash', '$modal', 'translationService', '$window', function ($scope, $http, $flash, $modal,translationService,$window) {


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


}]);
myApp.controller('ShoppingListCtrl', ['$scope', '$http', '$flash', '$modal', 'translationService', '$locale', 'tmhDynamicLocale', function ($scope, $http, $flash, $modal,translationService,$locale,tmhDynamicLocale) {

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


}]);
myApp.controller('SuperAdminFaqCtrl', ['$scope', '$http', '$flash', '$modal', 'translationService', '$locale', 'tmhDynamicLocale', function ($scope, $http, $flash, $modal, translationService, $locale, tmhDynamicLocale) {

    $scope.faqs = faqs;
    $scope.langs = langs;
    $scope.surveys=surveys;

    $scope.createNewFaq = function () {

        var resolve = {
            langs: function () {
                return $scope.langs;
            }
        };

        $modal.open({
            templateUrl: "/assets/javascripts/modal/CreateFaq/view.html",
            controller: "CreateFaqModalCtrl",
            size: 'lg',
            resolve: resolve
        });
    };

    $scope.createNewSurvey = function () {

        var resolve = {
            langs: function () {
                return $scope.langs;
            }
        };

        $modal.open({
            templateUrl: "/assets/javascripts/modal/createSurveyModal/view.html",
            controller: "CreateSurveyModalCtrl",
            size: 'lg',
            resolve: resolve
        });
    };

}]);
myApp.controller('RegistrationCtrl',  ['$scope', function ($scope) {

    $scope.nameAbrv="";
    $scope.name="";

    $scope.$watch('name',function(){
       $scope.nameAbrv = $scope.name.slice(0,3);
    });

}]);
myApp.controller('AboutCtrl',  ['$scope', '$modal', function ($scope, $modal) {

    $scope.openCalculator= function(){
        var resolve = {
            setResult: function () {
                return function(result){
                    scope.getInfo().field=result;
                };
            }
        };

        $modal.open({
            templateUrl: "/assets/javascripts/modal/Calculator/view.html",
            controller: "CalculatorModalCtrl",
            size:"sm",
            resolve: resolve
        });
    };

}]);
myApp.controller('WelcomeCtrl',  ['$scope', '$modal', '$window', '$flash', '$http', function ($scope, $modal,$window,$flash,$http) {

    $scope.languages = languages;

    $scope.languagesList = [];

    $scope.lang = lang.split('-')[0];
    $scope.langIni = angular.copy($scope.lang);

    for(var key in $scope.languages){
        if($scope.languages.hasOwnProperty(key)){
            $scope.languagesList.push({
                key:$scope.languages[key].code,
                value:$scope.languages[key].language
            });
        }
    }

    console.log($scope.languagesList);

    $scope.$watch('lang',function(){
        if($scope.lang != $scope.langIni){

            $http({
                'method': "GET",
                'url': "/changeLanguage/"+$scope.lang,
                'headers': "Content-Type:application/json"
            }).success(function (data, status) {
                $window.location.reload();
            })
            .error(function (data, status) {
                $flash.error(data.message);
            });
        }
    });

}]);
myApp.controller('CuTicketCtrl', ['$scope', '$http', '$flash', '$modalInstance', 'roommateList', 'ticket', 'addItem', 'categoryList', 'moneySymbol', 'shoppingItemList', '$filter', '$modal', function ($scope, $http, $flash, $modalInstance, roommateList, ticket, addItem, categoryList,moneySymbol,shoppingItemList,$filter,$modal) {

    $scope.roommateList = roommateList;
    $scope.balanced = true;
    $scope.categoryList = categoryList;
    $scope.loading=false;
    $scope.mySelf = data.mySelf;
    $scope.money=moneySymbol;
    $scope.shoppingItemList=shoppingItemList;

    $scope.fields = {
        description: {
            fieldTitle: "generic.description",
            validationRegex: "[a-zA-Z0-9-]{1,255}",
            validationMessage: ['generic.validation.size', '1', '255'],
            focus: function () {
                return true;
            },
            disabled:function(){
                return $scope.loading;
            }
        },
        date: {
            fieldTitle: "generic.date",
            validationMessage: "generic.validation.date",
            field: new Date(),
            disabled:function(){
                return $scope.loading;
            }
        },
        value: {
            fieldTitle: "generic.total",
            validationRegex: ".+",
            validationMessage: "generic.validation.notNull",
            numbersOnly: "double",
            hasCalculator:true,
            disabled:function(){
                return $scope.loading || !$scope.balanced;
            },
            money:$scope.money
        },
        category: {
            fieldTitle: "generic.category",
            autoCompleteValue: categoryList,
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
            $scope.edit = true;
            $scope.fields.description.field = angular.copy(ticket.description);
            $scope.fields.date.field = angular.copy(ticket.date);
            $scope.fields.category.field = angular.copy(ticket.category);
            var totalValue = 0;
            var ref = ticket.debtorList[0].value;
            for (var key in roommateList) {
                for (var key2 in ticket.debtorList) {

                    var founded = false;

                    if (ticket.debtorList[key2].roommateId == roommateList[key].id) {

                        var debtor = ticket.debtorList[key2];
                        if (debtor.value != ref) {
                            $scope.balanced = false;
                        }
                        totalValue += debtor.value;
                        $scope.fields[debtor.roommateId].field = debtor.value;

                        $scope.fields[debtor.roommateId].disabled = function(){
                            return !$scope.fields[id].isDebtor;
                        };

                        founded = true;

                        break;
                    }
                }
                if (founded == false) {
                    $scope.fields[roommateList[key].id].isDebtor = false;
                }
            }

            $scope.fields.value.field = totalValue;
        }

        if(!!$scope.shoppingItemList){

            //compute name
            var content="",first=true;
            for(key in $scope.shoppingItemList){
                if(first==true){
                    first=false;
                }
                else{
                    content+=", ";
                }
                content+=$scope.shoppingItemList[key].description;
            }

            //compute name
            var description = $filter('translateText')('cuTicket.shopping.description',content);
            if(description.length>1000){
                description=description.splice(0,996) + "...";
            }
            $scope.fields.description.field = description;

            //compute category
            var category = $filter('translateText')('cuTicket.shopping');
            $scope.fields.category.field = category;

            $scope.fields.description.focus=null;
            $scope.fields.value.focus=function () {
                return true;
            };
        }
    };

    $scope.addWatch = function (id) {

        $scope.$watch('fields[' + id + '].isDebtor', function (n, o) {
            computeValues();
        });
        $scope.$watch('fields[' + id + '].field', function (n, o) {
            computeValues();
        });
    };

    $scope.$watch('isLoading', function(){

    });

    $scope.initialize();


    $scope.$watch('fields.value.field', function (n, o) {
        computeValues();
    });

    var computeValues = function () {

        if ($scope.balanced) {

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
        }
        else{
            var total = 0;
            for (var key in $scope.roommateList) {
                var roommate = $scope.roommateList[key];
                if ($scope.fields[roommate.id].isDebtor) {

                    var val = parseFloat($scope.fields[roommate.id].field);
                    if(isNaN(val)==false){
                        total += val;
                    }
                }
            }
            $scope.fields.value.field = total+"";
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
                if ($scope.fields[roommate.id].isDebtor && !!$scope.fields[roommate.id].field) {
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
                payerId: $scope.mySelf.id,
                category: $scope.fields.category.field
            }

            var request = "POST";
            var url = '/rest/ticket';
            if (!!ticket) {
                request = 'PUT';
                url += "/" + ticket.id;
            }
            $scope.loading=true;

            $http({
                'method': request,
                'url': url,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.loading=false;
                addItem(data);
                $scope.close();
            })
            .error(function (data, status) {
                $scope.loading=false;
                $flash.error(data.message);
            });

            if(!!$scope.shoppingItemList){

                var listId = "";

                for(key in $scope.shoppingItemList){
                    listId+=$scope.shoppingItemList[key].id+",";
                }

                $http({
                    'method': "PUT",
                    'url': "/rest/shoppingItem/wasBought/"+listId,
                    'headers': "Content-Type:application/json"
                }).success(function (data, status) {
                })
                .error(function (data, status) {
                    $flash.error(data.message);
                });
            }
        }
    }
}]);
myApp.controller('CuShoppingItemCtrl', ['$scope', '$http', '$flash', '$modalInstance', 'shoppingItem', 'addItem', 'data', function ($scope, $http, $flash, $modalInstance, shoppingItem, addItem, data) {

    $scope.onlyForMe = false;
    $scope.loading = false;
    $scope.mySelf = data.mySelf;

    $scope.fields = {
        description: {
            fieldTitle: "generic.description",
            validationRegex: "[a-zA-Z0-9-]{1,255}",
            validationMessage: ['generic.validation.size', '1', '255'],
            focus: function () {
                return true;
            },
            disabled: function () {
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

    $scope.initialize = function () {

        //load old data
        if (!!shoppingItem) {
            $scope.edit = true;
            $scope.fields.description.field = angular.copy(shoppingItem.description);
            $scope.onlyForMe = angular.copy(shoppingItem.onlyForMe);

        }
    };

    $scope.initialize();

    $scope.save = function () {

        for (var key in $scope.fields) {
            var field = $scope.fields[key];
            if ($scope.fields.hasOwnProperty(key)) {
                field.firstAttempt = false;
            }
        }
        if ($scope.allFieldValid()) {


            //build dto


            var request = "POST";
            var url = '/rest/shoppingItem';
            var dto;
            if (!!shoppingItem) {
                dto = shoppingItem;
                dto.description = $scope.fields.description.field;
                dto.onlyForMe = $scope.onlyForMe;
                request = 'PUT';
                url += "/" + shoppingItem.id;
            }
            else {
                dto = {
                    description: $scope.fields.description.field,
                    onlyForMe: $scope.onlyForMe
                }
            }
            $scope.loading = true;

            $http({
                'method': request,
                'url': url,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.loading = false;
                addItem(data);
                $scope.close();
            })
                .error(function (data, status) {
                    $scope.loading = false;
                    $flash.error(data.message);
                });
        }
    }
}]);
myApp.controller('HelpModalCtrl', ['$scope', '$modalInstance', 'message', function ($scope, $modalInstance,message) {

    $scope.message=message;

    $scope.close = function () {
        $modalInstance.close();
    };

}]);
myApp.controller('ChangeEmailModalCtrl', ['$scope', '$http', '$flash', '$modalInstance', 'roommate', 'setEmail', function ($scope, $http, $flash, $modalInstance,roommate,setEmail) {

    $scope.loading=false;

    $scope.fields = {
        oldPassword: {
            name:'password',
            fieldTitle: "generic.oldPassword",
            validationRegex: "^[a-zA-Z0-9-_%]{6,18}$",
            validationMessage: "generic.validation.password",
            fieldType:'password',
            focus: function(){
                return true;
            },
            disabled:function(){
                return $scope.loading;
            }
        },
        newEmail: {
            fieldType:"email",
            name:'email',
            fieldTitle: "changeEmailModal.email",
            validationRegex: /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
            validationMessage: "generic.validation.email",
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
                oldPassword: $scope.fields.oldPassword.field,
                newEmail: $scope.fields.newEmail.field
            };

            $scope.loading=true;

            $http({
                'method': "PUT",
                'url': "/rest/roommate/email/"+roommate.id,
                'headers': "Content-Type:application/json",
                'data': dto
            }).success(function (data, status) {
                $scope.loading=false;
                $scope.close();
                setEmail(data.email);
            })
            .error(function (data, status) {
                $scope.loading=false;
                $flash.error(data.message);
            });
        }
    }


}]);
myApp.controller('ChangePasswordModalCtrl', ['$scope', '$http', '$flash', '$modalInstance', 'roommate', function ($scope, $http, $flash, $modalInstance, roommate) {

    $scope.loading=false;

    $scope.fields = {
        oldPassword: {
            name:'password',
            fieldTitle: "generic.oldPassword",
            validationRegex: "^[a-zA-Z0-9-_%]{6,18}$",
            validationMessage: "generic.validation.password",
            fieldType: 'password',
            focus: function () {
                return true;
            },
            disabled:function(){
                return $scope.loading;
            }
        },
        newPassword: {
            fieldTitle: "changePasswordModal.newPassword",
            validationRegex: "^[a-zA-Z0-9-_%]{6,18}$",
            validationMessage: "generic.validation.password",
            fieldType: 'password',
            disabled:function(){
                return $scope.loading;
            }
        },
        repeatPassword: {
            fieldTitle: "generic.repeatPassword",
            fieldType: 'password',
            validationMessage: "generic.validation.repeatPassword",
            validation: function () {
                return $scope.o.newPassword === $scope.o.repeatPassword;
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
                oldPassword: $scope.fields.oldPassword.field,
                newPassword: $scope.fields.newPassword.field
            };

            $scope.loading=true;

            $http({
                'method': "PUT",
                'url': "/rest/roommate/password/" + roommate.id,
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
    }


}]);
myApp.controller('CreateFaqModalCtrl', ['$scope', '$http', '$flash', '$modalInstance', function ($scope, $http, $flash, $modalInstance) {

    $scope.loading=false;

    $scope.fields = {
        question:{},
        answer:{}
    };

    $scope.langs = langs;

    //build field
    for (var i in langs) {

        lang = langs[i];

        $scope.fields.question[lang] = {
            fieldTitle: "faq.question",
            validationRegex: "^.{1,255}$",
            validationMessage: "generic.validation.size",
            focus: function () {
                return true;
            },
            disabled:function(){
                return $scope.loading;
            }
        };

        $scope.fields.answer[lang] = {
            fieldTitle: "faq.answer",
            validationRegex: "^.{1,255}$",
            validationMessage: "generic.validation.size",
            disabled:function(){
                return $scope.loading;
            }
        };
    }


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

        faq = {
            questions:{},
            answers:{}
        }

        for (var i in langs) {
            lang = langs[i];
            faq.questions[lang] = $scope.fields.question[lang].field;
            faq.answers[lang] = $scope.fields.answer[lang].field;
        }

        $http({
            'method': "POST",
            'url': "/rest/superadmin/faq",
            'headers': "Content-Type:application/json",
            'data': faq
        }).success(function (data, status) {
            $scope.loading=false;
            $scope.close();
        })
            .error(function (data, status) {
                $scope.loading=false;
                $flash.error(data.message);
            });

    }


}]);
myApp.controller('CreateSurveyModalCtrl', ['$scope', '$http', '$flash', '$modalInstance', function ($scope, $http, $flash, $modalInstance) {

    $scope.loading = false;

    $scope.fields = {
        questions: {},
        key:{
            fieldTitle: "survey key",
            validationRegex: "^.{1,255}$",
            validationMessage: "generic.validation.size",
            focus: function () {
                return true;
            },
            disabled: function () {
                return $scope.loading;
            }}
    };

    $scope.langs = langs;

    $scope.choices = [];

    //build field

    for (var i in langs) {

        lang = langs[i];

        $scope.fields.questions[lang] = {
            validationRegex: "^.{1,255}$",
            validationMessage: "generic.validation.size",
            disabled: function () {
                return $scope.loading;
            }
        };

    }

    $scope.close = function () {
        $modalInstance.close();
    };

    $scope.addAnswer = function () {

        answers = {};

        for (var i in langs) {

            lang = langs[i];

            answers[lang] = {
                validationRegex: "^.{1,255}$",
                validationMessage: "generic.validation.size",
                disabled: function () {
                    return $scope.loading;
                }
            }

        }

        $scope.choices.push(answers);
    }

    $scope.addAnswer();
    $scope.addAnswer();


    $scope.allFieldValid = function () {


        for (var key in $scope.fields.questions) {
            var obj = $scope.fields.questions[key];
            if (obj.isValid == null || obj.isValid === false) {
                return false;
            }
        }

        for (var key in $scope.choices) {
            var choice = $scope.choices[key];
            for (var key2 in choice) {
                var answer = choice[key2];

                if (answer.isValid == null || answer.isValid === false) {
                    return false;
                }
            }
        }


        return true;
    };

    $scope.save = function () {


        var dto = {
            surveyKey:$scope.fields.key.field,
            questions: {},
            answers:[],
            multipleAnswers:false
        }

        for (var i in $scope.fields.questions) {
            var question = $scope.fields.questions[i];
            dto.questions[i] = question.field;
        }

        for (var key in $scope.choices) {
            var choice = $scope.choices[key];
            var choices = {};
            for (var key2 in choice) {
                var answer = choice[key2];
                choices[key2] = answer.field;
            }

            dto.answers.push(choices);
        }

        console.log(dto);

        $http({
            'method': "POST",
            'url': "/rest/superadmin/survey",
            'headers': "Content-Type:application/json",
            'data': dto
        }).success(function (data, status) {
            $scope.loading = false;
            $scope.close();
        })
            .error(function (data, status) {
                $scope.loading = false;
                $flash.error(data.message);
            });

    }


}])
;
myApp.controller('CalculatorModalCtrl', ['$scope', '$modalInstance', 'setResult', function ($scope, $modalInstance, setResult) {

    $scope.input = "";
    $scope.memory = "";
    $scope.acceptOperator = false;

    $scope.valid = function(){
        $scope.result();
        var value = parseFloat($scope.memory);
        if (!!value) {
            setResult(value);
        }
        $scope.close();
    };

    $scope.close = function () {
        $modalInstance.close();
    };

    $scope.press = function (key) {
        $scope.input += key;
    };

    $scope.clear = function () {
        $scope.input = "";
    };

    $scope.clearAll = function () {
        $scope.input = "";
        $scope.memory = "";
    };

    $scope.clearLastOne = function () {
        $scope.input = $scope.input.slice(0, $scope.input.length - 1);
    }

    $scope.operation = function (operator) {
        if (!!parseFloat($scope.input) || !isNaN($scope.memory)) {
            $scope.memory += $scope.input + " ";
            $scope.memory += operator + " ";
            $scope.input = "";
        }
        else if($scope.memory.length>0){
            $scope.memory = $scope.memory.slice(0,$scope.memory.length-2)+operator+" ";
        }
    };

    $scope.result = function () {
        $scope.memory += $scope.input + " ";
        $scope.memory = math.eval($scope.memory);
        $scope.input = "";
    };

    document.onkeypress = function (e) {
        e = e || window.event;

        // 1
        if (e.keyCode == 49) {
            $scope.press(1);
        }// 2
        else if (e.keyCode == 50) {
            $scope.press(2);
        }// 3
        else if (e.keyCode == 51) {
            $scope.press(3);
        }// 4
        else if (e.keyCode == 52) {
            $scope.press(4);
        }// 5
        else if (e.keyCode == 53) {
            $scope.press(5);
        }// 6
        else if (e.keyCode == 54) {
            $scope.press(6);
        }// 7
        else if (e.keyCode == 55) {
            $scope.press(7);
        }// 8
        else if (e.keyCode == 56) {
            $scope.press(8);
        }// 9
        else if (e.keyCode == 57) {
            $scope.press(9);
        }// 0
        else if (e.keyCode == 48) {
            $scope.press(0);
        }// decimal
        else if (e.keyCode == 46 || e.keyCode == 44) {
            $scope.press('.');
        }// +
        else if (e.keyCode == 43) {
            $scope.operation('+');
        }// /
        else if (e.keyCode == 47) {
            $scope.operation('/');
        }// -
        else if (e.keyCode == 48) {
            $scope.operation('-');
        }// *
        else if (e.keyCode == 42) {
            $scope.operation('*');
        }// =
        else if (e.keyCode == 63 || e.keyCode == 13) {
            $scope.result();
        }// delete
        else if (e.keyCode == 127) {
            $scope.clear();
        }

        $scope.$apply();
    };

}]);
myApp.directive("dirFieldDate", ['directiveService', '$filter', 'generateId', function (directiveService, $filter, generateId) {
    return {
        restrict: "E",
        scope: directiveService.autoScope({
            ngInfo: '='
        }),
        templateUrl: "/assets/javascripts/directive/dirFieldDate/template.html",
        replace: true,
        transclude: true,
        compile: function () {
            return {
                pre: function (scope) {
                    scope.id = generateId.generate();
                    return scope.idHtag = '#' + scope.id;
                },
                post: function (scope) {
                    directiveService.autoScopeImpl(scope);
                    scope.result = null;

                    scope.$watch('result', function () {
                        return scope.resultFormated = $filter('date')(scope.result, 'yyyy-MM-dd');
                    });

                    scope.$watch('getInfo().field', function () {
                        if (scope.getInfo().field != null) {
                            return scope.result = new Date(Number(scope.getInfo().field));
                        }
                    });
                    scope.$watch('result', function () {
                        if (scope.result != null) {
                            scope.getInfo().field = scope.result.getTime();
                        } else {
                            scope.getInfo().field = null;
                        }
                        return scope.isValid();
                    });
                    scope.isValid = function () {
                        var isValid;
                        if (scope.getInfo().disabled === true || scope.getInfo().hidden === true) {
                            scope.getInfo().isValid = true;
                            return;
                        }
                        isValid = true;
                        if (scope.getInfo().field == null) {
                            isValid = false;
                        }
                        scope.getInfo().isValid = isValid;
                    };
                    scope.isValid();

                    scope.logField = function () {
                        return console.log(scope.getInfo());
                    };

                    scope.displayError = function () {
                        if (scope.getInfo().isValid == false && scope.getInfo().firstAttempt === false) {
                            return true;
                        }
                        return false;
                    };
                }
            };
        }
    };
}]);

myApp.directive("dirFieldText", ['directiveService', '$timeout', '$modal', function (directiveService, $timeout,$modal) {
    return {
        restrict: "E",
        scope: directiveService.autoScope({
            ngInfo: '='
        }),
        templateUrl: "/assets/javascripts/directive/dirFieldText/template.html",
        replace: true,
        transclude: true,
        compile: function () {
            return {
                pre: function (scope) {
                    return directiveService.autoScopeImpl(scope);
                },
                post: function (scope) {
                    directiveService.autoScopeImpl(scope);

                    if(scope.getInfo().autoCompleteValue==undefined){
                        scope.getInfo().autoCompleteValue=[];
                    }

                    scope.errorMessage = "";
                    scope.isValidationDefined = (scope.getInfo().validationRegex != null) || (scope.getInfo().validationFct != null);
                    scope.hideIsValidIcon = !!scope.getInfo().hideIsValidIcon;
                    scope.fieldType = (scope.getInfo().fieldType != null) ? scope.getInfo().fieldType : "text";

                    if (scope.getInfo().field == null) {
                        scope.getInfo().field = "";
                    }
                    if (scope.getInfo().isValid == null) {
                        scope.getInfo().isValid = !scope.isValidationDefined;
                    }
                    if (scope.isValidationDefined) {
                        scope.$watch('getInfo().field', function (n, o) {
                            if (n !== o) {
                                return scope.isValid();
                            }
                        });
                    }
                    scope.isValid = function () {

                        var isValid;
                        if (scope.getInfo().disabled === true || scope.getInfo().hidden === true) {
                            scope.getInfo().isValid = true;
                            return;
                        }
                        if (!scope.getInfo().field) {
                            scope.getInfo().field = "";
                        }

                        isValid = true;
                        if (typeof scope.getInfo().field !== 'string') {
                            scope.getInfo().field += "";
                        }
                        if (scope.getInfo().validationRegex != null) {
                            isValid = scope.getInfo().field.match(scope.getInfo().validationRegex) != null;
                        }
                        if (scope.getInfo().validationFct != null) {
                            isValid = isValid && scope.getInfo().validationFct();
                        }
                        scope.getInfo().isValid = isValid;
                    };

                    scope.isValid();
                    scope.logField = function () {
                        return console.log(scope.getInfo());
                    };
                    scope.setErrorMessage = function (errorMessage) {
                        scope.errorMessage = errorMessage;
                        if (scope.lastTimeOut != null) {
                            $timeout.cancel(scope.lastTimeOut);
                        }
                        return scope.lastTimeOut = $timeout(function () {
                            scope.errorMessage = "";
                            return scope.lastTimeOut = null;
                        }, 2000);
                    };

                    scope.displayError = function () {
                        if (scope.getInfo().isValid == false && scope.getInfo().firstAttempt === false) {
                            return true;
                        }
                        return false;
                    };


                    scope.openCalculator= function(){
                        var resolve = {
                            setResult: function () {
                                return function(result){
                                    scope.getInfo().field=result;
                                };
                            }
                        };

                        $modal.open({
                            templateUrl: "/assets/javascripts/modal/Calculator/view.html",
                            controller: "CalculatorModalCtrl",
                            size:"sm",
                            resolve: resolve
                        });
                    };

                }
            };
        }
    };
}]);

myApp.directive("dirFieldSelect", ['directiveService', '$timeout', '$modal', function (directiveService, $timeout,$modal) {
    return {
        restrict: "E",
        scope: directiveService.autoScope({
            ngInfo: '='
        }),
        templateUrl: "/assets/javascripts/directive/dirFieldSelect/template.html",
        replace: true,
        transclude: true,
        compile: function () {
            return {
                pre: function (scope) {
                    return directiveService.autoScopeImpl(scope);
                },
                post: function (scope) {


                    console.log(scope.getInfo().options);



                    directiveService.autoScopeImpl(scope);

                    if(scope.getInfo().autoCompleteValue==undefined){
                        scope.getInfo().autoCompleteValue=[];
                    }

                    scope.errorMessage = "";
                    scope.isValidationDefined = (scope.getInfo().validationRegex != null) || (scope.getInfo().validationFct != null);
                    scope.hideIsValidIcon = !!scope.getInfo().hideIsValidIcon;
                    scope.fieldType = (scope.getInfo().fieldType != null) ? scope.getInfo().fieldType : "text";

                    if (scope.getInfo().field == null) {
                        scope.getInfo().field = "";
                    }
                    if (scope.getInfo().isValid == null) {
                        scope.getInfo().isValid = !scope.isValidationDefined;
                    }
                    if (scope.isValidationDefined) {
                        scope.$watch('getInfo().field', function (n, o) {
                            if (n !== o) {
                                return scope.isValid();
                            }
                        });
                    }
                    scope.isValid = function () {

                        var isValid;
                        if (scope.getInfo().disabled === true || scope.getInfo().hidden === true) {
                            scope.getInfo().isValid = true;
                            return;
                        }
                        if (!scope.getInfo().field) {
                            scope.getInfo().field = "";
                        }

                        isValid = true;
                        if (typeof scope.getInfo().field !== 'string') {
                            scope.getInfo().field += "";
                        }
                        if (scope.getInfo().validationRegex != null) {
                            isValid = scope.getInfo().field.match(scope.getInfo().validationRegex) != null;
                        }
                        if (scope.getInfo().validationFct != null) {
                            isValid = isValid && scope.getInfo().validationFct();
                        }
                        scope.getInfo().isValid = isValid;
                    };

                    scope.isValid();
                    scope.logField = function () {
                        return console.log(scope.getInfo());
                    };
                    scope.setErrorMessage = function (errorMessage) {
                        scope.errorMessage = errorMessage;
                        if (scope.lastTimeOut != null) {
                            $timeout.cancel(scope.lastTimeOut);
                        }
                        return scope.lastTimeOut = $timeout(function () {
                            scope.errorMessage = "";
                            return scope.lastTimeOut = null;
                        }, 2000);
                    };

                    scope.displayError = function () {
                        if (scope.getInfo().isValid == false && scope.getInfo().firstAttempt === false) {
                            return true;
                        }
                        return false;
                    };


                    scope.openCalculator= function(){
                        var resolve = {
                            setResult: function () {
                                return function(result){
                                    scope.getInfo().field=result;
                                };
                            }
                        };

                        $modal.open({
                            templateUrl: "/assets/javascripts/modal/Calculator/view.html",
                            controller: "CalculatorModalCtrl",
                            size:"sm",
                            resolve: resolve
                        });
                    };

                }
            };
        }
    };
}]);

myApp.directive("numbersOnly", ['$filter', 'translationService', '$locale', function($filter, translationService, $locale) {
    return {
        restrict: 'A',
        require: "ngModel",
        link: function(scope, element, attrs, modelCtrl) {
            return scope.$watch(attrs.numbersOnly, function() {

                var convertToFloat, convertToString, displayError, errorMessage, filterFloat, nbDecimal, valueToDisplay;
                if (attrs.numbersOnly === "integer" || attrs.numbersOnly === "double" || attrs.numbersOnly === "percent") {
                    scope.lastValidValue = 0;
                    if (attrs.numbersOnly === "integer") {
                        errorMessage = $filter('translateText')('ONLY_INTEGER');
                        nbDecimal = 0;
                    } else {
                        errorMessage = $filter('translateText')('ONLY_NUMBER');
                        nbDecimal = 2;
                    }
                    scope.$root.$on('$localeChangeSuccess', function(event, current, previous) {
                        var result;
                        if (modelCtrl.$modelValue != null) {
                            result = convertToString(parseFloat(modelCtrl.$modelValue));
                            if (result != null) {
                                modelCtrl.$setViewValue(result.toString());
                                return modelCtrl.$render();
                            }
                        }
                    });
                    modelCtrl.$parsers.unshift(function(viewValue) {
                        var result, resultString, resultToDisplay;
                        if (viewValue === "") {
                            return null;
                        }

                        result = convertToFloat(viewValue);
                        if (isNaN(result)) {
                             displayError();
                            if (!!scope.lastValidValue) {
                                resultString = scope.lastValidValue.toString();
                                if (attrs.numbersOnly === "percent") {
                                    resultToDisplay = (scope.lastValidValue * 100).toString();
                                } else {
                                    resultToDisplay = scope.lastValidValue.toString();
                                }
                            } else {
                                resultString = "";
                                resultToDisplay = "";
                            }
                            modelCtrl.$setViewValue(resultToDisplay);
                            modelCtrl.$render();
                        } else {
                            if (attrs.numbersOnly === "percent") {
                                result = result / 100;
                            }
                            scope.lastValidValue = result;
                            resultString = result.toString();
                        }
                        if (resultString === "") {
                            return null;
                        }
                        return resultString;

                    });
                    modelCtrl.$formatters.unshift(function(modelValue) {
                        return scope.displayValue(modelValue);
                    });
                    scope.displayValue = function(modelValue) {
                        var result;
                        result = parseFloat(modelValue);
                        if (attrs.numbersOnly === "percent") {
                            result = result * 100;
                        }
                        return convertToString(result);
                    };
                    displayError = function() {
                        if (scope.setErrorMessage != null) {
                            return scope.setErrorMessage(errorMessage);
                        }
                    };
                    convertToString = function(value) {
                        var formats, result;
                        if ((value == null) || isNaN(value)) {
                            return "";
                        }
                        value = value.toFixed(nbDecimal);
                        formats = $locale.NUMBER_FORMATS;
                        return result = value.toString().replace(new RegExp("\\.", "g"), formats.DECIMAL_SEP);
                    };
                    convertToFloat = function(viewValue) {
                        var decimalRegex, formats, value;
                        if (viewValue === "") {
                            return NaN;
                        }
                        formats = $locale.NUMBER_FORMATS;
                        decimalRegex = formats.DECIMAL_SEP;
                        if (decimalRegex === ".") {
                            decimalRegex = "\\.";
                        }
                        value = viewValue.replace(new RegExp(decimalRegex, "g"), ".");

                        return filterFloat(value);
                    };
                    filterFloat = function(value) {
                        var regexFloat;
                        if (value.isNaN) {
                            return NaN;
                        }
                        if (attrs.numbersOnly === "integer") {
                            regexFloat = new RegExp("^(\\-|\\+)?([0-9]+|Infinity)?$");
                        } else {
                            regexFloat = new RegExp("^(\\-|\\+)?([0-9]+(\\.[0-9]*)?|Infinity)?$");
                        }
                        if (regexFloat.test(value)) {
                            return Number(value);
                        }
                        return NaN;
                    };
                    if (modelCtrl.$modelValue != null) {
                        scope.lastValidValue = parseFloat(modelCtrl.$modelValue);
                        valueToDisplay = scope.displayValue(scope.lastValidValue);
                        modelCtrl.$setViewValue(valueToDisplay);
                        return modelCtrl.$render();
                    }
                }
            });
        }
    };
}]);

myApp.directive("dirUserIcon", ['directiveService', '$filter', 'generateId', function (directiveService, $filter, generateId) {
    return {
        restrict: "E",
        scope: directiveService.autoScope({
            ngUser: '=',
            ngLittle: '='
        }),
        templateUrl: "/assets/javascripts/directive/dirUserIcon/template.html",
        replace: true,
        transclude: true,
        link: function (scope) {
            directiveService.autoScopeImpl(scope);

            scope.getName = function(){
                return scope.getUser().nameAbrv;
            }

            scope.getColor = function(){
                return scope.getUser().iconColor;
            }

        }
    };
}]);

myApp.directive("dirFocusMe", ['$timeout', '$parse', function($timeout, $parse) {
    return {
        restrict: 'A',
        scope: {
            dirFocusMe: '='
        },
        link: function(scope, element, attrs) {
            scope.$watch('dirFocusMe', function() {
                if (scope.dirFocusMe === true) {
                    return element[0].focus();
                }
            });
        }
    };
}]);

myApp.directive("dirEnter", function() {
    return function(scope, element, attrs) {
        return element.bind("keydown keypress", function(event) {
            if (event.which === 13) {
                scope.$apply(function() {
                    return scope.$eval(attrs.dirEnter);
                });
                return event.preventDefault();
            }
        });
    };
});

myApp.service("$flash", ['$filter', function($filter) {

    Messenger.options = {
        extraClasses: 'messenger-fixed messenger-on-top messenger-on-center cr-messenger',
        theme: 'block'
    }

    this.success = function(messages) {
        for(var key in messages.split("\n")){
            var message = messages.split("\n")[key];

            Messenger().post({
                message: message,
                type: 'success',
                showCloseButton: true
            });
        }

        return;
    };
    this.info = function(messages) {
        for(var key in messages.split("\n")){
            var message = messages.split("\n")[key];

            Messenger().post({
                message: message,
                type: 'info',
                showCloseButton: true
            });
        }
    };
    this.error = function(messages) {
        for(var key in messages.split("\n")){
            var message = messages.split("\n")[key];

            Messenger().post({
                message: message,
                type: 'error',
                showCloseButton: true
            });
        }

        return;

    };
    this.warning = function(messages) {
        for(var key in messages.split("\n")){
            var message = messages.split("\n")[key];

            Messenger().post({
                message: message,
                type: 'warning',
                showCloseButton: true
            });
        }

        return;
    };
}]);

myApp.service("directiveService", ['$sce', function($sce) {
    this.autoScope = function(s) {
        var k, res, v;
        res = {};
        for (k in s) {
            v = s[k];
            res[k] = v;
            if (k.slice(0, 2) === 'ng' && v === '=') {
                res[k[2].toLowerCase() + k.slice(3)] = '@';
            }
        }
        return res;
    };
    this.autoScopeImpl = function(s, name) {
        var fget, key, val;
        s.$$NAME = name;
        for (key in s) {
            val = s[key];
            if (key.slice(0, 2) === 'ng') {
                fget = function(scope, k) {
                    return function() {
                        var v;
                        v = 0;
                        if (scope[k] === void 0 || scope[k] === null || scope[k] === '') {
                            v = scope[k[2].toLowerCase() + k.slice(3)];
                        } else {
                            v = scope[k];
                        }
                        if (scope['decorate' + k.slice(2)]) {
                            return scope['decorate' + k.slice(2)](v);
                        } else {
                            return v;
                        }
                    };
                };
                s['get' + key.slice(2)] = fget(s, key);
            }
        }
        s.isTrue = function(v) {
            return v === true || v === 'true' || v === 'y';
        };
        s.isFalse = function(v) {
            return v === false || v === 'false' || v === 'n';
        };
        s.isNull = function(v) {
            return v === null;
        };
        return s.html = function(v) {
            return $sce.trustAsHtml(v);
        };
    };
}]);

myApp.service("generateId", ['$rootScope', function($rootScope) {
    this.generate = function() {
        var i, possible, text;
        text = "";
        possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        i = 0;
        while (i < 20) {
            text += possible.charAt(Math.floor(Math.random() * possible.length));
            i++;
        }
        return text;
    };
}]);

myApp.service("translationService", ['$rootScope', '$filter', '$http', '$locale', function ($rootScope, $filter, $http,$locale) {
    var svc;
    svc = this;
    svc.elements = null;

    svc.set = function(elements){
        svc.elements = elements.translations;
    };

    svc.get = function (code) {

        if(!!svc.elements[code]){
            return svc.elements[code];
        }
        return code;
    };

    svc.translateExceptionsDTO = function (exception) {
        if ((exception.params != null) && Object.keys(exception.params).length > 0) {
            return $filter('translateTextWithVars')(exception.messageToTranslate, exception.params);
        } else if (exception.messageToTranslate != null) {
            return $filter('translate')(exception.messageToTranslate);
        } else {
            return exception.message;
        }
    };
}]);

myApp.filter("translateText", ['$sce', 'translationService', function ($sce, translationService) {
    return function (input, params) {
        var text;

        if (typeof input === 'object') {
            text = translationService.get(input[0]);
            for (var key in input) {
                if (key != 0) {
                    text = text.replace('{' + (parseFloat(key) -1) + '}', input[key]);
                }
            }
            return text;
        }
        else {
            text = translationService.get(input);

            if (params != null) {
                if (typeof params === 'array') {
                    for (var key in params) {
                        text = text.replace('{' + key + '}', params[key]);
                    }
                } else {
                    text = text.replace('{0}', params);
                }
            }

            return text;
        }
        return input;
    };
}]);

(function(e){var o="left",n="right",d="up",v="down",c="in",w="out",l="none",r="auto",k="swipe",s="pinch",x="tap",i="doubletap",b="longtap",A="horizontal",t="vertical",h="all",q=10,f="start",j="move",g="end",p="cancel",a="ontouchstart" in window,y="TouchSwipe";var m={fingers:1,threshold:75,cancelThreshold:null,pinchThreshold:20,maxTimeThreshold:null,fingerReleaseThreshold:250,longTapThreshold:500,doubleTapThreshold:200,swipe:null,swipeLeft:null,swipeRight:null,swipeUp:null,swipeDown:null,swipeStatus:null,pinchIn:null,pinchOut:null,pinchStatus:null,click:null,tap:null,doubleTap:null,longTap:null,triggerOnTouchEnd:true,triggerOnTouchLeave:false,allowPageScroll:"auto",fallbackToMouseEvents:true,excludedElements:"button, input, select, textarea, a, .noSwipe"};e.fn.swipe=function(D){var C=e(this),B=C.data(y);if(B&&typeof D==="string"){if(B[D]){return B[D].apply(this,Array.prototype.slice.call(arguments,1))}else{e.error("Method "+D+" does not exist on jQuery.swipe")}}else{if(!B&&(typeof D==="object"||!D)){return u.apply(this,arguments)}}return C};e.fn.swipe.defaults=m;e.fn.swipe.phases={PHASE_START:f,PHASE_MOVE:j,PHASE_END:g,PHASE_CANCEL:p};e.fn.swipe.directions={LEFT:o,RIGHT:n,UP:d,DOWN:v,IN:c,OUT:w};e.fn.swipe.pageScroll={NONE:l,HORIZONTAL:A,VERTICAL:t,AUTO:r};e.fn.swipe.fingers={ONE:1,TWO:2,THREE:3,ALL:h};function u(B){if(B&&(B.allowPageScroll===undefined&&(B.swipe!==undefined||B.swipeStatus!==undefined))){B.allowPageScroll=l}if(B.click!==undefined&&B.tap===undefined){B.tap=B.click}if(!B){B={}}B=e.extend({},e.fn.swipe.defaults,B);return this.each(function(){var D=e(this);var C=D.data(y);if(!C){C=new z(this,B);D.data(y,C)}})}function z(a0,aq){var av=(a||!aq.fallbackToMouseEvents),G=av?"touchstart":"mousedown",au=av?"touchmove":"mousemove",R=av?"touchend":"mouseup",P=av?null:"mouseleave",az="touchcancel";var ac=0,aL=null,Y=0,aX=0,aV=0,D=1,am=0,aF=0,J=null;var aN=e(a0);var W="start";var T=0;var aM=null;var Q=0,aY=0,a1=0,aa=0,K=0;var aS=null;try{aN.bind(G,aJ);aN.bind(az,a5)}catch(ag){e.error("events not supported "+G+","+az+" on jQuery.swipe")}this.enable=function(){aN.bind(G,aJ);aN.bind(az,a5);return aN};this.disable=function(){aG();return aN};this.destroy=function(){aG();aN.data(y,null);return aN};this.option=function(a8,a7){if(aq[a8]!==undefined){if(a7===undefined){return aq[a8]}else{aq[a8]=a7}}else{e.error("Option "+a8+" does not exist on jQuery.swipe.options")}};function aJ(a9){if(ax()){return}if(e(a9.target).closest(aq.excludedElements,aN).length>0){return}var ba=a9.originalEvent?a9.originalEvent:a9;var a8,a7=a?ba.touches[0]:ba;W=f;if(a){T=ba.touches.length}else{a9.preventDefault()}ac=0;aL=null;aF=null;Y=0;aX=0;aV=0;D=1;am=0;aM=af();J=X();O();if(!a||(T===aq.fingers||aq.fingers===h)||aT()){ae(0,a7);Q=ao();if(T==2){ae(1,ba.touches[1]);aX=aV=ap(aM[0].start,aM[1].start)}if(aq.swipeStatus||aq.pinchStatus){a8=L(ba,W)}}else{a8=false}if(a8===false){W=p;L(ba,W);return a8}else{ak(true)}}function aZ(ba){var bd=ba.originalEvent?ba.originalEvent:ba;if(W===g||W===p||ai()){return}var a9,a8=a?bd.touches[0]:bd;var bb=aD(a8);aY=ao();if(a){T=bd.touches.length}W=j;if(T==2){if(aX==0){ae(1,bd.touches[1]);aX=aV=ap(aM[0].start,aM[1].start)}else{aD(bd.touches[1]);aV=ap(aM[0].end,aM[1].end);aF=an(aM[0].end,aM[1].end)}D=a3(aX,aV);am=Math.abs(aX-aV)}if((T===aq.fingers||aq.fingers===h)||!a||aT()){aL=aH(bb.start,bb.end);ah(ba,aL);ac=aO(bb.start,bb.end);Y=aI();aE(aL,ac);if(aq.swipeStatus||aq.pinchStatus){a9=L(bd,W)}if(!aq.triggerOnTouchEnd||aq.triggerOnTouchLeave){var a7=true;if(aq.triggerOnTouchLeave){var bc=aU(this);a7=B(bb.end,bc)}if(!aq.triggerOnTouchEnd&&a7){W=ay(j)}else{if(aq.triggerOnTouchLeave&&!a7){W=ay(g)}}if(W==p||W==g){L(bd,W)}}}else{W=p;L(bd,W)}if(a9===false){W=p;L(bd,W)}}function I(a7){var a8=a7.originalEvent;if(a){if(a8.touches.length>0){C();return true}}if(ai()){T=aa}a7.preventDefault();aY=ao();Y=aI();if(a6()){W=p;L(a8,W)}else{if(aq.triggerOnTouchEnd||(aq.triggerOnTouchEnd==false&&W===j)){W=g;L(a8,W)}else{if(!aq.triggerOnTouchEnd&&a2()){W=g;aB(a8,W,x)}else{if(W===j){W=p;L(a8,W)}}}}ak(false)}function a5(){T=0;aY=0;Q=0;aX=0;aV=0;D=1;O();ak(false)}function H(a7){var a8=a7.originalEvent;if(aq.triggerOnTouchLeave){W=ay(g);L(a8,W)}}function aG(){aN.unbind(G,aJ);aN.unbind(az,a5);aN.unbind(au,aZ);aN.unbind(R,I);if(P){aN.unbind(P,H)}ak(false)}function ay(bb){var ba=bb;var a9=aw();var a8=aj();var a7=a6();if(!a9||a7){ba=p}else{if(a8&&bb==j&&(!aq.triggerOnTouchEnd||aq.triggerOnTouchLeave)){ba=g}else{if(!a8&&bb==g&&aq.triggerOnTouchLeave){ba=p}}}return ba}function L(a9,a7){var a8=undefined;if(F()||S()){a8=aB(a9,a7,k)}else{if((M()||aT())&&a8!==false){a8=aB(a9,a7,s)}}if(aC()&&a8!==false){a8=aB(a9,a7,i)}else{if(al()&&a8!==false){a8=aB(a9,a7,b)}else{if(ad()&&a8!==false){a8=aB(a9,a7,x)}}}if(a7===p){a5(a9)}if(a7===g){if(a){if(a9.touches.length==0){a5(a9)}}else{a5(a9)}}return a8}function aB(ba,a7,a9){var a8=undefined;if(a9==k){aN.trigger("swipeStatus",[a7,aL||null,ac||0,Y||0,T]);if(aq.swipeStatus){a8=aq.swipeStatus.call(aN,ba,a7,aL||null,ac||0,Y||0,T);if(a8===false){return false}}if(a7==g&&aR()){aN.trigger("swipe",[aL,ac,Y,T]);if(aq.swipe){a8=aq.swipe.call(aN,ba,aL,ac,Y,T);if(a8===false){return false}}switch(aL){case o:aN.trigger("swipeLeft",[aL,ac,Y,T]);if(aq.swipeLeft){a8=aq.swipeLeft.call(aN,ba,aL,ac,Y,T)}break;case n:aN.trigger("swipeRight",[aL,ac,Y,T]);if(aq.swipeRight){a8=aq.swipeRight.call(aN,ba,aL,ac,Y,T)}break;case d:aN.trigger("swipeUp",[aL,ac,Y,T]);if(aq.swipeUp){a8=aq.swipeUp.call(aN,ba,aL,ac,Y,T)}break;case v:aN.trigger("swipeDown",[aL,ac,Y,T]);if(aq.swipeDown){a8=aq.swipeDown.call(aN,ba,aL,ac,Y,T)}break}}}if(a9==s){aN.trigger("pinchStatus",[a7,aF||null,am||0,Y||0,T,D]);if(aq.pinchStatus){a8=aq.pinchStatus.call(aN,ba,a7,aF||null,am||0,Y||0,T,D);if(a8===false){return false}}if(a7==g&&a4()){switch(aF){case c:aN.trigger("pinchIn",[aF||null,am||0,Y||0,T,D]);if(aq.pinchIn){a8=aq.pinchIn.call(aN,ba,aF||null,am||0,Y||0,T,D)}break;case w:aN.trigger("pinchOut",[aF||null,am||0,Y||0,T,D]);if(aq.pinchOut){a8=aq.pinchOut.call(aN,ba,aF||null,am||0,Y||0,T,D)}break}}}if(a9==x){if(a7===p||a7===g){clearTimeout(aS);if(V()&&!E()){K=ao();aS=setTimeout(e.proxy(function(){K=null;aN.trigger("tap",[ba.target]);if(aq.tap){a8=aq.tap.call(aN,ba,ba.target)}},this),aq.doubleTapThreshold)}else{K=null;aN.trigger("tap",[ba.target]);if(aq.tap){a8=aq.tap.call(aN,ba,ba.target)}}}}else{if(a9==i){if(a7===p||a7===g){clearTimeout(aS);K=null;aN.trigger("doubletap",[ba.target]);if(aq.doubleTap){a8=aq.doubleTap.call(aN,ba,ba.target)}}}else{if(a9==b){if(a7===p||a7===g){clearTimeout(aS);K=null;aN.trigger("longtap",[ba.target]);if(aq.longTap){a8=aq.longTap.call(aN,ba,ba.target)}}}}}return a8}function aj(){var a7=true;if(aq.threshold!==null){a7=ac>=aq.threshold}return a7}function a6(){var a7=false;if(aq.cancelThreshold!==null&&aL!==null){a7=(aP(aL)-ac)>=aq.cancelThreshold}return a7}function ab(){if(aq.pinchThreshold!==null){return am>=aq.pinchThreshold}return true}function aw(){var a7;if(aq.maxTimeThreshold){if(Y>=aq.maxTimeThreshold){a7=false}else{a7=true}}else{a7=true}return a7}function ah(a7,a8){if(aq.allowPageScroll===l||aT()){a7.preventDefault()}else{var a9=aq.allowPageScroll===r;switch(a8){case o:if((aq.swipeLeft&&a9)||(!a9&&aq.allowPageScroll!=A)){a7.preventDefault()}break;case n:if((aq.swipeRight&&a9)||(!a9&&aq.allowPageScroll!=A)){a7.preventDefault()}break;case d:if((aq.swipeUp&&a9)||(!a9&&aq.allowPageScroll!=t)){a7.preventDefault()}break;case v:if((aq.swipeDown&&a9)||(!a9&&aq.allowPageScroll!=t)){a7.preventDefault()}break}}}function a4(){var a8=aK();var a7=U();var a9=ab();return a8&&a7&&a9}function aT(){return !!(aq.pinchStatus||aq.pinchIn||aq.pinchOut)}function M(){return !!(a4()&&aT())}function aR(){var ba=aw();var bc=aj();var a9=aK();var a7=U();var a8=a6();var bb=!a8&&a7&&a9&&bc&&ba;return bb}function S(){return !!(aq.swipe||aq.swipeStatus||aq.swipeLeft||aq.swipeRight||aq.swipeUp||aq.swipeDown)}function F(){return !!(aR()&&S())}function aK(){return((T===aq.fingers||aq.fingers===h)||!a)}function U(){return aM[0].end.x!==0}function a2(){return !!(aq.tap)}function V(){return !!(aq.doubleTap)}function aQ(){return !!(aq.longTap)}function N(){if(K==null){return false}var a7=ao();return(V()&&((a7-K)<=aq.doubleTapThreshold))}function E(){return N()}function at(){return((T===1||!a)&&(isNaN(ac)||ac===0))}function aW(){return((Y>aq.longTapThreshold)&&(ac<q))}function ad(){return !!(at()&&a2())}function aC(){return !!(N()&&V())}function al(){return !!(aW()&&aQ())}function C(){a1=ao();aa=event.touches.length+1}function O(){a1=0;aa=0}function ai(){var a7=false;if(a1){var a8=ao()-a1;if(a8<=aq.fingerReleaseThreshold){a7=true}}return a7}function ax(){return !!(aN.data(y+"_intouch")===true)}function ak(a7){if(a7===true){aN.bind(au,aZ);aN.bind(R,I);if(P){aN.bind(P,H)}}else{aN.unbind(au,aZ,false);aN.unbind(R,I,false);if(P){aN.unbind(P,H,false)}}aN.data(y+"_intouch",a7===true)}function ae(a8,a7){var a9=a7.identifier!==undefined?a7.identifier:0;aM[a8].identifier=a9;aM[a8].start.x=aM[a8].end.x=a7.pageX||a7.clientX;aM[a8].start.y=aM[a8].end.y=a7.pageY||a7.clientY;return aM[a8]}function aD(a7){var a9=a7.identifier!==undefined?a7.identifier:0;var a8=Z(a9);a8.end.x=a7.pageX||a7.clientX;a8.end.y=a7.pageY||a7.clientY;return a8}function Z(a8){for(var a7=0;a7<aM.length;a7++){if(aM[a7].identifier==a8){return aM[a7]}}}function af(){var a7=[];for(var a8=0;a8<=5;a8++){a7.push({start:{x:0,y:0},end:{x:0,y:0},identifier:0})}return a7}function aE(a7,a8){a8=Math.max(a8,aP(a7));J[a7].distance=a8}function aP(a7){return J[a7].distance}function X(){var a7={};a7[o]=ar(o);a7[n]=ar(n);a7[d]=ar(d);a7[v]=ar(v);return a7}function ar(a7){return{direction:a7,distance:0}}function aI(){return aY-Q}function ap(ba,a9){var a8=Math.abs(ba.x-a9.x);var a7=Math.abs(ba.y-a9.y);return Math.round(Math.sqrt(a8*a8+a7*a7))}function a3(a7,a8){var a9=(a8/a7)*1;return a9.toFixed(2)}function an(){if(D<1){return w}else{return c}}function aO(a8,a7){return Math.round(Math.sqrt(Math.pow(a7.x-a8.x,2)+Math.pow(a7.y-a8.y,2)))}function aA(ba,a8){var a7=ba.x-a8.x;var bc=a8.y-ba.y;var a9=Math.atan2(bc,a7);var bb=Math.round(a9*180/Math.PI);if(bb<0){bb=360-Math.abs(bb)}return bb}function aH(a8,a7){var a9=aA(a8,a7);if((a9<=45)&&(a9>=0)){return o}else{if((a9<=360)&&(a9>=315)){return o}else{if((a9>=135)&&(a9<=225)){return n}else{if((a9>45)&&(a9<135)){return v}else{return d}}}}}function ao(){var a7=new Date();return a7.getTime()}function aU(a7){a7=e(a7);var a9=a7.offset();var a8={left:a9.left,right:a9.left+a7.outerWidth(),top:a9.top,bottom:a9.top+a7.outerHeight()};return a8}function B(a7,a8){return(a7.x>a8.left&&a7.x<a8.right&&a7.y>a8.top&&a7.y<a8.bottom)}}})(jQuery);
$(document).ready(function() {


    var header_height = 50;
    $("#sidebar-toggle").click(function() {
        $(".global-container").toggleClass("open-sidebar");
    });


    $(".swipe-area").swipe({
        swipeStatus:function(event, phase, direction, distance, duration, fingers)
        {

            if (phase=="move" && direction =="right") {
                $(".global-container").addClass("open-sidebar");
                return false;
            }
            if (phase=="move" && direction =="left") {
                $(".global-container").removeClass("open-sidebar");
                return false;
            }
        }
    });

    //initialization
    $(".large-menu").css("width", $(".main-container").width());

    $(window).resize(function () {
        $(".large-menu").css("width", $(".main-container").width());
        if($(window).width()>992 && $(".global-container").hasClass("open-sidebar")){
            $(".global-container").removeClass("open-sidebar");
        }
    });

    $(".main-body").scroll(function () {
        if ($(".main-body").scrollTop() > header_height) {
            $(".large-menu").css("position", "fixed");
            $(".large-menu").css("top", "0");
        }
        else {
            $(".large-menu").css("position", "relative");
        }
    });

});
