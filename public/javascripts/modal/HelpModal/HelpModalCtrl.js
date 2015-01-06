myApp.controller('HelpModalCtrl', function ($scope, $modalInstance,message) {

    $scope.message=message;

    $scope.close = function () {
        $modalInstance.close();
    };

});