package hu.cubix.logistics.patrik.security;

import hu.cubix.logistics.patrik.model.UserModel;
import hu.cubix.logistics.patrik.repository.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LogisticsUserDetailsService implements UserDetailsService {

    @Autowired
    UserModelRepository userModelRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userModelRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(username, user.getPassword(), user.getRoles().stream().map(SimpleGrantedAuthority::new).toList());
    }
}
