package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Room;
import model.RoomComment;
import model.RoomDescription;
import model.RoomImage;
import model.RoomPoint;
import model.UserRoom;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.RoomService;
import service.UserService;


@Controller
@RequestMapping("/GetRoomInfo")
public class GetRoomInfo {
    
    @RequestMapping()
    public void getAll(HttpServletRequest request,HttpServletResponse response){

    	System.out.println("GET ROOM INFO START");
//    	
//    	response.setHeader("Access-Control-Allow-Origin", "*");
//    	response.setCharacterEncoding("utf-8");
//    	
    	int id = Integer.parseInt(request.getParameter("id"));
    	System.out.println("GET ROOM ID:"+id);
    	RoomService rsv = RoomService.getIstance();
    	
    	Room room = rsv.selectRoomById(id);
    	
    	JSONObject jsonObj = new JSONObject();
    	jsonObj.put("name",room.getName());
    	jsonObj.put("id",room.getId());
    	jsonObj.put("location",room.getLocation());
    	jsonObj.put("lat",room.getLat());
    	jsonObj.put("lng",room.getLng());
    	jsonObj.put("tags",room.getTags());
    	jsonObj.put("username",room.getUsername());
    	
    	
    	//获取DESCRIPTION
    	List<RoomDescription> roomDescription = rsv.selectRoomDescriptionByRoomId(id);
    	JSONArray rdJsonArray = new JSONArray();
    	for(int i=0;i<roomDescription.size();i++){
    		RoomDescription rd = roomDescription.get(i);
    		JSONObject tmp = new JSONObject();
    		tmp.put("username", rd.getUsername());
    		tmp.put("description", rd.getDescription());
    		rdJsonArray.put(tmp);
    	}
    	jsonObj.put("description", rdJsonArray);
    	
    	//获取COMMENT
    	List<RoomComment> roomComment = rsv.selectRoomCommentByRoomId(id);
    	JSONArray rcJsonArray = new JSONArray();
    	for(int i=0;i<roomComment.size();i++){
    		RoomComment rc = roomComment.get(i);
    		JSONObject tmp = new JSONObject();
    		tmp.put("username", rc.getUsername());
    		tmp.put("content",rc.getComment());
    		rcJsonArray.put(tmp);
    	}
    	jsonObj.put("comment", rcJsonArray);
    	
    	//获取LIKE数量
    	
    	UserService usv = UserService.getIstance();
    	List <UserRoom> userRooms = null;
    	userRooms = usv.selectUserRoomByRoomIdAndType(id, 2);
    	if(userRooms == null){
    	
    		jsonObj.put("likes",0);
    	}else{
    	
    		jsonObj.put("likes", userRooms.size());	
    	}
    	//获取room image
    	List<RoomImage> roomImage = rsv.selectRoomImageByRoomId(id);
    	JSONArray riJsonArray = new JSONArray();
    	for(int i=0;i<roomImage.size();i++){
    		RoomImage ri = roomImage.get(i);
    		JSONObject tmp = new JSONObject();
    		tmp.put("username", ri.getUsername());
    		tmp.put("imgPath",ri.getImgPath());
    		riJsonArray.put(tmp);
    	}
    	jsonObj.put("img", riJsonArray);
    	
    	//获取room Point
    	List<RoomPoint> roomPoints = rsv.selectRoomPointByRoomId(id);
    	int room_point_count = roomPoints.size();
    	double value = 0.0;
    	for(int i=0;i<roomPoints.size();i++){
    		RoomPoint rp = roomPoints.get(i);
    		value += rp.getValue();
    	}
		JSONObject tmp = new JSONObject();
		DecimalFormat    df   = new DecimalFormat("######0.0");  
		if(room_point_count ==0){
			tmp.put("value", 0);
		}else{
			tmp.put("value", df.format(value/room_point_count));
			}
		
		tmp.put("count",room_point_count);
    	jsonObj.put("point", tmp);
    	
    	String result = jsonObj.toString();
    	PrintWriter pw;
		try {
			pw = response.getWriter();
	    	pw.write(result);
	    	pw.flush();
	    	pw.close();
	    	System.out.println("-----------GET ROOM INFO END-----------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
    
    
    
    
}



