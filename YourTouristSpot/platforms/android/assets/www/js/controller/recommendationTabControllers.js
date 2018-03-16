/**
 * Created by hasee on 2017/4/9.
 */
app.controller('RecommendationTabCtrl', function($scope,$ionicModal,dataService,$state,$http,commonService) {
    $scope.showRec = dataService.isLogin();
    $scope.message =[
       '请登录后获取推荐!',
        '对景点评分，收藏将获得更好的推荐效果！'
   ];
    $scope.doRefresh = function() {
        $scope.getRecommendations().finally(function() {
            $scope.$broadcast('scroll.refreshComplete');
        });
    };
    $scope.sortRecommendations = function(rooms){
        var index_array = [];
        var tmp_array = [];
        for(var i =0;i<rooms.length;i++){
            index_array.push(rooms[i].index);
        }
        for(var i=0;i<rooms.length;i++){
            for(var j=0;j<index_array.length;j++){
                if(index_array[j] == i){
                    tmp_array.push(rooms[j]);
                    break;
                }
            }
        }
        return tmp_array.reverse();
    }
    $scope.getRecommendations = function(){
        if(dataService.isLogin() == false){
            //若没登陆则不发出请求
            return;
        }
        var user = dataService.getUser();

        var http = $http({
            url:commonService.getServerUrl()+'GetRecommendation',
            method:'POST',
            params:{
                'id': user.id,
                'username': user.username,
                'state':user.state
            }
        }).then(function success(response){
            console.log(response);
            var status = response.data.status;
            if(status != 1){
                //something wrong
                swal('服务器出错。');
                return;
            }

            dataService.setRecommendations(response.data.rooms);
            var rooms = dataService.getRecommendations();
            if(rooms == null || rooms == undefined){
                //推荐结果为0
                return;
            }

            $scope.recommendations = dataService.getRecommendations();
       //     $scope.recommendations = $scope.sortRecommendations(rooms);
            console.log($scope.recommendations);
        },function error(response){
            console.log(response);
            swal('服务器出错。');
        });
        return http;
    };
    var first_in = true;
    $scope.$on('$ionicView.beforeEnter', function() {
        console.log('RECOMMENDATION beforeEnter');
        $scope.showRec = dataService.isLogin();
        if(first_in == true && $scope.showRec == true){
            console.log('RECOMMENDATION FIRST IN');
            $scope.getRecommendations();
            first_in = false;
        }else{
            console.log('RECOMMENDATION NOT FIRST IN OR NOT LOG IN');
            $scope.recommendations = dataService.getRecommendations();
        }
    });



    $scope.roomDetail = function(id){
        console.log(id);
        $state.go('tabs.roomInfo',{id : id});		//就拿它传
    }

})