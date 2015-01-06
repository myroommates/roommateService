myApp.controller('CalculatorModalCtrl', function ($scope, $modalInstance, setResult) {

    $scope.input = "";
    $scope.memory = "";
    $scope.acceptOperator = false;

    $scope.valid = function(){
        $scope.result();
        var value = parseFloat($scope.memory);
        if (!!value) {
            setResult(value);
        }
        $scope.close();
    };

    $scope.close = function () {
        $modalInstance.close();
    };

    $scope.press = function (key) {
        $scope.input += key;
    };

    $scope.clear = function () {
        $scope.input = "";
    };

    $scope.clearAll = function () {
        $scope.input = "";
        $scope.memory = "";
    };

    $scope.clearLastOne = function () {
        $scope.input = $scope.input.slice(0, $scope.input.length - 1);
    }

    $scope.operation = function (operator) {
        if (!!parseFloat($scope.input) || !isNaN($scope.memory)) {
            $scope.memory += $scope.input + " ";
            $scope.memory += operator + " ";
            $scope.input = "";
        }
        else if($scope.memory.length>0){
            $scope.memory = $scope.memory.slice(0,$scope.memory.length-2)+operator+" ";
        }
    };

    $scope.result = function () {
        $scope.memory += $scope.input + " ";
        $scope.memory = math.eval($scope.memory);
        $scope.input = "";
    };

    document.onkeypress = function (e) {
        e = e || window.event;

        // 1
        if (e.keyCode == 49) {
            $scope.press(1);
        }// 2
        else if (e.keyCode == 50) {
            $scope.press(2);
        }// 3
        else if (e.keyCode == 51) {
            $scope.press(3);
        }// 4
        else if (e.keyCode == 52) {
            $scope.press(4);
        }// 5
        else if (e.keyCode == 53) {
            $scope.press(5);
        }// 6
        else if (e.keyCode == 54) {
            $scope.press(6);
        }// 7
        else if (e.keyCode == 55) {
            $scope.press(7);
        }// 8
        else if (e.keyCode == 56) {
            $scope.press(8);
        }// 9
        else if (e.keyCode == 57) {
            $scope.press(9);
        }// 0
        else if (e.keyCode == 48) {
            $scope.press(0);
        }// decimal
        else if (e.keyCode == 46 || e.keyCode == 44) {
            $scope.press('.');
        }// +
        else if (e.keyCode == 43) {
            $scope.operation('+');
        }// /
        else if (e.keyCode == 47) {
            $scope.operation('/');
        }// -
        else if (e.keyCode == 48) {
            $scope.operation('-');
        }// *
        else if (e.keyCode == 42) {
            $scope.operation('*');
        }// =
        else if (e.keyCode == 63 || e.keyCode == 13) {
            $scope.result();
        }// delete
        else if (e.keyCode == 127) {
            $scope.clear();
        }

        $scope.$apply();
    };

});