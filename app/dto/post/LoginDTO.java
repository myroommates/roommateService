package dto.post;

import dto.technical.DTO;
import play.data.validation.Constraints;

import javax.validation.constraints.Pattern;

/**
 * Created by florian on 10/11/14.
 */
public class LoginDTO extends DTO {

    @Constraints.Required
    @Constraints.Email
    private String email;

    @Constraints.Required
    @Constraints.MinLength(value = 6)
    @Constraints.MaxLength(value = 18)
    private String password;

    private boolean keepSessionOpen;

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

    public boolean isKeepSessionOpen() {
        return keepSessionOpen;
    }

    public void setKeepSessionOpen(boolean keepSessionOpen) {
        this.keepSessionOpen = keepSessionOpen;
    }


    @Override
    public String toString() {
        return "LoginDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", keepSessionOpen=" + keepSessionOpen +
                '}';
    }
}
