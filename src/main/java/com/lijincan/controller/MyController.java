package com.lijincan.controller;


import com.lijincan.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * @author: lijincan
 * @date: 2020年02月26日 12:51
 * @Description: TODO
 */
@Controller
public class MyController {

    @Autowired
    UserService userService;

    @RequestMapping({"/","/index"})
    public String toIndex(Model model){
        model.addAttribute("msg","hello shiro");
        return "index";
    }

    //配置登出
    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/toLogin";
    }

    @RequestMapping("/user/add")
    public String add(){
        return "user/add";
    }
    @RequestMapping("/register")
    public String register(){
        return "register";
    }
    @RequestMapping("/user/update")
    public String update(){
        return "user/update";
    }

    @RequestMapping("/toLogin")
    public String tologin(){
        return "login";
    }
    @RequestMapping("/login")
    public String login(String username,String password,Model model){
        //获取权限操作对象，利用这个对象来完成登陆操作
        Subject subject = SecurityUtils.getSubject();

        //登陆前清除用户信息，是否如果之前登陆了用户信息会缓存在缓存中，之后每次重新登陆都会登陆成功
        subject.logout();

        //用户是否认证过（是否登陆过），进入if表示没有认证需要认证
        if(!subject.isAuthenticated()){
            //创建用户认证时的身份令牌，并设置我们从页面中传递过来的账号和密码
            UsernamePasswordToken token =new UsernamePasswordToken(username,password);
            try{
                //指定登陆，会自动调用我们Realm对象中的认证方法，如果登陆失败会抛出各种异常
                subject.login(token);
                return "index";
            }catch (UnknownAccountException e){//用户名不存在
                model.addAttribute("msg","用户名错误");
                return "login";
            }catch (IncorrectCredentialsException e){
                model.addAttribute("msg","密码错误");
                return "login";
            }
        }
        return "login";

    }

    @RequestMapping("/noauth")
    public String unauthorized(){
        return "未授权";
    }
}

