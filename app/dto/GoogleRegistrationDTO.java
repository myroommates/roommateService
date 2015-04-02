package dto;

import dto.technical.DTO;

/**
 * Created by florian on 2/04/15.
 */
public class GoogleRegistrationDTO extends DTO{


    private String email;

    private String password;

    private String googleKey;

    public GoogleRegistrationDTO() {
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

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    @Override
    public String toString() {
        return "GoogleRegistrationDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", googleKey='" + googleKey + '\'' +
                '}';
    }
}
