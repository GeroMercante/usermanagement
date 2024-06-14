package login.usermanagementsystem.integration.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Boolean active;
    private String email;
    private String gender;
    private String name;
    private String phone;
}
