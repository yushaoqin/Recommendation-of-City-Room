/**
 * Created by hasee on 2017/4/5.
 */
app.service('dataService', function() {
    //推荐信息
    var recommendations = [];

    //获取推荐信息
    this.getRecommendations = function(){
        return recommendations;
    };
    //用户信息
    var user={
        id:-1,
        username: '',
        pwd:'',
        state:0
    };
    //设置用户信息
    this.setUser = function(id,username,pwd,state){
        user.id = id;
        user.username = username;
        user.pwd = pwd;
        user.state = state;
    };
    //获取用户信息
    this.getUser = function(){
        return user;
    };

    //处理返回room data中的tag， 由"a,b,c" 转化成 ["a","b","c"]
    this.transferTag = function(rooms){
        for(var i =0;i<rooms.length;i++){
            rooms[i].tags = rooms[i].tags.split(',');
        }
    }
    //所有空间信息
    var all_rooms = [];
    this.setAllRoomsWithRightTags = function(rooms){
        all_rooms = rooms;
        //recommendations = rooms;
    }

    this.setAllRooms = function(rooms){
        if(rooms == null || rooms == undefined){
            return;
        }
        this.transferTag(rooms);
        all_rooms = rooms;
    }

    this.setRecommendations = function(rooms){
        if(rooms == null || rooms == undefined){
            return;
        }
        for(var i =0;i<rooms.length;i++){
            rooms[i].tags = rooms[i].tags.split(',');
        }
        recommendations = rooms;
    }
    //获取所有空间信息
    this.getAllRooms = function(){
        return all_rooms;
    };

    /*
    rooms 皆为 room的对象数组
     */
    var liked_rooms;   //用户喜欢的空间
    var shared_rooms;  //用户分享的空间

    this.setLikedRooms = function(rooms){
        liked_rooms = rooms;
    };
    this.getLikedRooms = function(){
        return liked_rooms;
    }

    this.setSharedRooms = function(rooms){
        shared_rooms = rooms;
    };
    this.getSharedRooms = function(){
        return shared_rooms;
    }

    /*
    search result
     */
    var search_result = [];
    this.setSearchResult = function(rooms){
        this.transferTag(rooms);
        search_result = rooms;
    }

    this.getSearchResult = function(){
        return search_result;
    }


    //判断是否为登陆状态
    this.isLogin = function(){
        if (user.state == 0 ||user.username == ''|| user.username == undefined || user.username == null){
            return false;
        }else{
            return true;
        }
    }

});