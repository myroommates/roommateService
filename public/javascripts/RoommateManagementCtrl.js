var myApp = angular.module('app', []);

myApp.controller('RoommateManagementCtrl', ['$scope', function($scope) {

    $scope.roommateList= [];

    $scope.addRoommate = function(){
        $scope.roommateList.push(new {});
    }

}]);