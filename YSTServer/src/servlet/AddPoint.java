package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.RoomComment;
import model.RoomDescription;
import model.RoomPoint;
import model.UserRoom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.RoomService;
import service.UserService;


@Controller
@RequestMapping("/AddPoint")
public class AddPoint{
	@RequestMapping()
	public void addLiked(HttpServletRequest request, HttpServletResponse response){
		// TODO Auto-generated method stub
		System.out.println("ADD Point START");
		
		int userId = Integer.parseInt(request.getParameter("userId"));
		int roomId = Integer.parseInt(request.getParameter("roomId"));
		double value = Double.parseDouble(request.getParameter("point"));
		RoomPoint roomPoint = new RoomPoint(userId,roomId,value);
		System.out.println("USER:"+userId+" ROOM:"+roomId+" VALUE:"+value);
		int result = -1;
		RoomService rsv = RoomService.getIstance();
		result = rsv.insertRoomPoint(roomPoint, userId, roomId);
		
		System.out.println("ADD POINT RESULT:"+result);
		
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
