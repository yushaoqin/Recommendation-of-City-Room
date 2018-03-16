package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.RoomComment;
import model.RoomDescription;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.RoomService;


@Controller
@RequestMapping("/AddInfo")
public class AddInfo {
    
    @RequestMapping()
    public void getAll(HttpServletRequest request,HttpServletResponse response){
		System.out.println("ADD INFO START");
		
		String username = request.getParameter("username");
		String text = request.getParameter("text");
		int roomId = Integer.parseInt(request.getParameter("roomId"));
		int type = Integer.parseInt(request.getParameter("type"));
		
		
		//处理中文乱码
		try {
			username = new String(username.getBytes("8859_1"), "utf8");
			text = new String(text.getBytes("8859_1"), "utf8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("TYPE:"+type); //0 comment 1 description
		System.out.println(username+"   "+text);
		int result = -1;
		RoomService rsv = RoomService.getIstance();
		if(type == 0 ){ //comment
			RoomComment rc = new RoomComment(username,text,roomId);
			result = rsv.insertRoomComment(rc);
		}else if( type == 1){ // description
			RoomDescription rd = new RoomDescription(username,text,roomId);
			result = rsv.insertRoomDescription(rd);
		}else{result = -1;}
		System.out.println("ADD INFO RESULT:"+result);
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

	    
		System.out.println("ADD INFO END");
		}
    
    }





