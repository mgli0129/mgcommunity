package com.mg.community.controller;

import com.mg.community.enums.NotificationTypeEnum;
import com.mg.community.model.Notification;
import com.mg.community.model.User;
import com.mg.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/notify/{id}", method = RequestMethod.GET)
    public String readNotifyMessage(@PathVariable("id") Long id, Model model, HttpServletRequest request){
        Object user = request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }
        //标记通知已读
        notificationService.readNotify(id);
        Notification notification = notificationService.findById(id);
        if(NotificationTypeEnum.nameOfType(notification.getType()) != null){
            return "redirect:/question/"+notification.getQuestionid();
        }else{
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public String clearNotifications(Model model, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        notificationService.clearByReceiver(user);
        return "redirect:/question/reply";
    }
}
