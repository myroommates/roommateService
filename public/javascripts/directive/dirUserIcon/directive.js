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
