package dto;

import dto.technical.DTO;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;

/**
 * Created by florian on 11/11/14.
 */
public class RoommateDTO extends DTO {

    private Long id;

    @Pattern(regexp = ".{2,50}", message = "firstName must respect this pattern : .{2,50}")
    private String name;

    @Email
    private String email;

    private String password;

    private Float iconColor;

    public RoommateDTO() {
    }


    public Float getIconColor() {
        return iconColor;
    }

    public void setIconColor(Float iconColor) {
        this.iconColor = iconColor;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RoommateDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", iconColor=" + iconColor +
                '}';
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RoommateDTO && ((RoommateDTO) obj).getId().equals(this.id)) {
            return true;
        }
        return false;
    }
}
