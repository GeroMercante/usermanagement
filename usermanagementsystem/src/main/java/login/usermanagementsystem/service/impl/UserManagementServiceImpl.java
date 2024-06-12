package login.usermanagementsystem.service.impl;

import login.usermanagementsystem.dto.ReqRes;
import login.usermanagementsystem.entity.OurUsers;

public interface UserManagementServiceImpl {

  ReqRes register(ReqRes registrationRequest);

  ReqRes login(ReqRes loginRequest);

  ReqRes refreshToken (ReqRes refreshTokenRequest);

  ReqRes getAllUsers();

  ReqRes getUsersById(Long id);

  ReqRes deleteUsersById(Long id);

  ReqRes updateUser(Long userId, OurUsers updateUser);

  ReqRes getMynfo (String email);

}
