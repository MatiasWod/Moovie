package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.BannedService;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BannedUserController {
    @Autowired
    BannedService bannedService;

    @RequestMapping(value = "/bannedMessage", method = RequestMethod.GET)
    public ModelAndView bannedMessage() {
        ModelAndView mav = new ModelAndView("helloworld/bannedMessage");
        mav.addObject("bannedMessageObject", bannedService.getBannedMessage());
        return mav;
    }
}
