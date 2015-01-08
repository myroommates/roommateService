myApp.controller('WelcomeCtrl',  function ($scope, $modal,$window,$flash,$http) {

    $scope.languages = languages;

    $scope.languagesList = [];

    $scope.lang = lang.split('-')[0];
    $scope.langIni = angular.copy($scope.lang);

    for(var key in $scope.languages){
        if($scope.languages.hasOwnProperty(key)){
            $scope.languagesList.push({
                key:$scope.languages[key].code,
                value:$scope.languages[key].language
            });
        }
    }

    console.log($scope.languagesList);

    $scope.$watch('lang',function(){
        if($scope.lang != $scope.langIni){

            $http({
                'method': "GET",
                'url': "/changeLanguage/"+$scope.lang,
                'headers': "Content-Type:application/json"
            }).success(function (data, status) {
                $window.location.reload();
            })
            .error(function (data, status) {
                $flash.error(data.message);
            });
        }
    });

});