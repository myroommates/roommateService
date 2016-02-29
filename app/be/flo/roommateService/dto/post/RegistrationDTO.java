package be.flo.roommateService.dto.post;

import be.flo.roommateService.dto.technical.DTO;
import be.flo.roommateService.dto.technical.verification.Pattern;
import be.flo.roommateService.util.ErrorMessage;


/**
 * Created by florian on 11/11/14.
 */
public class RegistrationDTO extends DTO {

    @Pattern(regexp = ".{2,50}")
    private String name;

    @Pattern(regexp = Pattern.EMAIL,message = ErrorMessage.VALIDATION_EMAIL)
    private String email;

    private String lang;

    public RegistrationDTO() {}

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
