myApp.service("translationService", function ($rootScope, $filter, $http) {
    var svc;
    svc = this;
    svc.elements = null;
    svc.lang = null;
    svc.reinitialize = function () {
        return svc.initialize(svc.lang);
    };
    svc.initialize = function (lang) {
        console.log("translationService.initialize('" + lang + "')");
        $http({
            method: "GET",
            url: "/awac/translations/" + lang,
            headers: {
                "Content-Type": "application/json"
            }
        }).success(function (data, status, headers, config) {
            svc.elements = data.lines;
            svc.lang = lang;
            return $rootScope.$broadcast("LOAD_FINISHED", {
                type: "TRANSLATIONS",
                success: true
            });
        }).error(function (data, status, headers, config) {
            svc.elements = [];
            svc.lang = null;
            return $rootScope.$broadcast("LOAD_FINISHED", {
                type: "TRANSLATIONS",
                success: false
            });
        });
    };
    svc.get = function (code, count) {
        var txt, v;
        if (svc.elements == null) {
            return "";
        }
        v = svc.elements[code];
        if (v == null) {
            return null;
        }
        if (count != null) {
            if ("" + count === "0") {
                txt = v.zero || v.fallback;
            } else if ("" + count === "1") {
                txt = v.one || v.fallback;
            } else {
                txt = v.more || v.fallback;
            }
            txt = txt.replace(/\{0\}/g, count);
        } else {
            txt = v.fallback || '';
        }
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
