package dto;

import dto.technical.DTO;
import dto.technical.verification.Pattern;

/**
 * Created by florian on 27/12/14.
 */
public class ChangeEmailDTO extends DTO {

    @Pattern(regexp = Pattern.PASSWORD)
    private String oldPassword;

    @Pattern(regexp = Pattern.EMAIL)
    private String newEmail;

    public ChangeEmailDTO() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    @Override
    public String toString() {
        return "ChangeEmailDTO{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newEmail='" + newEmail + '\'' +
                '}';
    }
}
