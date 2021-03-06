package be.flo.roommateService.dto.post;

import be.flo.roommateService.dto.technical.DTO;
import be.flo.roommateService.dto.technical.verification.NotNull;
import be.flo.roommateService.dto.technical.verification.Pattern;
import be.flo.roommateService.util.ErrorMessage;


/**
 * Created by florian on 10/11/14.
 */
public class LoginDTO extends DTO {

    @NotNull
    @Pattern(regexp = Pattern.EMAIL,message = ErrorMessage.VALIDATION_EMAIL)
    private String email;

    @NotNull
    @Pattern(regexp = Pattern.PASSWORD,message = ErrorMessage.VALIDATION_PASSWORD)
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

    public boolean getKeepSessionOpen() {
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
