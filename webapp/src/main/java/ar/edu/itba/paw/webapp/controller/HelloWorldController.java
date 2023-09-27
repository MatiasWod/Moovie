package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.UserService;

import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;;
import org.springframework.web.servlet.ModelAndView;


import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
public class HelloWorldController {

    @Autowired
    UserService userService;

    @RequestMapping("/{id:\\d+}")
    public ModelAndView profile(@PathVariable("id") final long userId) {
        final ModelAndView mav = new ModelAndView("helloworld/profile");
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
        User user = userService.createUser(form.getUsername(), form.getEmail(), form.getPassword());
        return new ModelAndView("redirect:/login");
    }

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
    public ModelAndView uploadProfilePicture(@RequestParam("file") MultipartFile picture) throws IOException {
        User user = userService.getInfoOfMyUser();
        try {
            if (!picture.isEmpty()) {
                if (!isImage(picture.getContentType())) {
                    return new ModelAndView("redirect:/user/" + user.getUsername() + "?error=invalidFileType");
                }
                byte[] image = IOUtils.toByteArray(picture.getInputStream());
                userService.setProfilePicture(image, user);

                return new ModelAndView("redirect:/profile/" + user.getUsername());
            }
        } catch (Exception e) {
            return new ModelAndView("redirect:/profile/" + user.getUsername() + "?error=uploadFailed");
        }
        return new ModelAndView("redirect:/profile/" + user.getUsername() + "?error=noFileSelected");
    }
    private boolean isImage(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }


    @RequestMapping(value = "/profile/image/{username}", produces = "image/**")
    public @ResponseBody
    byte[] getProfilePicture(@PathVariable("username") final String username){
        return userService.getProfilePicture(username);
    }

}
