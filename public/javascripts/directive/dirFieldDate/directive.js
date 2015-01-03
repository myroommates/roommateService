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
