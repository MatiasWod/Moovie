package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.services.VerificationTokenService;
import ar.edu.itba.paw.webapp.exceptions.VerificationTokenNotFoundException;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

;

@Controller
public class HelloWorldController {

    @Autowired
    UserService userService;

    @Autowired
    VerificationTokenService verificationTokenService;

    @RequestMapping("/{id:\\d+}")
    public ModelAndView profile(@PathVariable("id") final long userId) {
        final ModelAndView mav = new ModelAndView("helloworld/profile");
        try{
            mav.addObject("user",userService.getInfoOfMyUser());
        }catch(UnableToFindUserException exception){
            mav.addObject("user",null);
        }
        mav.addObject("userid", userId);
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(@ModelAttribute("registerForm") final RegisterForm form) {
        return new ModelAndView("helloworld/register");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerForm(@Valid @ModelAttribute("registerForm") final RegisterForm form,final BindingResult errors) {
        if (errors.hasErrors()) {
            return register(form);
        }
        try{
            User user = userService.createUser(form.getUsername(), form.getEmail(), form.getPassword());
        } catch (UnableToCreateUserException e){
            return new ModelAndView("redirect:/register?error:" + e.getMessage());
        }

        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/register/confirm")
    public ModelAndView confirmRegistration(@RequestParam("token") final String token) {
        Token verificationToken = verificationTokenService.getToken(token).orElseThrow(VerificationTokenNotFoundException::new);
        if(userService.confirmRegister(verificationToken)) {
            return new ModelAndView("redirect:/login");

        }
       //TODO return new ModelAndView("redirect:/register/tokentimedout?token=" + token);
        return new ModelAndView("redirect:/discover");
    }

    //TODO
    /*@RequestMapping(value = "/register/tokentimedout")
    public ModelAndView tokenTimedOut(@RequestParam("token") final String token) {
        ModelAndView mav = new ModelAndView("tokenTimedOut");
        mav.addObject("token", token);
        return mav;
    }*/

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("helloworld/login");
    }


    @RequestMapping("/profile/{username}")
    public ModelAndView profilePage(@PathVariable String username){
        Optional<User> aux = userService.findUserByUsername(username);
        if(aux.isPresent()){
            ModelAndView mav = new ModelAndView("helloworld/profile");
            mav.addObject("user", aux.get() );
            mav.addObject("isMe", userService.isUsernameMe(username));

            return mav;
        }
        ModelAndView mav = new ModelAndView("helloworld/404");
        mav.addObject("extraInfo", "User " + username + " not found");
        return mav;
    }

    @RequestMapping(value = "/uploadProfilePicture", method = {RequestMethod.POST})
    public String uploadProfilePicture(@RequestParam("file") MultipartFile picture, HttpServletRequest request) {
        String referer = request.getHeader("referer");
        try {
            userService.setProfilePicture(picture);
        }catch (InvalidTypeException e) {
            return "redirect:" + referer + "?error:invalidType";
        } catch (NoFileException e) {
            return "redirect:" + referer + "?error:noFile";
        } catch (FailedToSetProfilePictureException e) {
            return "redirect:" + referer + "?error:failedSetProfilePicture";
        }
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/profile/image/{username}", produces = "image/**")
    public @ResponseBody
    byte[] getProfilePicture(@PathVariable("username") final String username){
        return userService.getProfilePicture(username);
    }

}
