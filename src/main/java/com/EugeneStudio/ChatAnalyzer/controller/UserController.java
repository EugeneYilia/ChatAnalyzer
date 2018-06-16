package com.EugeneStudio.ChatAnalyzer.controller;

import com.EugeneStudio.ChatAnalyzer.pojo.UserStatus;
import com.EugeneStudio.ChatAnalyzer.service.UserInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller("userController")
@RequestMapping("/user")
public class UserController {
    @Autowired
    @Resource(name = "UserService")
    private UserInformationService userInformationService;

    @ResponseBody
    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public Object login(@RequestBody Map<String, Object> requestMap, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods","POST");
        System.out.println("收到请求");
        String id = requestMap.get("id").toString();
        String password = requestMap.get("password").toString();
        System.out.println(id + "  " + password);
        UserStatus userStatus = new UserStatus(userInformationService.findUser(id, password));
        return userStatus;
    }
}
