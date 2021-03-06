myApp.controller('CuTicketCtrl', function ($scope, $http, $flash, $modalInstance, roommateList, ticket, addItem, categoryList,moneySymbol,shoppingItemList,$filter,$modal) {

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

            //build be.flo.roommateService.dto
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
});
myApp.controller('CuShoppingItemCtrl', function ($scope, $http, $flash, $modalInstance, shoppingItem, addItem, data) {

    $scope.onlyForMe = true;
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


            //build be.flo.roommateService.dto


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
});
myApp.controller('ChangeEmailModalCtrl', function ($scope, $http, $flash, $modalInstance,roommate,setEmail) {

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


});
myApp.controller('ChangePasswordModalCtrl', function ($scope, $http, $flash, $modalInstance, roommate) {

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


});
myApp.controller('CalculatorModalCtrl', function ($scope, $modalInstance, setResult) {

    $scope.input = "";
    $scope.memory = "";
    $scope.acceptOperator = false;

    $scope.close = function () {
        $scope.result();
        var value = parseFloat($scope.memory);
        if (!!value) {
            setResult(value);
        }
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

});
myApp.directive("dirFieldDate", function (directiveService, $filter, generateId) {
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
});

myApp.directive("dirFieldText", function (directiveService, $timeout,$modal) {
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
});

myApp.directive("numbersOnly", function($filter, translationService, $locale) {
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
});

myApp.directive("dirUserIcon", function (directiveService, $filter, generateId) {
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
});

myApp.directive("dirFocusMe", function($timeout, $parse) {
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
});

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

myApp.service("$flash", function($filter) {

    Messenger.options = {
        extraClasses: 'messenger-fixed messenger-on-top messenger-on-center cr-messenger',
        theme: 'block'
    }

    this.success = function(key) {
        return Messenger().post({
            message: key,
            type: 'success',
            showCloseButton: true
        });
    };
    this.info = function(key) {
        return Messenger().post({
            message: key,
            type: 'info',
            showCloseButton: true
        });
    };
    this.error = function(key) {
        return Messenger().post({
            message: key,
            type: 'error',
            showCloseButton: true
        });
    };
    this.warning = function(key) {
        return Messenger().post({
            message: key,
            type: 'warning',
            showCloseButton: true
        });
    };
});

myApp.service("directiveService", function($sce) {
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
});

myApp.service("generateId", function($rootScope) {
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
});

myApp.service("translationService", function ($rootScope, $filter, $http,$locale) {
    var svc;
    svc = this;
    svc.elements = null;

    svc.set = function(elements){
        svc.elements = elements.translations;
    };

    svc.get = function (code) {

        var v = svc.elements[code];

        return v;
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
});

myApp.filter("translateText", function ($sce, translationService) {
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
});
