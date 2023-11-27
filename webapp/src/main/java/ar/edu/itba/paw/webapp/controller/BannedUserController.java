package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.BannedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BannedUserController {
    @Autowired
    BannedService bannedService;

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(ListController.class);

    @RequestMapping(value = "/bannedMessage/{id:\\d+}", method = RequestMethod.GET)
    public ModelAndView bannedMessage( @PathVariable("id") final int userId) {
        ModelAndView mav = new ModelAndView("main/bannedMessage");
        LOGGER.info("Attempting to GET bannedMessafe page with id: {} for /bannedMessage.", userId);
        try {
            mav.addObject("bannedMessageObject", bannedService.getBannedMessage(userId));
            LOGGER.info("Returned bannedMessafe page with id: {} for /bannedMessage..", userId);
        } catch (Exception e) {
            LOGGER.info("Failed to return bannedMessafe page with id: {} for /bannedMessage..", userId);
        }
        return mav;
    }
}
