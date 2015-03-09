myApp.controller('LoginCtrl', function ($scope,$modal) {


    $scope.forgotPassword = function(){
        $modal.open({
            templateUrl: "/assets/javascripts/modal/ForgotPassword/view.html",
            controller: "ForgotPasswordModalCtrl",
            size: 'lg'
        });
    }

});


