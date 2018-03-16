/**
 * Created by hasee on 2017/4/23.
 */


app.service('mapService', function() {


    var map;
    var all_markers = []; //所有图标 除了用户图标
    var all_rooms = [];  //所有空间
    var me_marker; //用户图标


    //构建地图
    var buildMap = function(){
        map = new BMap.Map("allmap");
        var point = new BMap.Point(121.484231,31.249057);
        map.centerAndZoom(point,20);
        map.enableScrollWheelZoom(true);    //滚轮缩放
        map.setMapStyle({style:'googlelite'}); //精简模式
    }


    //定位用户
    var geolocation = new BMap.Geolocation();
    var locatedUser = function(){
        geolocation.getCurrentPosition(function(r){
            if(this.getStatus() == BMAP_STATUS_SUCCESS){
                me_marker = new BMap.Marker(r.point);
                map.panTo(r.point);
                addMeMarker();

                console.log(r);
                console.log('您的位置：'+r.point.lng+','+r.point.lat);
                //alert('省份：'+r.address.province +' 城市：'+r.address.city+' 区：'+r.address.district+' 街道：'+r.address.street+' 号码：'+r.address.street_number);

                sessionStorage.currentPosition = JSON.stringify(r);
            }
            else {
                alert('failed'+this.getStatus());
            }
        },{enableHighAccuracy: true});
    };

    //将用户所在位置在地图上标注
    var addMeMarker = function(){
        var label = new BMap.Label("Me",{offset:new BMap.Size(20,-10)});
        if(me_marker == undefined){return;}
        me_marker.setLabel(label);
        map.addOverlay(me_marker);
    }


    //将room添加进map
    var addNewOverlay =  function(rooms){
        for(var i=0;i<rooms.length;i++){
            var room = rooms[i];
            var point = new BMap.Point(room.lng,room.lat);
            var marker = new BMap.Marker(point);
            var label = new BMap.Label(room.name,{offset:new BMap.Size(20,-10)});
            marker.setLabel(label);
            all_markers.push(marker);
            all_rooms.push(room);
            marker.addEventListener("click",function(e){
                window.location.href= '#/tab/roomInfo?id='+room.id;
                console.log(room.id);
                console.log(marker);
            });
            map.addOverlay(marker);
        }

    };


    //根据 point 解析 具体地址信息
    var geoc = new BMap.Geocoder();
    var getLocationFromPoint = function(point){
        var result ='aaa';
        geoc.getLocation(point, function(rs){
            var addComp = rs.addressComponents;
            result =  addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;

        });
        return result;
    };

    this.getGeoc = function(){
        return geoc;
    }

    this.getMap = function(){
        return map;
    }


    //重新绘制遮盖物
    var addOverlay = function(){
        for(var i=0;i<all_markers.length;i++){
            all_markers[i].addEventListener("click",function(e){
                console.log(all_markers[i]);
            });
            map.addOverlay(all_markers[i]);
        }
    };


    this.addOverlay = function(){
        addOverlay();
    }

//清除遮盖物
    this.clearOverlay = function(){
        map.clearOverlays();
        addMeMarker();
    }

    this.judgeDistance = function(pointA,pointB,range){
        console.log(BMap.getData(pointA,pointB));
        if(BMap.getDistance(pointA,pointB)<range){
            return true;
        }else{
            return false;
        }
    };





    this.getLocationFromPoint = function(point){
        getLocationFromPoint(point);
    }

    this.addMeMarker = function(){
        addMeMarker();
    }


    this.buildMap = function(){
        buildMap();
    };


    this.locatedUser = function(){
        locatedUser();
    };

    this.addNewOverlay = function(room){
        addNewOverlay(room);
    }




});




















