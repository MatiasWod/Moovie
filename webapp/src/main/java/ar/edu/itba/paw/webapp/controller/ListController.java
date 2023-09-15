package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.services.MoovieListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ListController {

    @Autowired
    private MoovieListService mediaListService;

    @Autowired
    private MediaService mediaService;

    @RequestMapping("/lists")
    public ModelAndView lists(){
        final ModelAndView mav = new ModelAndView("helloworld/viewLists");
        List<MoovieList> moovieLists = mediaListService.geAllMoovieLists();
        mav.addObject("moovieLists", moovieLists);
        return mav;
    }


    @RequestMapping("/createList")
    public ModelAndView createList(){
        List<Media> media;
        final ModelAndView mav = new ModelAndView("helloworld/createList");
        media = mediaService.getMoovieList();
        mav.addObject("mediaList",media);
        return mav;
    }
}
