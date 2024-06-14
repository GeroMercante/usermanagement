package login.usermanagementsystem.integration.dto;

import lombok.Data;

@Data
public class PeopleDTO {
    private Integer id;
    private String name;

    public PeopleDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
