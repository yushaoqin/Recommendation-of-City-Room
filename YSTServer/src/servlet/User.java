package servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/User")
public class User {
	@RequestMapping(method=RequestMethod.GET,params="userId")
    public void getUser(HttpServletRequest request,HttpServletResponse response){
    	System.out.println(request.getParameter("userId"));
    }
	
	@RequestMapping(method=RequestMethod.POST,params={"username","password"})
    public void addUser(HttpServletRequest request,HttpServletResponse response){
    	System.out.println(request.getParameter("username"));
    	System.out.println(request.getParameter("password"));
    }
}
