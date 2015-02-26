myApp.controller('CreateSurveyModalCtrl', function ($scope, $http, $flash, $modalInstance) {

    $scope.loading = false;

    $scope.fields = {
        questions: {},
        key:{
            fieldTitle: "survey key",
            validationRegex: "^.{1,255}$",
            validationMessage: "generic.validation.size",
            focus: function () {
                return true;
            },
            disabled: function () {
                return $scope.loading;
            }}
    };

    $scope.langs = langs;

    $scope.choices = [];

    //build field

    for (var i in langs) {

        lang = langs[i];

        $scope.fields.questions[lang] = {
            validationRegex: "^.{1,255}$",
            validationMessage: "generic.validation.size",
            disabled: function () {
                return $scope.loading;
            }
        };

    }

    $scope.close = function () {
        $modalInstance.close();
    };

    $scope.addAnswer = function () {

        answers = {};

        for (var i in langs) {

            lang = langs[i];

            answers[lang] = {
                validationRegex: "^.{1,255}$",
                validationMessage: "generic.validation.size",
                disabled: function () {
                    return $scope.loading;
                }
            }

        }

        $scope.choices.push(answers);
    }

    $scope.addAnswer();
    $scope.addAnswer();


    $scope.allFieldValid = function () {


        for (var key in $scope.fields.questions) {
            var obj = $scope.fields.questions[key];
            if (obj.isValid == null || obj.isValid === false) {
                return false;
            }
        }

        for (var key in $scope.choices) {
            var choice = $scope.choices[key];
            for (var key2 in choice) {
                var answer = choice[key2];

                if (answer.isValid == null || answer.isValid === false) {
                    return false;
                }
            }
        }


        return true;
    };

    $scope.save = function () {


        var dto = {
            surveyKey:$scope.fields.key.field,
            questions: {},
            answers:[],
            multipleAnswers:false
        }

        for (var i in $scope.fields.questions) {
            var question = $scope.fields.questions[i];
            dto.questions[i] = question.field;
        }

        for (var key in $scope.choices) {
            var choice = $scope.choices[key];
            var choices = {};
            for (var key2 in choice) {
                var answer = choice[key2];
                choices[key2] = answer.field;
            }

            dto.answers.push(choices);
        }

        console.log(dto);

        $http({
            'method': "POST",
            'url': "/rest/superadmin/survey",
            'headers': "Content-Type:application/json",
            'data': dto
        }).success(function (data, status) {
            $scope.loading = false;
            $scope.close();
        })
            .error(function (data, status) {
                $scope.loading = false;
                $flash.error(data.message);
            });

    }


})
;