myApp.controller('CreateSurveyModalCtrl', function ($scope, $http, $flash, $modalInstance) {

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
            validationRegex: "^[a-zA-Z0-9-_%]{6,18}$",
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


});