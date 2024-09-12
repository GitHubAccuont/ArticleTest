package org.justme.articlestest.security.services;

import org.justme.articlestest.data.entity.Role;
import org.justme.articlestest.data.entity.UserEntity;
import org.justme.articlestest.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("No such user"));
        return new User(user.getName(), user.getPassword(), getAuthorities(user.getRoleSet()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorities.addAll(role.getPrivilegeSet()
                    .stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .toList());
        }

        return authorities;
    }
}
