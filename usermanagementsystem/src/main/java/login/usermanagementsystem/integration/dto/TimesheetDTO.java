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
    // For Version 2
    private int idEmployee;
    private int idProject;
    private int idTask;
}
