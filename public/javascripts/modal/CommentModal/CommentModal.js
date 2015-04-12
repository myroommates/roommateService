myApp.controller('CommentModalCtrl', function ($scope, $http, $flash, $modalInstance, roommateList, comments, targetURL, $timeout) {

    $scope.loading = false;
    $scope.comments = comments;

    $scope.getRoommate = function (id) {

        for (var key in roommateList) {

            if (roommateList[key].id == id) {
                return roommateList[key];
            }
            ;
        }
        return null;
    }


    $scope.close = function () {
        $modalInstance.close();
    };

    $timeout(function () {
        $(".comment-container").scrollTop($('.comment-container').height());
    }, 1);
    $scope.sendComment = function () {

        var dto = {
            comment: $scope.newComment
        };

        $scope.loading = true;

        $http({
            'method': "POST",
            'url': targetURL,
            'headers': "Content-Type:application/json",
            'data': dto
        }).success(function (data, status) {
            $scope.loading = false;
            comments.push(data);
            $scope.newComment = "";
            $(".comment-container").animate({scrollTop: $(document).height()}, "slow");
        })
            .error(function (data, status) {
                $scope.loading = false;
                $flash.error(data.message);
            });
    }


});