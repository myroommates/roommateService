myApp.controller('CountResumeCtrl', function ($scope, $http, $flash, $modal,translationService,$locale) {
    console.log($locale.id);
    $locale.id=data.langId;
    console.log($locale.id);
    translationService.set(data.translations);

    $scope.ticketList=ticketList.list;

    //sort
    $scope.countResumeList = _.sortBy(countResumeList.list,function(element){
       return element.roommate.id;
    });

    $scope.sub = function(a,b){
        return a - b;
    }

    $scope.totalSpend =function(){
        var total = 0;
        for(var key in $scope.countResumeList){
            total+=$scope.countResumeList[key].spend;
        }
        return total;
    }

    $scope.totalDept = function(){
        var total = 0;
        for(var key in $scope.countResumeList){
            total+=$scope.countResumeList[key].dept;
        }
        return total;
    }


    $scope.computeResult = function(){

        var resultDetailList=[];

        for(var key in $scope.countResumeList){
            var roommate = $scope.countResumeList[key].roommate;
            var resultDetail={
                roommate:roommate,
                payed:0,
                mustPay:0
            };
            for(var key2 in $scope.ticketList){
                var ticket = $scope.ticketList[key2];
                for(var key3 in ticket.debtorList){
                    if(ticket.payerId ==roommate.id ) {
                        resultDetail.payed += ticket.debtorList[key3].value;
                    }
                    if(ticket.debtorList[key3].roommateId == roommate.id){
                        resultDetail.mustPay+= ticket.debtorList[key3].value;
                    }
                }

            }
            resultDetail.resetToReceive = resultDetail.payed - resultDetail.mustPay;
            resultDetailList.push(resultDetail);
        }

        $scope.balanceList = [];

        for(key in resultDetailList){
            var resultDetail = resultDetailList[key];
            var toPay = resultDetail.payed - resultDetail.mustPay;
            if(toPay < 0){
                for(key2 in resultDetailList){
                    var resultDetail2 = resultDetailList[key2];
                    if (resultDetail2.resetToReceive > 0) {
                        var balance = null;
                        for (key3 in  $scope.balanceList) {
                            balanceToFind =$scope.balanceList[key3];
                            if (balanceToFind.from.id ==  resultDetail.roommate.id &&
                                balanceToFind.to.id == resultDetail2.roommate.id) {
                                balance = balanceToFind;
                            }
                        }
                        if (balance == null) {
                            balance = {};
                            balance.from = resultDetail.roommate;
                            balance.to = resultDetail2.roommate;
                            balance.value=0;
                        }
                        $scope.balanceList.push(balance);

                        if (resultDetail2.resetToReceive >= Math.abs(toPay)) {
                            balance.value += Math.abs(toPay);
                            resultDetail2.resetToReceive -= Math.abs(toPay);
                            break;
                        } else {
                            balance.value += resultDetail2.resetToReceive;
                            toPay += resultDetail2.resetToReceive;
                            resultDetail2.resetToReceive = 0;
                        }
                    }
                }
            }

        }
    };

    $scope.computeResult();

});