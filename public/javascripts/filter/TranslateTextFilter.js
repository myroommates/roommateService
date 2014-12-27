myApp.filter("translateText", function ($sce, translationService) {
    return function (input, count) {
        var text;
        text = translationService.get(input, count);
        if (text != null) {
            return text;
        }
        return input;
    };
});
