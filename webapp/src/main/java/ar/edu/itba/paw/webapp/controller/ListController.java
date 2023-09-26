package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.Utils.Tuple;
import ar.edu.itba.paw.services.GenreService;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.CreateListForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class ListController {

    @Autowired
    private MoovieListService mediaListService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private MoovieListService moovieListService;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ListController.class);

    @RequestMapping("/lists")
    public ModelAndView lists(
            @RequestParam(value = "search", required = false) final String searchQ){

        final ModelAndView mav = new ModelAndView("helloworld/viewLists");
        List<MoovieList> moovieLists = mediaListService.geAllMoovieLists();

        Map<User, Tuple<MoovieList,List<String>>> tupleHashMap = new HashMap<>();


        moovieLists.forEach(list -> {
            if (( searchQ == null || searchQ.isEmpty() ) || (list.getName().toLowerCase().contains(searchQ.toLowerCase()))){
                Optional<User> optionalUser = userService.findUserById(list.getUserId());
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    List<String> innerList = new ArrayList<>();

                    moovieListService.getMoovieListContentById(list.getMoovieListId())
                            .subList(0,Math.min(4,moovieListService.getMoovieListContentById(list.getMoovieListId()).size())).forEach(moovieListContent -> {
                                innerList.add(mediaService.getMediaById(moovieListContent.getMediaId()).get().getPosterPath());
                            });

                    Tuple<MoovieList,List<String>> innerTuple = new Tuple<>(list,innerList);
                    tupleHashMap.put(user,innerTuple);
                }
            }
        });

        mav.addObject("mapTuple", tupleHashMap);
        return mav;
    }

    private String extractNumericPart(String input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/createListAction", method = RequestMethod.POST)
    public ModelAndView createListAction(@Valid @ModelAttribute("ListForm") final CreateListForm form, final BindingResult errors){

        if(errors.hasErrors()){
            return  createList(null,null,null,null,form);
        }
        User user = userService.getOrCreateUserViaMail(form.getUserEmail());

        MoovieList list = moovieListService.createMoovieListWithContent(user.getUserId(),form.getListName(),form.getListDescription(),form.getMediaIdsList());

        int id = list.getMoovieListId();
        return new ModelAndView("redirect:/list/"+id);
    }

// http://tuDominio.com/createList?s=A&s=B&s=C&s=D&s=E

    @RequestMapping("/createList")
    public ModelAndView createList(@RequestParam(value = "g", required = false) List<String> genre,
                                   @RequestParam(value = "m", required = false) String media,
                                   @RequestParam(value = "q", required = false) String query,
                                   @RequestParam(value = "s", required = false) List<String> selected,
                                   @ModelAttribute("ListForm") final CreateListForm form){

        List<Movie> movieList = null;
        List<TVSerie> tvSerieList = null;
        List<Media> mediaList = null;
        List<Media> searchList;

        final ModelAndView mav = new ModelAndView("helloworld/createList");

        if (genre != null && !genre.isEmpty()) {
            if (media != null && media.equals("Movies")) {
                movieList = mediaService.getMovieFilteredByGenreList(genre, mediaService.DEFAULT_PAGE_SIZE, 0);
            } else if (media != null && media.equals("Series")) {
                tvSerieList = mediaService.getTvFilteredByGenreList(genre, mediaService.DEFAULT_PAGE_SIZE, 0);
            } else {
                mediaList = mediaService.getMediaFilteredByGenreList(genre, mediaService.DEFAULT_PAGE_SIZE, 0);
            }
        } else if (media != null && media.equals("Movies")){
            movieList = mediaService.getMovieList(mediaService.DEFAULT_PAGE_SIZE, 0);
        } else if (media != null && media.equals("Series")){
            tvSerieList = mediaService.getTvList(mediaService.DEFAULT_PAGE_SIZE, 0);
        } else {
            mediaList = mediaService.getMoovieList(mediaService.DEFAULT_PAGE_SIZE, 0);
        }

        if( query != null && !query.isEmpty()){
            searchList = mediaService.getMediaBySearch(query, mediaService.DEFAULT_PAGE_SIZE, 0);
            if (movieList != null){
                List<Integer> mediaIdList = movieList.stream()
                        .map(Media::getMediaId)
                        .collect(Collectors.toList());

                searchList.removeIf(aux -> !mediaIdList.contains(aux.getMediaId()));
            } else if (tvSerieList != null) {
                List<Integer> mediaIdList = tvSerieList.stream()
                        .map(Media::getMediaId)
                        .collect(Collectors.toList());

                searchList.removeIf(aux -> !mediaIdList.contains(aux.getMediaId()));
            } else if (mediaList != null) {
                List<Integer> mediaIdList = mediaList.stream()
                        .map(Media::getMediaId)
                        .collect(Collectors.toList());

                searchList.removeIf(aux -> !mediaIdList.contains(aux.getMediaId()));

            }
            mav.addObject("mediaList",searchList);
        }else{
            if (movieList != null){
                mav.addObject("mediaList",movieList);
            } else if (tvSerieList != null) {
                mav.addObject("mediaList",tvSerieList);
            } else if (mediaList != null) {
                mav.addObject("mediaList",mediaList);
            }
        }

        if (selected != null && !selected.isEmpty()) {
            List<Media> selectedMedia = new ArrayList<>();
            for (String id : selected) {
                String numericPart = extractNumericPart(id);
                if (numericPart != null) {
                    int mediaId = Integer.parseInt(numericPart);
                    Media aux = mediaService.getMediaById(mediaId).get();
                    selectedMedia.add(aux);
                }
            }
            mav.addObject("selected", selectedMedia);
        }

        List<String> genres = genreService.getAllGenres();
        mav.addObject("genresList", genres);
        return mav;
    }

    @RequestMapping("/list/{id:\\d+}")
    public ModelAndView list(@PathVariable("id") final int moovieListId) {
        Optional<MoovieList> moovieListData = moovieListService.getMoovieListById(moovieListId);
        if (moovieListData.isPresent()) {
            final ModelAndView mav = new ModelAndView("helloworld/moovieList");
            mav.addObject("moovieList", moovieListData.get());

            List<Media> mediaList = mediaService.getMediaByMoovieListId(moovieListId, mediaService.DEFAULT_PAGE_SIZE, 0);
            List<MoovieListContent> moovieListContent = moovieListService.getMoovieListContentById(moovieListId);
            String listOwner = userService.findUserById(moovieListData.get().getUserId()).get().getEmail();

            mav.addObject("mediaList", mediaList);
            mav.addObject("moovieListContent", moovieListContent);
            mav.addObject("listOwner", listOwner);
            return mav;
        } else {
            final ModelAndView mav = new ModelAndView("helloworld/404.jsp");
            mav.addObject("extraInfo", "The list with id: " +moovieListId+ " doesn't exists");
            return mav;
        }
    }
}
