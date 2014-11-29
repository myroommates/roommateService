package dto;

import dto.technical.DTO;

import javax.validation.constraints.Pattern;

/**
 * Created by florian on 11/11/14.
 */
public class HomeDTO extends DTO {

    private Long id;

    public HomeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
