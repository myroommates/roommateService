package dto.post;

import dto.technical.PostedDTO;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;

/**
 * Created by florian on 11/11/14.
 */
public class RegistrationCDTO extends PostedDTO {

    @Pattern(regexp = ".{6,50}", message = "homeName must respect this pattern : .{6,50}")
    private String homeName;

    @Pattern(regexp = ".{2,50}", message = "firstName must respect this pattern : .{2,50}")
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "[a-zA-Z0-9-_%]{6,18}", message = "homeName must respect this pattern : [a-zA-Z0-9-_%]{6,18}")
    private String password;

    public RegistrationCDTO() {
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
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
        return "RegistrationDTO{" +
                "homeName='" + homeName + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
