myApp.controller('LateralMenuCtrl', function ($scope) {

    $scope.itemOpen = [];

    $scope.switchVisibility = function (menuItem) {
        $scope.itemOpen[menuItem] = !$scope.menuItemOpen(menuItem);
    };

    $scope.menuItemOpen = function (menuItem) {
        if ($scope.itemOpen[menuItem] == undefined || $scope.itemOpen[menuItem] == false) {
            return false;
        }
        return true;
    };

});