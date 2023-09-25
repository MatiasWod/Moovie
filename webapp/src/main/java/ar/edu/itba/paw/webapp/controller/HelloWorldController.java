package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.CreateReviewForm;
import ar.edu.itba.paw.webapp.form.LoginForm;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class HelloWorldController {

    @Autowired
    private UserService userService;
    @Autowired
    public HelloWorldController(final UserService us) {
        this.userService = us;
    }


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

    @RequestMapping(value = "/registerpost", method = RequestMethod.POST)
    public ModelAndView registerForm(@Valid @ModelAttribute("registerForm") final RegisterForm form,final BindingResult errors) {
        if (errors.hasErrors()) {
            return register(form);
        }
        User user = userService.createUser(form.getUsername(), form.getEmail(), form.getPassword());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/login")
    public ModelAndView login(@ModelAttribute("loginForm") final LoginForm form) {
        return new ModelAndView("helloworld/login");
    }

  /*  @RequestMapping(value = "/loginpost", method = RequestMethod.POST)
    public ModelAndView loginForm(@Valid @ModelAttribute("loginForm") final LoginForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return login(form);
        }
        Optional<User> user = userService.findUserByUsername(form.getUsername());
        if(user.isPresent()){
            if(user.get().getPassword().equals(form.getPassword())) {
                return new ModelAndView("redirect:/");
            }
        }
        return new ModelAndView("helloworld/login");
    }*/

}
