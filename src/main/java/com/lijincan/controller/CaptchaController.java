package com.lijincan.controller;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.lijincan.config.SmsDemo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class CaptchaController {

    private Map<String,String> codeList=new ConcurrentHashMap();
    private Map<String,Long> timeList=new ConcurrentHashMap<>();
    @RequestMapping(value = "/sendMsg")
    public @ResponseBody
    Map<String, Object> sendMsg(@RequestBody Map<String,Object> requestMap) throws ClientException, InterruptedException {
        String phoneNumber = requestMap.get("phoneNumber").toString();
//        String randomNum = UUID.randomUUID().toString().substring(0, 4);// 生成随机数


        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 5);
        long time = c.getTime().getTime();
        timeList.put(phoneNumber,time);

        //发短信
        SendSmsResponse response = SmsDemo.sendSms(phoneNumber);

        Thread.sleep(3000L);
        //查明细
        if (response.getCode() != null && response.getCode().equals("OK")) {
            QuerySendDetailsResponse querySendDetailsResponse = SmsDemo.querySendDetails(response.getBizId(),phoneNumber);
            System.out.println("Message=" + querySendDetailsResponse.getMessage());
            for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs()) {
                String[] split = smsSendDetailDTO.getContent().split("：");
                String code = split[1].substring(0, 4);
                System.out.println("验证码" + code);
                codeList.put(phoneNumber,code);
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("hash", hash);
        requestMap.put("code",200);
        return resultMap; //将hash值和tamp时间返回给前端
    }

    @RequestMapping(value = "/validateNum", method = RequestMethod.POST)
    public String validateNum(String phone,String code,Model model) {
        System.out.println("phone"+"code"+phone+"\t"+code);
        long time = (Long)new Date().getTime()!=null?new Date().getTime():0;
        Long phone1 = timeList.get(phone);
        System.out.println("phone1"+phone);
        if(phone1<time){
            model.addAttribute("message","验证码超时");
            return "register";
        }
        String realyCode = codeList.get(phone)!=null?codeList.get(phone):"";
        if(!realyCode.equals(code)){
            model.addAttribute("message","验证码错误");
            return "register";
        }
        System.out.println("成功");
        return "login";
    }
}
