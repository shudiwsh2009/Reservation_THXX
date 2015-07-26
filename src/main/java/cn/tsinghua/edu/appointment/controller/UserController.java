package cn.tsinghua.edu.appointment.controller;

import cn.tsinghua.edu.appointment.domain.User;
import cn.tsinghua.edu.appointment.domain.UserType;
import cn.tsinghua.edu.appointment.exception.BasicException;
import cn.tsinghua.edu.appointment.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public void register(@RequestParam("username") String _username,
                         @RequestParam("password") String _password,
                         HttpServletResponse response, ModelMap model) throws IOException {
        UserRepository ur = new UserRepository();
        try {
            ur.register(_username, _password);
            response.sendRedirect("/appointment/login");
        } catch (BasicException e) {
            model.addAttribute("message", e.getInfo());
            response.sendRedirect("/appointment/register");
        }
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public void login(@RequestParam("username") String _username,
                      @RequestParam("password") String _password,
                      HttpServletResponse response,
                      HttpSession session) throws IOException {

        JSONObject result = new JSONObject();
        UserRepository ur = new UserRepository();
        try {
            User user = ur.login(_username, _password);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", _username);
            session.setAttribute("userType", user.getUserType());
            if (user.getUserType() == UserType.ADMIN) {
                result.put("url", "admin");
            } else if (user.getUserType() == UserType.TEACHER) {
                result.put("url", "teacher");
            } else {
                result.put("url", "/");
            }
            result.put("state", "SUCCESS");
        } catch (BasicException e) {
            result.put("state", "FAILED");
            result.put("url", "login");
        }

        // send response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.write(result.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public void login(HttpServletResponse response,
                      HttpSession session) throws IOException {
        session.removeAttribute("userId");
        session.removeAttribute("username");
        session.removeAttribute("userType");
        response.sendRedirect("/appointment");
    }

}
