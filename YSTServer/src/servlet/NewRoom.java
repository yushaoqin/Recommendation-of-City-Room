//package servlet;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.math.BigDecimal;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import model.Room;
//import model.RoomDescription;
//import model.UserRoom;
//
//
//import service.RoomService;
//import service.UserService;
//
///**
// * Servlet implementation class NewRoom
// */
//@WebServlet("/NewRoom")
//public class NewRoom extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public NewRoom() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		System.out.println("New Room Start");
//		response.setHeader("Access-Control-Allow-Origin", "*");  //允许跨域
//		
//		String location_text = request.getParameter("location_text");
//		BigDecimal lat=new BigDecimal(request.getParameter("lat"));
//		BigDecimal lng=new BigDecimal(request.getParameter("lng"));
//		int userId = Integer.parseInt(request.getParameter("userId"));
//		
//		System.out.println("lat:"+lat);
//		String name = request.getParameter("name");
//		String description = request.getParameter("description");
//		
//		//解决乱码
//		name = new String(name.getBytes("8859_1"), "utf8");
//		location_text = new String(location_text.getBytes("8859_1"), "utf8");
//		description = new String(description.getBytes("8859_1"),"utf8");
//		
//		String [] tags = request.getParameterValues("tags");
//		String tag ="";
//		String username = request.getParameter("username");
//		for(int i =0;i<tags.length;i++){
//			System.out.print ("tags:"+new String(tags[i].getBytes("8859_1"),"utf8")+"    ");
//			if(i!=tags.length-1){
//				tag+=new String(tags[i].getBytes("8859_1"),"utf8")+",";	
//			}else{
//				tag+=new String(tags[i].getBytes("8859_1"),"utf8");
//			}
//		}
//		
//		System.out.println("END TAG:"+tag);
//		
//		RoomService rsv = RoomService.getIstance();
//		
//		Room room = new Room(name,location_text,lat,lng,tag,username);	
//		
//		int resultForInsertRoom = rsv.insertRoom(room);
//		System.out.println("INSERT ROOM RESULT:"+resultForInsertRoom);
//		
//		RoomDescription roomDescription = new RoomDescription(username,description,room.getId());
//		int resultForInsertRoomDescription = rsv.insertRoomDescription(roomDescription);
//		System.out.println("INSERT ROOM DESCRIPTION RESULT:"+resultForInsertRoomDescription);
//		
//		UserRoom userRoom = new UserRoom(userId,room.getId(),1);
//		UserService usv = UserService.getIstance();
//		int resultForInsertUserRoom = usv.insertUserRoom(userRoom);
//		System.out.println("INSERT USER ROOM RESULT:"+resultForInsertUserRoom);
//		
//		int result = -1;
//		if(resultForInsertRoom == 1 && resultForInsertRoomDescription ==1 && resultForInsertUserRoom ==1){
//			result = room.getId();
//		}
//		
//		
//		PrintWriter pw = response.getWriter();
//		pw.write(result+"");
//		pw.flush();
//		pw.close();
//	}
//
//}


package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Room;
import model.RoomDescription;
import model.UserRoom;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.RoomService;
import service.UserService;

@Controller
@RequestMapping("/NewRoom")
public class NewRoom {

	@RequestMapping()
	public void getAll(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("New Room Start");

		String location_text = request.getParameter("location_text");
		BigDecimal lat = new BigDecimal(request.getParameter("lat"));
		BigDecimal lng = new BigDecimal(request.getParameter("lng"));
		int userId = Integer.parseInt(request.getParameter("userId"));

		System.out.println("lat:" + lat);
		String name = request.getParameter("name");
		String description = request.getParameter("description");

		// 解决乱码
		try {
			name = new String(name.getBytes("8859_1"), "utf8");
			location_text = new String(location_text.getBytes("8859_1"), "utf8");
			description = new String(description.getBytes("8859_1"), "utf8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String[] tags = request.getParameterValues("tags");
		String tag = "";
		String username = request.getParameter("username");
		try {
			for (int i = 0; i < tags.length; i++) {

				System.out.print("tags:"
						+ new String(tags[i].getBytes("8859_1"), "utf8")
						+ "    ");

				if (i != tags.length - 1) {
					tag += new String(tags[i].getBytes("8859_1"), "utf8") + ",";
				} else {
					tag += new String(tags[i].getBytes("8859_1"), "utf8");
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("END TAG:" + tag);

		RoomService rsv = RoomService.getIstance();

		Room room = new Room(name, location_text, lat, lng, tag, username);

		int resultForInsertRoom = rsv.insertRoom(room);
		System.out.println("INSERT ROOM RESULT:" + resultForInsertRoom);

		RoomDescription roomDescription = new RoomDescription(username,
				description, room.getId());
		int resultForInsertRoomDescription = rsv
				.insertRoomDescription(roomDescription);
		System.out.println("INSERT ROOM DESCRIPTION RESULT:"
				+ resultForInsertRoomDescription);

		UserRoom userRoom = new UserRoom(userId, room.getId(), 1);
		UserService usv = UserService.getIstance();
		int resultForInsertUserRoom = usv.insertUserRoom(userRoom);
		System.out
				.println("INSERT USER ROOM RESULT:" + resultForInsertUserRoom);

		int result = -1;
		if (resultForInsertRoom == 1 && resultForInsertRoomDescription == 1
				&& resultForInsertUserRoom == 1) {
			result = room.getId();
		}

		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(result + "");
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
