myApp.service("translationService", function ($rootScope, $filter, $http,$locale) {
    var svc;
    svc = this;
    svc.elements = null;

    svc.set = function(elements){
        svc.elements = elements;
    };

    svc.get = function (code) {
        var lang = $locale.id.split('-')[0];

        var txt, v;
        if (svc.elements == null) {
            return "";
        }
        v = svc.elements[code];
        if (v == null) {
            return null;
        }
        txt = v.fallback || '';
        return txt;
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
