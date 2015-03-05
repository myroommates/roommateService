package dto;

import dto.technical.DTO;
import dto.technical.verification.Pattern;
import util.ErrorMessage;

/**
 * Created by florian on 27/12/14.
 */
public class ChangeEmailDTO extends DTO {

    @Pattern(regexp = Pattern.PASSWORD,message = ErrorMessage.VALIDATION_PASSWORD)
    private String oldPassword;

    @Pattern(regexp = Pattern.EMAIL,message = ErrorMessage.VALIDATION_EMAIL)
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
