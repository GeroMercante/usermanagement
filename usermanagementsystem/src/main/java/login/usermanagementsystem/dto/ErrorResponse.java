package login.usermanagementsystem.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ErrorResponse {
  private int status;
  private String message;
}
