package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MoovieUserDetailsService implements UserDetailsService {

    private UserService us;

    @Autowired
    public MoovieUserDetailsService(final UserService us) {
        this.us = us;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = us.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user" + username));
        final Set<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new MoovieAuthUser(user.getUsername(),user.getPassword(),authorities);
    }
}
