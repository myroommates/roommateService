myApp.service("translationService", function ($rootScope, $filter, $http,$locale) {
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
});
