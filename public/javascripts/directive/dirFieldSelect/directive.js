myApp.directive("dirFieldSelect", function (directiveService, $timeout,$modal) {
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
});
