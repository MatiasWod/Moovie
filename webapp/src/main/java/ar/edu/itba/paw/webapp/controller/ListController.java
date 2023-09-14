package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.services.MoovieListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ListController {

    @Autowired
    private MoovieListService mediaListService;

    @RequestMapping("/lists")
    public ModelAndView lists(){
        final ModelAndView mav = new ModelAndView("helloworld/viewLists");
        List<MoovieList> moovieLists = mediaListService.geAllMoovieLists();
        mav.addObject("moovieLists", moovieLists);
        return mav;
    }


    @RequestMapping("/createList")
    public ModelAndView createList(){
        final ModelAndView mav = new ModelAndView("helloworld/createList");
        return mav;
    }
}
