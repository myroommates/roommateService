package dto.post;

import dto.technical.DTO;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;

/**
 * Created by florian on 11/11/14.
 */
public class RegistrationDTO extends DTO {

    @Pattern(regexp = ".{2,50}", message = "firstName must respect this pattern : .{2,50}")
    private String name;

    @Email
    private String email;

    public RegistrationDTO() {}

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

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}