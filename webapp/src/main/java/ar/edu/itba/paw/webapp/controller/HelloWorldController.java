package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.ActorService;
import ar.edu.itba.paw.services.GenreService;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class HelloWorldController {
    @Autowired
    private MediaService mediaService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private GenreService genreService;
    private final UserService us;

    @Autowired
    public HelloWorldController(final UserService us) {
        this.us = us;
    }

    @RequestMapping("/test")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("helloworld/index");
        mav.addObject("user", us.createUser("paw@itba.edu.ar"));
        return mav;
    }

    @RequestMapping("/discover")
    public ModelAndView discover() {
        final ModelAndView mav = new ModelAndView("helloworld/discover");
        return mav;
    }

    @RequestMapping("/{id:\\d+}")
    public ModelAndView profile(@PathVariable("id") final long userId) {
        final ModelAndView mav = new ModelAndView("helloworld/profile");
        mav.addObject("userid", userId);
        return mav;
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ModelAndView register(@RequestParam(value = "email",required = true) final String email,
                                 @RequestParam(value = "password",required = true) final String password){
        final User user = us.createUser(email);
        final ModelAndView mav = new ModelAndView("helloworld/index");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerForm() {
        return new ModelAndView("helloworld/register");
    }


    @RequestMapping("/details/{id:\\d+}")
    public ModelAndView details(@PathVariable("id") final int mediaId) {
        final ModelAndView mav = new ModelAndView("helloworld/details");
        final Optional<Movie> media = mediaService.getMovieById(mediaId);
        final List<Actor> actorsList = actorService.getAllActorsForMedia(mediaId);
        final List<Genre> genresList =genreService.getGenreForMedia(mediaId);
        if (media.isPresent())
            mav.addObject("media", media.get());
        else
            mav.addObject("media", null);
        mav.addObject("actorsList", actorsList);
        mav.addObject("genresList", genresList);
        return mav;
    }
}
