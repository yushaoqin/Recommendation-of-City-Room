package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.RoomImage;
import service.RoomService;

/**
 * Servlet implementation class UploadImage
 */
@WebServlet("/UploadImage")
//@MultipartConfig(location="/root/tomcat/apache-tomcat-7.0.77/webapps/yst_image/image")
@MultipartConfig(location="D://program/eclipse-4.4-workspcae/YSTServer/WebContent/image")
public class UploadImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadImage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		
		System.out.println("UPLOAD IMAGE START");

		request.setCharacterEncoding("UTF-8");  //对客户端请求重新编码
		String serverUrl =  "http://192.168.0.102:8080/";
		//获取文件路径
		Part part = request.getPart("file");
				
		//将当前系统时间作为文件的名称避免重名
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String filename = sdf.format(new Date())+".jpg";
		part.write(filename);
		System.out.println("文件名："+filename);
		
		//读取username,roomId
		String tmp = "";
		Map readOnlyMap = request.getParameterMap(); 
		for (Object key : readOnlyMap.keySet()) {
		String a =  request.getParameter(""+key);
		tmp+= a;
		}
		System.out.println("");
		System.out.println("tmp:"+tmp);
		String[]tmp1 = tmp.split(",");
		String username = tmp1[0];
		int roomId = Integer.parseInt(tmp1[1]);
		System.out.println("roomId:"+roomId);
		System.out.println("username:"+username);
		
		String imgPath = filename;
				
		RoomImage roomImage = new RoomImage(roomId,username,imgPath);
		RoomService rsv = RoomService.getIstance();
		
		int result = rsv.insertRoomImage(roomImage);
		
		System.out.println("result:"+result);
		System.out.println("UPLOAD IMAGE END");
		
		PrintWriter pw = response.getWriter();
		pw.write("a");
		pw.flush();
		pw.close();
	}

}
