package dto;

import dto.technical.DTO;
import dto.technical.verification.Pattern;
import util.ErrorMessage;

/**
 * Created by florian on 27/12/14.
 */
public class ChangePasswordDTO extends DTO {

    @Pattern(regexp = Pattern.PASSWORD,message = ErrorMessage.VALIDATION_PASSWORD)
    private String oldPassword;

    @Pattern(regexp = Pattern.PASSWORD,message = ErrorMessage.VALIDATION_PASSWORD)
    private String newPassword;

    public ChangePasswordDTO() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordDTO{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
