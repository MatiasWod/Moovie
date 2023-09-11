package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Genre.MovieGenre;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.services.GenreService;
import ar.edu.itba.paw.services.MovieService;
import ar.edu.itba.paw.services.TVSerieService;
import com.sun.tools.javac.jvm.Gen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private TVSerieService tvSerieService;
    @Autowired
    private GenreService genreService;

    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("helloworld/test");
        final List<Movie> movieList = movieService.getMovieList();
        final List<TVSerie> tvList = tvSerieService.getTvList();
        mav.addObject("movieList", movieList);
        mav.addObject("tvList", tvList);
        return mav;
    }

}
