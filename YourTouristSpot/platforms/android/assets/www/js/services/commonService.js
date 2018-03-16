
/**
 * Created by hasee on 2017/4/5.
 */
app.service('commonService', function() {
    //服务器地址
    //
 //   var serverUrl = 'http://192.168.0.102:8080/YSTServer/';
    var serverUrl = 'http://172.16.66.157:8080/YSTServer/';
    this.serverImagePath = 'http://172.16.66.157:8080/YSTServer/image/';
    //var serverUrl = 'http://localhost:8080/YSTServer/';
    //this.serverImagePath = 'http://localhost:8080/YSTServer/WebContent/image/'
    this.getServerUrl = function(){return serverUrl;}
    //
    //var serverUrl = 'http://119.23.210.172:8080/YSTServer/';
    //this.serverImagePath = 'http://119.23.210.172:8080/yst_image/image/';



    this.addJs = function(src){
        new_element=document.createElement("script");
        new_element.setAttribute("type","text/javascript");
        new_element.setAttribute("src",src);
        document.body.appendChild(new_element);
        console.log('add'+src);
        return 1;
    }

    this.isStringEmpty = function(str){
        if(str == null || str ==''){
            return true;
        }else{
            return false;
        }
    }

    this.getTagsByString = function(str){
        return str.split(',');
    }


    /*
    file: 要预览的图片文件
    whereToShow: 预览图片展示位置的dom元素
     */
    this.previewImg = function(file,whereToShow){
        if(typeof FileReader == 'undefined') {
           swal("抱歉，你的浏览器不支持FileReader");
        }

        // 将文件以Data URL形式进行读入页面

        // 检查是否为图像类型
        if(!/image\/\w+/.test(file.type)) {
            swal("请确保文件类型为图像类型");
            return false;
        }
        var reader = new FileReader();
        // 将文件以Data URL形式进行读入页面
        reader.readAsDataURL(file);
        reader.onload = function(e){
            whereToShow.innerHTML += '<img src="'+this.result+'" alt="" style="height: 100px;width: 100px"/><br/>';
        }

    }

    this.arrayUnique = function(arr){
        var res = [];
        var json = {};
        for(var i = 0; i < arr.length; i++){
            if(!json[this[i]]){
                res.push(this[i]);
                json[this[i]] = 1;
            }
        }
        return res;
    }

});