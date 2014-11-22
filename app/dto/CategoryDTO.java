package dto;

import dto.technical.DTO;

import javax.validation.constraints.Pattern;

/**
 * Created by florian on 10/11/14.
 */
public class CategoryDTO extends DTO {

    private Long id;

    @Pattern(regexp = ".{2,50}", message = "name must respect this pattern : .{2,50}")
    private String name;

    public CategoryDTO() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
