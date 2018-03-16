package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
@RequestMapping("/Search")
public class Search {

	@RequestMapping()
	public void getAll(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("SEARCH START");
		String keyword = request.getParameter("keyword");
		try {
			keyword = new String(keyword.getBytes("8859_1"), "utf8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(keyword);

		RoomService rsv = RoomService.getIstance();
		List<Room> rooms = rsv.selectRoomByKeyWord(keyword);
		System.out.println(rooms.size());
		JSONArray json = new JSONArray();
		if (rooms != null) {
			for (Room room : rooms) {
				System.out.println(room.getName());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("name", room.getName());
				jsonObj.put("id", room.getId());
				jsonObj.put("location", room.getLocation());
				jsonObj.put("lat", room.getLat());
				jsonObj.put("lng", room.getLng());
				jsonObj.put("tags", room.getTags());
				jsonObj.put("username", room.getUsername());
				json.put(jsonObj);
			}
		}

		String result = json.toString();

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

		System.out.println("SEARCH END");
	}

}
