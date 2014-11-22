package dto.post;

import dto.technical.PostedDTO;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;

/**
 * Created by florian on 10/11/14.
 */
public class LoginDTO extends PostedDTO {

    @Email
    protected String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{6,18}$", message = "homeName must respect this pattern : [a-zA-Z0-9-_%]{6,18}")
    protected String password;

    public LoginDTO() {
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
        return "LoginDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
