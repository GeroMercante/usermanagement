package login.usermanagementsystem.integration.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private Integer id;
    private String name;

    public TaskDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
