package pfe.backend.DAL.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pfe.backend.DAL.Models.User;
import pfe.backend.DAL.Models.UserDecorator;
import pfe.backend.DAL.Repositories.UserRepository;

/**
 * Details about the user.
 */
@Component
public class CustomerUserDetailsService implements UserDetailsService {

    private UserRepository users;

    public CustomerUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = this.users.findByLogin(username);
        UserDecorator userDecorator = new UserDecorator(user);
        return userDecorator;
    }
}
