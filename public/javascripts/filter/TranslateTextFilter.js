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
