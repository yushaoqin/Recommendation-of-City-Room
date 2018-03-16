
app.controller('RoomInfoCtrl', function($scope,$ionicModal,dataService,commonService,$state,$stateParams,$http,FileUploader,$timeout) {

    // 获取参数room id
    var roomId = $stateParams.id ;
    $scope.test1 = 1.2;
    $scope.room_info = {
        point:{value:1.5}
    };
    $scope.room_info.image = [];
    $scope.room_comments = [];
    var getRoomInfo = function(){
        var http = $http({
            url: commonService.getServerUrl() + 'GetRoomInfo',
            method: 'POST',
            params: {
                'id': roomId
            }
        }).then(function success(res){
            var room = res.data;
            var tags ;
                console.log(res);
            $scope.room_info = {
                room_name: room.name,
                location: room.location,
                lat: room.lat,
                lng: room.lng,
                id: room.id,
                tags: room.tags.split(','),
                username:room.username,
                description:room.description,
                likes:room.likes,
                image:room.img,
                point:room.point
            };
            for(var i =0;i<$scope.room_info.image.length;i++){
                //$scope.room_info.image[i].imgPath  = commonService.getServerUrl()+'image/'+$scope.room_info.image[i].imgPath;
                $scope.room_info.image[i].imgPath  = commonService.serverImagePath+$scope.room_info.image[i].imgPath;
            }

            //获取评论
            $scope.room_comments = room.comment;

            //展示分数
            console.log($scope.room_info.point.value);
            $("#input-21b").val($scope.room_info.point.value);
            $("#input-21b").rating({
                showClear:false,
                type:"number",
                min:"0",
                max:"5",
                step:"0.1" ,
                size:"lg"});

        },function error(res){
                console.log(res);
            });

        return http;
    };

    getRoomInfo();

    $scope.doRefresh = function() {
        getRoomInfo().finally(function() {
                $scope.$broadcast('scroll.refreshComplete');
            });
    };


    //是否显示评论
    $scope.is_comments_show = false;
    $scope.showComments = function(){
        $scope.is_comments_show =!$scope.is_comments_show;
    };



    //上传图片
    $scope.uploader = new FileUploader({
        url: commonService.getServerUrl()+'UploadImage',
        formData:[]
    });
    $scope.uploader.filters.push({
        name: 'customFilter',
        fn: function(item, options) {
            return this.queue.length < 3;
        }
    });
    //添加文件后预览
    $scope.uploader.onAfterAddingFile = function(fileItem) {
        console.log('添加了文件：');
        console.log(fileItem);
        //预览显示位置
        var result = document.getElementById("previewImg");

        commonService.previewImg(fileItem._file,result);

    };

    $scope.uploader.onCompleteAll = function(){
        $scope.uploader.clearQueue();
        document.getElementById("previewImg").innerHTML = "";
        swal('感谢您的完善！');
    }




    //完善信息模板
    $ionicModal.fromTemplateUrl('modals/addRoomDescription.html', {
        scope: $scope
    }).then(function(modal) {
        $scope.add_room_description_modal = modal;
    });
    //添加评论
    $ionicModal.fromTemplateUrl('modals/roomComment.html', {
        scope: $scope
    }).then(function(modal) {
        $scope.add_room_comment_modal = modal;
    });


    $scope.show_add_room_comment_modal = function(){
        if(dataService.isLogin()){
            $scope.add_room_comment_modal.show();
        }else{
            swal('请先登陆！');
        }
    }

    $scope.show_add_room_description_modal = function(){
        if(dataService.isLogin()){
            $scope.add_room_description_modal.show();
        }else{
            swal('请先登陆！');
        }
    }

    $scope.new_room_info = {
        description:'',
        comment:''
    };
    $scope.commit_add_room_comment = function(){
        $http({
            url: commonService.getServerUrl() + 'AddInfo',
            method: 'POST',
            params: {
                'roomId': roomId,
                'username':dataService.getUser().username,
                'text':$scope.new_room_info.comment,
                'type':0  // 0 评论 ，1 描述
            }
        }).then(function success(response){
            getRoomInfo();
            swal('已完成评论');
        },function error(response){
            swal('发生错误')
        });
    }

    $scope.commit_add_room_description = function(){
        if(commonService.isStringEmpty($scope.new_room_info.description)&& $scope.uploader.queue.length == 0 ){
            swal('请添加描述或者图片');
            return;
        }
        if(commonService.isStringEmpty($scope.new_room_info.description) == false){
            $http({
                url: commonService.getServerUrl() + 'AddInfo',
                method: 'POST',
                params: {
                    'roomId': roomId,
                    'username':dataService.getUser().username,
                    'text':$scope.new_room_info.description,
                    'type':1  // 0 评论 ，1 描述
                }
            }).then(function success(response){
                console.log("补充结果："+response.data);
                if( response.data == -1){
                    swal('发生错误');
                }else{
                    getRoomInfo();
                    swal('感谢您的完善！');
                }
            },function error(response){
                swal('发生错误')
            });
        }

        if($scope.uploader.queue.length != 0){
            for(var i =0;i<$scope.uploader.queue.length;i++){
                $scope.uploader.queue[i].formData = [dataService.getUser().username+','+$scope.room_info.id];
                $scope.uploader.queue[i].upload();
        }

    }
    }

    $scope.commit_add_like =function(){
        if(!dataService.isLogin()){
            swal('请先登录！');
            return;
        }
        $http({
            url: commonService.getServerUrl() +'AddLiked',
            method:'POST',
            params:{
                'userId': dataService.getUser().id,
                'roomId': roomId,
                'type': 2
            }
        }).then(function success(response){
            console.log('add LIKED RESULT');
            console.log(response);
            var data = response.data;
            if(data == -2){console.log('add liked failed with result:-2');swal('已再喜好列表中，无法重复添加！');return;}
            if(data == -1){console.log('add liked error1 with result:'+data);return;}
            if(data == 1){console.log('add liked success');swal('已加入喜好列表！');}
            getRoomInfo();
        },function error(response){
            console.log('add liked error2');
            swal('添加失败，请重试!');
        });
    }


    /*
    评分功能
     */
//value="1.2" type="number"min="0" max="5" step="0.1" data-size="lg"

    $scope.uploadPoint=function(){
        var p = $("#input-21b").val();
        console.log(p);
        if(dataService.isLogin() == false) {
            swal('请登录');
            return;
        }
        if(p == 0){
            swal('不能选择0分！');
            return;
        }

        $http({
            url: commonService.getServerUrl() +'AddPoint',
            method:'POST',
            params:{
                'userId': dataService.getUser().id,
                'roomId': roomId,
                'point': p
            }
        }).then(function success(response){
            console.log('add point RESULT');
            console.log(response);
            var data = response.data;
            if(data == -2){console.log('add point failed with result:-2');swal('请勿重复打分！');return;}
            if(data == -1){console.log('add point error1 with result:'+data);return;}
            if(data == 1){console.log('add point success');swal('完成评分！');}
            getRoomInfo();
        },function error(response){
            console.log('add point error2');
            swal('出错了');
        });

    };


});
