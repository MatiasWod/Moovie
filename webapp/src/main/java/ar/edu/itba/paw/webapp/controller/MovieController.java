package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Movie.Movie;
import ar.edu.itba.paw.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;


    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("helloworld/test");
        final List<Movie> movieList = movieService.getMovieList();
        mav.addObject("movieList", movieList);
        return mav;
    }

}
