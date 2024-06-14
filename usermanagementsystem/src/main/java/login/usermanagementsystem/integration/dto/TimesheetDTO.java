package login.usermanagementsystem.integration.dto;

import lombok.Data;

@Data
public class TimesheetDTO {
    private String employeeName;
    private String projectName;
    private String taskName;
    private String date;
    private String description;
    private double hours;
}
