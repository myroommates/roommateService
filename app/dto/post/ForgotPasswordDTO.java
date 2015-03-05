package dto.post;

import dto.technical.DTO;
import dto.technical.verification.NotNull;
import dto.technical.verification.Pattern;

/**
 * Created by florian on 4/03/15.
 */
public class ForgotPasswordDTO extends DTO{

    @NotNull
    @Pattern(regexp = Pattern.EMAIL)
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
