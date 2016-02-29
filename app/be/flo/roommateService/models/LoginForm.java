package be.flo.roommateService.models;

import play.data.validation.Constraints;

/**
 * Created by florian on 18/12/14.
 */
public class LoginForm {

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
}
