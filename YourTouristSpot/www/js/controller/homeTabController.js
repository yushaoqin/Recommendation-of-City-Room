
app.controller('HomeTabCtrl', function($scope,$ionicModal,dataService,$window,$http,commonService,$q,mapService,FileUploader,$state) {
    //构建地图
    mapService.buildMap();
    //定位用户
    mapService.locatedUser();

    //获取空间信息
    $scope.getAllRooms = function(){
        $http({
            url:commonService.getServerUrl()+'GetAllRooms',
            method:'POST',
            params:{}
        }).then(function success(response){
            console.log(response);
            dataService.setAllRooms(response.data);
            var rooms = dataService.getAllRooms();
            mapService.clearOverlay();
            mapService.addNewOverlay(rooms);
            console.log(rooms);
            console.log('repaint all rooms');
        },function error(response){
            console.log(response);
        });

    };
    $scope.$on('$ionicView.beforeEnter', function() {
        console.log('HOME beforeEnter');
        $scope.getAllRooms();
    });


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
        $scope.getAllRooms();
        swal('添加新空间成功');
    }



    //添加活动空间模板
    $ionicModal.fromTemplateUrl('modals/newActivityRoomModal.html', {
        scope: $scope
    }).then(function(modal) {
        $scope.new_room_modal = modal;
    });


    //空间信息
    $scope.new_room ={
        location:{
            text: '',
            lat:'',
            lng:''
        },
        name:'',
        tags:[],
        description:''
    };
    //图片文件
    var files;
    $scope.isSelectLocation = false;

    //添加景点时移动的标签
    var new_room_marker;

    $scope.showNewRoomModal = function(){
        //判断是否登录
        if(dataService.getUser().username ==''){
            swal('请先登录');
            return;
        }


        mapService.clearOverlay(); //清除原先标签
        $scope.isSelectLocation = true; //控制按钮显示

        var current_position = JSON.parse($window.sessionStorage.currentPosition);
        $scope.new_room ={
            location:{
                text:current_position.address.province+" "+current_position.address.city+" "+current_position.address.district+" "+current_position.address.street+" "+current_position.address.street_number,
                lat:current_position.point.lat,
                lng:current_position.point.lng
            },
            name:'',
            tags:[],
            description:''
        };
        //添加标签
        var point = new BMap.Point($scope.new_room.location.lng+0.0001,$scope.new_room.location.lat+0.0001);
        new_room_marker = new BMap.Marker(point);
        new_room_marker.enableDragging();
        var label = new BMap.Label("拖动我选择位置！",{offset:new BMap.Size(20,-10)});
        new_room_marker.setLabel(label);
        new_room_marker.addEventListener("dragstart",function(e){label.setStyle({display:"none"})}); //开始拖动后隐藏标注

        //拖动结束获取坐标以及地理位置信息
        new_room_marker.addEventListener("dragend", function(e){
                $scope.new_room.location.lat = e.point.lat;
                $scope.new_room.location.lng = e.point.lng;
                var geoc = mapService.getGeoc();
                geoc.getLocation(e.point, function(rs){
                    var addComp = rs.addressComponents;
                    tmp_str =   addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                });
            }
        );


        files = null;
        for(var i=0;i<$scope.tags.length;i++){
            $scope.tags[i].isChecked = false;
        }
        $scope.new_room_modal.show();
        document.getElementById("previewImg").innerHTML='';
    }

    $scope.newRoomCancel = function(){
        console.log(dataService.getAllRooms());
        mapService.addNewOverlay(dataService.getAllRooms());
        mapService.addMeMarker();
        $scope.isSelectLocation = false;
        $scope.new_room_modal.hide();

        //清空上传文件队列
        $scope.uploader.clearQueue();
    }
    //标签
    $scope.tags = [
        {
            id:'entertainment',
            text:'娱乐',
            isChecked:false,
        },
        {
            id:'sports',
            text:'运动',
            isChecked:false,
        },
        {
            id:'food',
            text:'美食',
            isChecked:false,
        },
        {
            id:'work',
            text:'办公',
            isChecked:false,
        },
        {
            id:'tour',
            text:'游览',
            isChecked:false,
        },
    ]


    //当前位置
    var tmp_str;
    //获取当前地理位置
    $scope.getCurrentPosition = function () {
        $scope.isSelectLocation = true;
        $scope.new_room_modal.hide();
        mapService.getMap().addOverlay(new_room_marker);


    }

    //确认新空间地址
    $scope.commit_location = function(){

        $scope.isSelectLocation = false;

        $scope.new_room.location.text = tmp_str;
        new_room_marker.disableDragging();
        mapService.clearOverlay();
        $scope.new_room_modal.show();
    }

    //提交新空间按钮
    $scope.newRoomCommit = function(){
        // 判断选中标签
        for(var i =0;i<$scope.tags.length;i++){
            if($scope.tags[i].isChecked == true){
                $scope.new_room.tags.push($scope.tags[i].text);
            }
        }

        if(commonService.isStringEmpty($scope.new_room.name) ||commonService.isStringEmpty($scope.new_room.description)|$scope.new_room.tags.length == 0){
            swal({
                title: "出错啦!",
                text: "除了图片是可选之外，其他都要填！",
                timer: 2000,
                showConfirmButton: false
            });
            return;
            }
        console.log(JSON.stringify($scope.new_room));
//        dataService.setAllRooms(dataService.getAllRooms().push($scope.new_room));
        var rooms = dataService.getAllRooms();
        rooms.push($scope.new_room);

        dataService.setAllRoomsWithRightTags(rooms);
        mapService.addNewOverlay(dataService.getAllRooms());

        $scope.new_room_modal.hide();
      //  alert(dataService.getUser().username);
        //提交结果至服务器
        $http({
            method: 'POST',
            url:commonService.getServerUrl()+'NewRoom',
            params:{
                'userId':dataService.getUser().id,
                'location_text':$scope.new_room.location.text,
                'lat':$scope.new_room.location.lat,
                'lng':$scope.new_room.location.lng,
                'name':$scope.new_room.name,
                'tags':$scope.new_room.tags,
                'description':$scope.new_room.description,
                'username':dataService.getUser().username
            }
        }).then(function successCallback(response) {

            if(response.data == -1){
                swal('添加出错');
            }else{
                //没有图片
                if($scope.uploader.queue.length ==0){
                    $scope.getAllRooms();
                    swal('添加新空间成功');
                    console.log('新空间ID：'+response.data);
                    return;
                }else{//有图片
                    for(var i =0;i<$scope.uploader.queue.length;i++){
                        $scope.uploader.queue[i].formData = [dataService.getUser().username+','+response.data];
                       $scope.uploader.queue[i].upload();
                    }
                }

            }
            console.log(response);
        }, function errorCallback(response) {
            //do something
            swal('出错，请填写完整信息');
        });


    }


    $scope.search_text = {};
    $scope.search = function(){
        console.log($scope.search_text.string);
        $http({
            url:commonService.getServerUrl()+'Search',
            method:'POST',
            params:{
                'keyword':$scope.search_text.string
            }
        }).then(function success(res){
            console.log(res);
            dataService.setSearchResult(res.data);
            $state.go('tabs.search_result');
        },function error(res){
            console.log(res);
        });
    }









});