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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public String createListAction(@RequestParam(value = "userEmail", required = true) final String userEmail,
                                   @RequestParam(value = "mediaIds", required = true) final List<String> mediaIds,
                                   @RequestParam(value = "listName", required = true) final String name,
                                   @RequestParam(value = "listDescription", required = true) final String description){
        if(! userEmail.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[A-Za-z]{2,}")){
            return "redirect:/createList?error=invalidEmail";
        }


        List<Integer> finalIds = new ArrayList<>();
        for (String id : mediaIds) {
            String numericPart = extractNumericPart(id);
            if (numericPart != null) {
                int mediaId = Integer.parseInt(numericPart);
                finalIds.add(mediaId);
            }
        }

        User user = userService.getOrCreateUserViaMail(userEmail);

        MoovieList list = moovieListService.createMoovieListWithContent(user.getUserId(),name,description,finalIds);

        int id = list.getMoovieListId();
        return ("redirect:/createList/" + id);
    }

// http://tuDominio.com/createList?s=A&s=B&s=C&s=D&s=E
    @RequestMapping("/createList")
    public ModelAndView createList(@RequestParam(value = "g", required = false) String genre,
                                   @RequestParam(value = "m", required = false) String media,
                                   @RequestParam(value = "q", required = false) String query,
                                   @RequestParam(value = "s", required = false) List<String> selected){

        List<Movie> movieList = null;
        List<TVSerie> tvSerieList = null;
        List<Media> mediaList = null;
        List<Media> searchList;

        final ModelAndView mav = new ModelAndView("helloworld/createList");

        if (genre != null && !genre.isEmpty()) {
            if (media != null && media.equals("Movies")) {
                movieList = mediaService.getMovieFilteredByGenre(genre);
            } else if (media != null && media.equals("Series")) {
                tvSerieList = mediaService.getTvFilteredByGenre(genre);
            } else {
                mediaList = mediaService.getMediaFilteredByGenre(genre);
            }
        } else if (media != null && media.equals("Movies")){
            movieList = mediaService.getMovieList();
        } else if (media != null && media.equals("Series")){
            tvSerieList = mediaService.getTvList();
        } else {
            mediaList = mediaService.getMoovieList();
        }

        if( query != null && !query.isEmpty()){
            searchList = mediaService.getMediaBySearch(query);
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

            List<Media> mediaList = mediaService.getMediaByMoovieListId(moovieListId);
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
