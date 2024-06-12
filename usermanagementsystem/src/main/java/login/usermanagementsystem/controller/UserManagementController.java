package login.usermanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import login.usermanagementsystem.dto.ReqRes;
import login.usermanagementsystem.entity.OurUsers;
import login.usermanagementsystem.service.UserManagementService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class UserManagementController {
  @Autowired
  private UserManagementService userManagementService;

  @PostMapping("/auth/register")
  public ResponseEntity<ReqRes> register(@RequestBody ReqRes regRequest) {
    return ResponseEntity.ok(userManagementService.register(regRequest));
  }

  @PostMapping("/auth/login")
  public ResponseEntity<ReqRes> login(@RequestBody ReqRes logRequest) {
    return ResponseEntity.ok(userManagementService.login(logRequest));
  }

  @PostMapping("/auth/refresh")
  public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes tokRequest) {
    return ResponseEntity.ok(userManagementService.refreshToken(tokRequest));
  }

  @GetMapping("/admin/get-all-users")
  public ResponseEntity<ReqRes> getAllUsers() {
    return ResponseEntity.ok(userManagementService.getAllUsers());
  }

  @GetMapping("/admin/get-user/{userId}")
  public ResponseEntity<ReqRes> getUserById(@PathVariable Long userId) {
    return ResponseEntity.ok(userManagementService.getUsersById(userId));
  }

  @PutMapping("/admin/update/{userId}")
  public ResponseEntity<ReqRes> updateUser(@PathVariable Long userId, @RequestBody OurUsers reqres) {
    return ResponseEntity.ok(userManagementService.updateUser(userId, reqres));
  }

  @GetMapping("/adminuser/get-profile")
  public ResponseEntity<ReqRes> getProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    ReqRes response = userManagementService.getMynfo(email);
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @DeleteMapping("/admin/delete/{userId}")
  public ResponseEntity<ReqRes> deleteUser(@PathVariable Long userId) {
    return ResponseEntity.ok(userManagementService.deleteUsersById(userId));
  }
}
