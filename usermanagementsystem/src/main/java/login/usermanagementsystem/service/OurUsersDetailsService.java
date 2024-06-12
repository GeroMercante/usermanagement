package login.usermanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import login.usermanagementsystem.repository.UsersRepository;

@Service
public class OurUsersDetailsService implements UserDetailsService {

  @Autowired
  private UsersRepository usersRepository;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return usersRepository.findByEmail(username).orElseThrow();
  }
}
