package be.flo.roommateService.dto.post;

import be.flo.roommateService.dto.technical.DTO;
import be.flo.roommateService.dto.technical.verification.NotNull;
import be.flo.roommateService.dto.technical.verification.Pattern;
import be.flo.roommateService.util.ErrorMessage;

/**
 * Created by florian on 4/03/15.
 */
public class ForgotPasswordDTO extends DTO{

    @NotNull
    @Pattern(regexp = Pattern.EMAIL,message = ErrorMessage.VALIDATION_EMAIL)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ForgotPasswordDTO{" +
                "email='" + email + '\'' +
                '}';
    }
}
