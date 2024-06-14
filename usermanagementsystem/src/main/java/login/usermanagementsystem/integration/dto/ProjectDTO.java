package login.usermanagementsystem.integration.dto;

import lombok.Data;

@Data
public class ProjectDTO {
    private Integer id;
    private String name;

    public ProjectDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
