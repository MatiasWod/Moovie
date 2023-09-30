package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.extendedMoovieList;
import ar.edu.itba.paw.models.User.User;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
            @RequestParam(value = "search", required = false) final String searchQ) {

        final ModelAndView mav = new ModelAndView("helloworld/viewLists");
        List<MoovieList> moovieLists = null;
        if (searchQ != null && !searchQ.isEmpty()) {
            moovieLists = mediaListService.getMoovieListBySearch(searchQ, 24, 0);
        } else {
            moovieLists = mediaListService.getAllMoovieLists(24, 0);
        }
        ArrayList<extendedMoovieList> showLists = new ArrayList<extendedMoovieList>();
        for (MoovieList movieList : moovieLists) {
            List<Media> mediaList = mediaService.getMoovieListContentByIdMediaBUpTo(movieList.getMoovieListId(), 4);
            String[] posters = new String[mediaList.size()];
            for (int i = 0; i < mediaList.size(); i++) {
                posters[i] = mediaList.get(i).getPosterPath();
            }
            showLists.add(new extendedMoovieList(movieList, userService.findUserById(movieList.getUserId()).get().getUsername(),
                    moovieListService.getLikesCount(movieList.getMoovieListId()).get()
                    , mediaListService.getMoovieListSize(movieList.getMoovieListId(), false).get(), mediaListService.getMoovieListSize(movieList.getMoovieListId(), true).get(), posters));

        }
        mav.addObject("showLists", showLists);

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
    public ModelAndView createListAction(@Valid @ModelAttribute("ListForm") final CreateListForm form, final BindingResult errors) {

        if (errors.hasErrors()) {
            return createList(null, null, null, null, form);
        }

        MoovieList list = moovieListService.createStandardPublicMoovieListWithContent(form.getListName(), form.getListDescription(), form.getMediaIdsList());

        int id = list.getMoovieListId();
        return new ModelAndView("redirect:/list/" + id);
    }

// http://tuDominio.com/createList?s=A&s=B&s=C&s=D&s=E

    @RequestMapping("/createList")
    public ModelAndView createList(@RequestParam(value = "g", required = false) List<String> genre,
                                   @RequestParam(value = "m", required = false) String media,
                                   @RequestParam(value = "q", required = false) String query,
                                   @RequestParam(value = "s", required = false) List<String> selected,
                                   @ModelAttribute("ListForm") final CreateListForm form) {

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
        } else if (media != null && media.equals("Movies")) {
            movieList = mediaService.getMovieList(mediaService.DEFAULT_PAGE_SIZE, 0);
        } else if (media != null && media.equals("Series")) {
            tvSerieList = mediaService.getTvList(mediaService.DEFAULT_PAGE_SIZE, 0);
        } else {
            mediaList = mediaService.getMoovieList(mediaService.DEFAULT_PAGE_SIZE, 0);
        }

        if (query != null && !query.isEmpty()) {
            searchList = mediaService.getMediaBySearch(query, mediaService.DEFAULT_PAGE_SIZE, 0);
            if (movieList != null) {
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
            mav.addObject("mediaList", searchList);
        } else {
            if (movieList != null) {
                mav.addObject("mediaList", movieList);
            } else if (tvSerieList != null) {
                mav.addObject("mediaList", tvSerieList);
            } else if (mediaList != null) {
                mav.addObject("mediaList", mediaList);
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

    @RequestMapping("/profile/{username}/watchedList")
    public ModelAndView watchedlist(@PathVariable("username") final String username) {
        return getPrivateListMav("Watched");
    }

    @RequestMapping("/profile/{username}/watchList")
    public ModelAndView watchlist(@PathVariable("username") final String username) {
        return getPrivateListMav("Watchlist");
    }

    private ModelAndView getPrivateListMav(String name) {
        final List<MoovieList> moovieListList = moovieListService.getMoovieListDefaultPrivateFromCurrentUser();
        MoovieList toReturnList = null;
        for (int i = 0 ; i < moovieListList.size() ; i++){
            if(moovieListList.get(i).getName().equals(name)){
                toReturnList = moovieListList.get(i);
            }
        }
        return list(toReturnList.getMoovieListId());
    }

    @RequestMapping("/list/{id:\\d+}")
    public ModelAndView list(@PathVariable("id") final int moovieListId) {
        Optional<MoovieList> moovieListData = moovieListService.getMoovieListById(moovieListId);
        if (moovieListData.isPresent()) {
            final ModelAndView mav = new ModelAndView("helloworld/moovieList");

            boolean isLiked = moovieListService.likeMoovieListStatusForUser(moovieListId);
            mav.addObject("isLiked", isLiked);
            mav.addObject("moovieList", moovieListData.get());

            List<Media> mediaList = mediaService.getMediaByMoovieListId(moovieListId, mediaService.DEFAULT_PAGE_SIZE, 0);
            List<MoovieListContent> moovieListContent = moovieListService.getMoovieListContentById(moovieListId);
            String listOwner = userService.findUserById(moovieListData.get().getUserId()).get().getUsername();
            int likeCount = moovieListService.getLikesCount(moovieListId).get();
            int moviesCount = moovieListService.getMoovieListSize(moovieListId, false).get();
            int tvSeriesCount = moovieListService.getMoovieListSize(moovieListId, true).get();
            List<MoovieListContent> watchedMovies = mediaListService.getMediaWatchedInMoovieList(moovieListId);
            mav.addObject("mediaList", mediaList);
            mav.addObject("moovieListContent", moovieListContent);
            mav.addObject("listOwner", listOwner);
            mav.addObject("likeCount", likeCount);
            mav.addObject("moviesCount", moviesCount);
            mav.addObject("tvSeriesCount", tvSeriesCount);
            mav.addObject("watchedMovies", watchedMovies);
            mav.addObject("watchedMoviesSize", watchedMovies.size());
            return mav;
        } else {
            final ModelAndView mav = new ModelAndView("helloworld/404.jsp");
            mav.addObject("extraInfo", "The list with id: " + moovieListId + " doesn't exists");
            return mav;
        }
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public ModelAndView createReview(@RequestParam("listId") int listId) {
        moovieListService.likeMoovieList(listId);
        return new ModelAndView("redirect:/list/" + listId);
    }

    @ModelAttribute("user")
    public User getLoggedUser() {
        try {
            userService.getInfoOfMyUser();
        } catch (UnableToFindUserException exception) {
            return null;
        }
        return userService.getInfoOfMyUser();
    }

}
