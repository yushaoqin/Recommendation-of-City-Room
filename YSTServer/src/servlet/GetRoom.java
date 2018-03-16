package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Room;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.RoomService;


@Controller
@RequestMapping("/GetAllRooms")
public class GetRoom {
    
    @RequestMapping()
    public void getAll(HttpServletRequest request,HttpServletResponse response){
	System.out.println("GET  ALL ROOM START");
		
		RoomService rsv = RoomService.getIstance();
		List<Room> rooms = new ArrayList<Room>();
		rooms = rsv.selectAllRoom();
		
		JSONArray json= new JSONArray();
		for(int i =0;i<rooms.size();i++){
			JSONObject jsonObj = new JSONObject(); 
			Room room = rooms.get(i);
			jsonObj = jsonObj.put("name",room.getName());
			jsonObj = jsonObj.put("id",room.getId());
			jsonObj = jsonObj.put("location",room.getLocation());
			jsonObj = jsonObj.put("lat",room.getLat());
			jsonObj = jsonObj.put("lng",room.getLng());
			jsonObj = jsonObj.put("tags",room.getTags());
			jsonObj = jsonObj.put("username",room.getUsername());
			json.put(jsonObj);
		}
		String result = json.toString();		
		System.out.println("共有"+json.length()+"条room数据");
		
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(result);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("GET  ALL ROOM END");
    }
    
    
    
    
}




