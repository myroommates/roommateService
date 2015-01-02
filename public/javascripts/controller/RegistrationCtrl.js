myApp.controller('RegistrationCtrl',  function ($scope) {

    $scope.nameAbrv="";
    $scope.name="";

    $scope.$watch('name',function(){
       $scope.nameAbrv = $scope.name.slice(0,3);
    });

});