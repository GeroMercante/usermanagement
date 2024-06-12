package login.usermanagementsystem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import login.usermanagementsystem.dto.ReqRes;
import login.usermanagementsystem.entity.OurUsers;
import login.usermanagementsystem.repository.UsersRepository;
import login.usermanagementsystem.service.impl.UserManagementServiceImpl;

@Service
public class UserManagementService implements UserManagementServiceImpl {
  
  @Autowired
  private UsersRepository usersRepository;
  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  private  AuthenticationManager authenticationManager;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public ReqRes register(ReqRes registrationRequest) {
    ReqRes resp = new ReqRes();
    try {
      OurUsers ourUser = new OurUsers();
      ourUser.setEmail(registrationRequest.getEmail());
      ourUser.setCity(registrationRequest.getCity());
      ourUser.setRole(registrationRequest.getRole());
      ourUser.setName(registrationRequest.getName());
      ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
      OurUsers ourUserResult = usersRepository.save(ourUser);
      if (ourUserResult.getId() > 0) {
        resp.setOurUsers((ourUserResult));
        resp.setMessage("Usuario guardado correctamente");
        resp.setStatusCode(200);
      }
      resp.setStatusCode(200);
    } catch (Exception e) {
      resp.setStatusCode(500);
      resp.setError(e.getMessage());
    }
    return resp;
  }

  @Override
  public ReqRes login(ReqRes loginRequest) {
    ReqRes res = new ReqRes();
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
      var user = usersRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
      var jwt = jwtUtils.generateToken(user);
      var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
      res.setStatusCode(200);
      res.setToken(jwt);
      res.setRefreshToken(refreshToken);
      res.setExpirationTime("24Hrs");
      res.setMessage("Login correcto");
    }catch (Exception e) {
      res.setStatusCode(500);
      res.setError(e.getMessage());
    }
    return res;
  }

  @Override
  public ReqRes refreshToken (ReqRes refreshTokenRequest) {
    ReqRes res = new ReqRes();
    try {
      String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
      OurUsers users = usersRepository.findByEmail(ourEmail).orElseThrow();
      if (jwtUtils.isTokeValid(refreshTokenRequest.getRefreshToken(), users)) {
        var jwt = jwtUtils.generateToken(users);
        res.setStatusCode(200);
        res.setToken(jwt);
        res.setRefreshToken(refreshTokenRequest.getToken());
        res.setExpirationTime("24Hrs");
        res.setMessage("Refresh token correcto");
      }
      res.setStatusCode(200);
      return res;
    } catch (Exception e) {
      res.setStatusCode(500);
      res.setError(e.getMessage());
      return res;
    }
  }

  @Override
  public ReqRes getAllUsers() {
    ReqRes res = new ReqRes();
    try {
      List<OurUsers> result = usersRepository.findAll();
      if (!result.isEmpty()) {
        res.setOurUsersList(result);
        res.setStatusCode(200);
        res.setMessage("Ok");
      } else {
        res.setStatusCode(404);
        res.setMessage("No hay usuarios registrados");
      }
      return res;
    } catch (Exception e) {
      res.setStatusCode(500);
      res.setError(e.getMessage());
      return res;
    }
  }

  @Override
  public ReqRes getUsersById(Long id) {
    ReqRes res = new ReqRes();
    try {
      OurUsers result = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro el usuario"));
      if (result != null) {
        res.setOurUsers(result);
        res.setStatusCode(200);
        res.setMessage("Ok");
      } else {
        res.setStatusCode(404);
        res.setMessage("No hay usuarios registrados");
      }
      return res;
    } catch (Exception e) {
      res.setStatusCode(500);
      res.setError(e.getMessage());
      return res;
    }
  }

  @Override
  public ReqRes deleteUsersById(Long id) {
    ReqRes res = new ReqRes();
    try {
      OurUsers result = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro el usuario"));
      if (result != null) {
        usersRepository.deleteById(id);
        res.setStatusCode(200);
        res.setMessage("Usuario eliminado correctamente");
      } else {
        res.setStatusCode(404);
        res.setMessage("No hay usuarios registrados");
      }
      return res;
    } catch (Exception e) {
      res.setStatusCode(500);
      res.setError(e.getMessage());
      return res;
    }
  }

  @Override
  public ReqRes updateUser(Long userId, OurUsers updateUser) {
    ReqRes res = new ReqRes();

    try {
      Optional<OurUsers> userOptional = usersRepository.findById(userId);
      if (userOptional.isPresent()) {
        OurUsers existingUsers = userOptional.get();
        existingUsers.setEmail(updateUser.getEmail());
        existingUsers.setName(updateUser.getName());
        existingUsers.setCity(updateUser.getCity());
        existingUsers.setRole(updateUser.getRole());

        // Check if password is present in the request
        if (updateUser.getPassword() != null  && !updateUser.getPassword().isEmpty()) {
          // Encode the password and update it
          existingUsers.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }

        OurUsers savedUser = usersRepository.save(existingUsers);
        res.setOurUsers(savedUser);
        res.setStatusCode(200);
        res.setMessage("Usuario actualizado correctamente");
      } else {
        res.setStatusCode(404);
        res.setMessage("No se encontro el usuario");
      }
      return res;
    } catch (Exception e) {
      res.setStatusCode(500);
      res.setError("Error al actualizar el usuario: " + e.getMessage());
      return res;
    }
  }

  @Override
  public ReqRes getMynfo (String email) {
    ReqRes res = new ReqRes();
    try {
      Optional<OurUsers> userOptional = usersRepository.findByEmail(email);
      if (userOptional.isPresent()) {
        OurUsers user = userOptional.get();
        res.setOurUsers(user);
        res.setStatusCode(200);
        res.setMessage("Ok");
      } else {
        res.setStatusCode(404);
        res.setMessage("No se encontro el usuario");
      }
      return res;
    } catch (Exception e) {
      res.setStatusCode(500);
      res.setError("Error: " + e.getMessage());
      return res;
    }
  }

}
