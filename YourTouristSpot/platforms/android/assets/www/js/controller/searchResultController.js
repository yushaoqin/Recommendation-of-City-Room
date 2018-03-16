
app.controller('SearchResultCtrl', function($scope,$ionicModal,dataService,commonService,$state,$stateParams,$http,FileUploader,$timeout) {

    $scope.search_result = dataService.getSearchResult();
    $scope.search_result_to_show = $scope.search_result;
    $scope.roomDetail = function(id){
        console.log(id);
        $state.go('tabs.homeRoomInfo',{id : id});		//就拿它传
    }


    $scope.isShaiXuanShow = false;

    $scope.showShaiXuan = function(){
        $scope.isShaiXuanShow = !$scope.isShaiXuanShow;
    }

    $scope.tagList = [
        { text: "娱乐", checked: false },
        { text: "美食", checked: false },
        { text: "游览", checked: false },
        { text: "运动", checked: false },
        { text: "办公", checked: false }
    ];
    $scope.tagsWantToShow = ['娱乐','美食','游览','运动','办公'];
    function watchTagList(){
        for(var i =0;i<$scope.tagList.length;i++){
            var tmp = 'tagList['+i+'].checked';
            $scope.$watch(tmp,function(newValue,oldValue){
                if(newValue == oldValue){
                    return;
                }
                $scope.tagsWantToShow = [];
                for(var j =0;j< $scope.tagList.length;j++){
                    if($scope.tagList[j].checked == true){
                        $scope.tagsWantToShow.push($scope.tagList[j].text);
                    }
                }
                console.log($scope.tagsWantToShow);

            });
        }
    }

    watchTagList();

    $scope.$watch('tagsWantToShow',function(newValue,oldValue){
        if(newValue == oldValue){
            return;
        }


        if($scope.tagsWantToShow.length == 0 ){
            $scope.search_result_to_show = $scope.search_result;
            return;
        }else{
            $scope.search_result_to_show = [];
        }
        var not_to_show = [];
        console.log( $scope.search_result_to_show);
        for(var i =0;i<$scope.search_result.length;i++){
            var room = $scope.search_result[i];
            console.log(room.name);
            for(var j =0;j<newValue.length;j++){
                if(room.tags.indexOf(newValue[j]) == -1){
                    console.log(newValue[j]);
                   not_to_show.push(room);
                    break;
                }
            }
        }
        console.log(not_to_show);
        for(var i=0;i<not_to_show.length;i++){
            for(var j =0;j<$scope.search_result.length;j++){
                if($scope.search_result[j].id == not_to_show[i].id){
                    console.log('remove id:'+ $scope.search_result[j].id);
                }else{
                    $scope.search_result_to_show.push($scope.search_result[j]);
                }
            }
        }
        console.log( $scope.search_result_to_show);


    });





});
