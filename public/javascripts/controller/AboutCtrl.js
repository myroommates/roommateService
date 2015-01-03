myApp.controller('AboutCtrl',  function ($scope, $modal) {

    $scope.openCalculator= function(){
        var resolve = {
            setResult: function () {
                return function(result){
                    scope.getInfo().field=result;
                };
            }
        };

        $modal.open({
            templateUrl: "/assets/javascripts/modal/Calculator/view.html",
            controller: "CalculatorModalCtrl",
            size:"sm",
            resolve: resolve
        });
    };

});