package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.UserService;

@Controller
@RequestMapping("/Register")
public class Register {

	@RequestMapping()
	public void getAll(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Register Start");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserService usv = UserService.getIstance();
		User user = new User();
		user.setPwd(password);
		user.setUsername(username);
		int result = usv.insertUser(user);
		System.out.println("result:" + result); //1 sucess -1 failed,already exists

		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.print(result);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
