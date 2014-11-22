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
    private String firstName;

    @Pattern(regexp = ".{2,50}", message = "lastName must respect this pattern : .{2,50}")
    private String lastName;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RoommateDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
