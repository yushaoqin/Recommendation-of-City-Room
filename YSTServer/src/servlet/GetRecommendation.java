package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Room;
import model.RoomComment;
import model.RoomDescription;
import model.UserRoom;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import recommendation.UserCF;
import service.RoomService;

@Controller
@RequestMapping("/GetRecommendation")
public class GetRecommendation {

	

	private JSONArray getResult(int number, List<Room> rooms,
			List<Integer> rooms_id) {
		JSONArray json = new JSONArray();
		int num = 0;
		if (rooms.size() < number) {
			num = rooms.size();
		} else {
			num = number;
		}
		int index = 0;
		for (int i = rooms_id.size() - 1; i >= rooms_id.size() - num; i--) {
			int room_id = rooms_id.get(i);
			for (Room room : rooms) {
				if (room.getId() == room_id) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("name", room.getName());
					jsonObj.put("id", room.getId());
					jsonObj.put("location", room.getLocation());
					jsonObj.put("lat", room.getLat());
					jsonObj.put("lng", room.getLng());
					jsonObj.put("tags", room.getTags());
					jsonObj.put("username", room.getUsername());
					jsonObj.put("index", index);
					json.put(jsonObj);
					index++;
					break;
				}
			}
		}

		return json;
	}

	@RequestMapping()
	public void getRecommendation(HttpServletRequest request,
			HttpServletResponse response) {
		System.out
				.println("----------------GET RECOMMENDATION START--------------");

		String username = request.getParameter("username");
		int user_state = Integer.parseInt(request.getParameter("state"));
		int user_id = Integer.parseInt(request.getParameter("id"));
		System.out.println(username + user_state + user_id);
		UserCF ucf = null;
		String result = "";
		JSONObject resultObj = new JSONObject();
		int result_number = 10;
		if (user_id == -1 || user_state == 0) {
			// 未登录,不显示推荐信息
			resultObj.put("status", -1);
			result = resultObj.toString();
		} else {
			ucf = UserCF.getIstance();
			RoomService rsv = RoomService.getIstance();
			try {
				Map<Integer, Double> possibility = ucf
						.getRecommendationByKSimilar(user_id, username);
				List<Integer> room_id = new ArrayList<Integer>();
				for (Integer key : possibility.keySet()) {
					room_id.add(key);
					System.out.println("ROOM ID:" + key);
					System.out.println("POSSIBILITY POINT:"
							+ possibility.get(key));
				}
				resultObj.put("status", "1");
				if (room_id.size() == 0) {
					result = resultObj.toString();
				}
				// 返回个数：
				result_number = (room_id.size() > 5) ? 5 : room_id.size();

				List<Room> rooms = rsv.selectRoomsByIdList(room_id);

				JSONArray json = getResult(result_number, rooms, room_id);
				resultObj.put("rooms", json);
				result = resultObj.toString();

			} catch (Exception e) {
				e.printStackTrace();
				resultObj.put("status", -2);
				result = resultObj.toString();
			} finally {
				ucf.exitNLPIR();
			}
		}

		System.out.println(result);
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(result);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out
					.println("----------------GET RECOMMENDATION END----------------");
		}

	}

}
