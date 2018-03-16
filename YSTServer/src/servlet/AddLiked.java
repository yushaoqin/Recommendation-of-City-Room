package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.RoomComment;
import model.RoomDescription;
import model.UserRoom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.RoomService;
import service.UserService;


@Controller
@RequestMapping("/AddLiked")
public class AddLiked{
	@RequestMapping()
	public void addLiked(HttpServletRequest request, HttpServletResponse response){
		// TODO Auto-generated method stub
		System.out.println("ADD LIKED START");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		int userId = Integer.parseInt(request.getParameter("userId"));
		int roomId = Integer.parseInt(request.getParameter("roomId"));
		int type = Integer.parseInt(request.getParameter("type"));
		UserRoom userRoom = new UserRoom(userId,roomId,type);
		System.out.println(type);
		int result = -1;
		UserService usv = UserService.getIstance();
		result = usv.insertUserRoom(userRoom);
		
		System.out.println("ADD LIKED RESULT:"+result);
		
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(result+"");
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
