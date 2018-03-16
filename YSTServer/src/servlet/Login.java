package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Room;
import model.User;
import model.UserRoom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.RoomService;
import service.UserService;

@Controller
@RequestMapping("/Login")
public class Login {

	@RequestMapping()
	public void getAll(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = null;
		JSONObject userJsonObj = new JSONObject();
		JSONArray roomsLikedJsonArray = new JSONArray();
		JSONArray roomsSharedJsonArray = new JSONArray();
		JSONArray resultJsonArray = new JSONArray();
		UserService usv = UserService.getIstance();
		user = usv.selectUserByUsernameAndPassword(username, password);
		if (user == null) {
			System.out.println("Login Failed");
			userJsonObj.put("state", -1);
		} else {
			System.out.println("Login Success");
			System.out.println("Login user id:" + user.getId());

			// 获取到用户以后根据用户ID获取用户相关的空间信息
			List<UserRoom> userRooms = usv.selectUserRoomByUserId(user.getId());
			RoomService rsv = RoomService.getIstance();
			List<Integer> roomsLikedIds = new ArrayList<Integer>();
			List<Integer> roomsSharedIds = new ArrayList<Integer>();
			List<Room> roomsLiked = new ArrayList<Room>();
			List<Room> roomsShared = new ArrayList<Room>();
			// 根据type： 1分享 2喜欢分类
			for (int i = 0; i < userRooms.size(); i++) {

				int type = userRooms.get(i).getType();

				int roomId = userRooms.get(i).getRoomId();

				if (type == 1) {
					roomsSharedIds.add(roomId);
				}
				if (type == 2) {
					roomsLikedIds.add(roomId);
				}
			}
			if (roomsSharedIds.size() != 0) {
				roomsShared = rsv.selectRoomsByIdList(roomsSharedIds);
			}
			if (roomsLikedIds.size() != 0) {
				roomsLiked = rsv.selectRoomsByIdList(roomsLikedIds);
			}
			for (Room room : roomsShared) {
				JSONObject tmp = new JSONObject();
				tmp.put("id", room.getId());
				tmp.put("name", room.getName());
				tmp.put("location", room.getLocation());
				tmp.put("username", room.getUsername());
				tmp.put("tags", room.getTags());
				tmp.put("lat", room.getLat());
				tmp.put("lng", room.getLng());
				roomsSharedJsonArray.put(tmp);
			}
			for (Room room : roomsLiked) {
				JSONObject tmp = new JSONObject();
				tmp.put("id", room.getId());
				tmp.put("name", room.getName());
				tmp.put("location", room.getLocation());
				tmp.put("username", room.getUsername());
				tmp.put("tags", room.getTags());
				tmp.put("lat", room.getLat());
				tmp.put("lng", room.getLng());
				roomsLikedJsonArray.put(tmp);
			}

			try {
				userJsonObj.put("username", user.getUsername());
				userJsonObj.put("id", user.getId());
				userJsonObj.put("password", user.getPwd());
				userJsonObj.put("state", 1);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		resultJsonArray.put(userJsonObj);
		resultJsonArray.put(roomsSharedJsonArray);
		resultJsonArray.put(roomsLikedJsonArray);

//		
//		JSONObject tmppp = new JSONObject();
////		resultJsonArray = new JSONArray();
////		resultJsonArray.put(tmppp);
////		tmppp.put("state", -5);
		String loginResult = resultJsonArray.toString();
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.print(loginResult);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("LOGIN END");

	}


}





