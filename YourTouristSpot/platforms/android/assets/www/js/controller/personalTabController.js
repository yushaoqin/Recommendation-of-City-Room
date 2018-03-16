app.controller('PersonalTabCtrl', function($scope,dataService,$http,commonService,$state) {
    $scope.user= dataService.getUser();
    console.log($scope.user.state);
    $scope.userInput = {
        username : '',
        pwd : ''
    };

    $scope.isShowShared = true;
    $scope.isShowLiked = true;

    $scope.showLiked = function(){
        $scope.isShowLiked = !$scope.isShowLiked;
    }

    $scope.showShared = function(){
        $scope.isShowShared = !$scope.isShowShared;
    }

    //每次进入页面时刷新roomsLiked，roomsShared
    $scope.$on('$ionicView.beforeEnter', function() {
        console.log('PERSONAL beforeEnter');
        $scope.roomsLiked =  dataService.getLikedRooms();
        $scope.roomsShared = dataService.getSharedRooms();
    });


    $scope.logout = function(){
        //清空登陆信息。重置dataservice中的数据
        dataService.setUser(-1,'','',0); //id ,username.pwd,state
        dataService.setLikedRooms(null);
        dataService.setSharedRooms(null);
        $scope.user = dataService.getUser();
        console.log($scope.user);
        $scope.userInput.username ='';
        $scope.userInput.pwd = '';
        $scope.roomsLiked = [];
        $scope.roomsShared = [];
        swal('已退出登录');
    };

    //注册
    $scope.register = function(){
        if(commonService.isStringEmpty($scope.userInput.username)||commonService.isStringEmpty($scope.userInput.pwd)){
            swal('请填写用户名、密码!');
            return;
        }
        $http({
            method:'POST',
            url:commonService.getServerUrl()+'Register',
            params:{
                'username':$scope.userInput.username,
                'password':$scope.userInput.pwd
            }
        }).then(function successCallback(response){
            $scope.userInput.username = '';
            $scope.userInput.pwd = '';
            console.log(response);
            //返回1 注册成功 否则发生错误
            if(response.data == -1 ){
                console.log('register error1');
                swal('注册失败！');
            }else{
                swal('注册成功！');
                console.log('register success');
            }

        },function errorCallback(response){
            swal('注册时发生错误！');
            console.log('register error2');
        })
    }

    $scope.login = function(){
        if(commonService.isStringEmpty($scope.userInput.username)||commonService.isStringEmpty($scope.userInput.pwd)){
            swal('请填写用户名、密码!');
            return;
        }
        $http({
            method: 'POST',
            url:commonService.getServerUrl()+"Login",
            params:{
                'username':$scope.userInput.username,
                'password':$scope.userInput.pwd
            }
        }).then(function successCallback(response) {
            $scope.userInput.username = '';
            $scope.userInput.pwd = '';
            $scope.roomsLiked = [];
            $scope.roomsShared = [];
            console.log(response);
            var res = response.data; //res[0] 是用户个人信息 res[1]是用户分享空间信息 res[2]是用户喜欢空间的信息
            var userData = res[0];

            if(userData.state != 1 && userData.state != -1){
                console.log('Login User:  username:'+$scope.user.username +"  userId:"+$scope.user.id);
                swal('服务器内部错误！');
                return;
            }


            //处理空间信息中的tag，并赋值给变量以及dataService
            var roomsShared = res[1];
            var roomsLiked = res[2];
            for(var i =0;i<roomsLiked.length;i++){
                roomsLiked[i].tags = commonService.getTagsByString(roomsLiked[i].tags);  //处理tag 由字符串变为数组
            }
            for(var i =0;i<roomsShared.length;i++){
                roomsShared[i].tags = commonService.getTagsByString(roomsShared[i].tags);
            }
            $scope.roomsLiked = roomsLiked;
            $scope.roomsShared = roomsShared;
            console.log($scope.roomsLiked);
            console.log($scope.roomsShared);
            dataService.setLikedRooms(roomsLiked);
            dataService.setSharedRooms(roomsShared);
            console.log(userData.state);
            if(userData.state == -1 ){
                console.log('用户名、密码错！');
                swal('用户名、密码错误！');

            }else if(userData.state == 1){
                console.log('登陆成功！');
                dataService.setUser(userData.id,userData.username,userData.password,1);
                $scope.user = dataService.getUser();
                console.log('Login User:  username:'+$scope.user.username +"  userId:"+$scope.user.id);
                swal('登陆成功！');

            }

        }, function errorCallback(response) {
            swal('登录时发生错误！');
            console.log("login error");
        });
    }


    $scope.roomDetail = function(id){
        console.log(id);
        $state.go('tabs.personalRoomInfo',{id : id});
    }


});