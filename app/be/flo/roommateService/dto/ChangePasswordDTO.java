package be.flo.roommateService.dto;

import be.flo.roommateService.dto.technical.DTO;
import be.flo.roommateService.dto.technical.verification.Pattern;
import be.flo.roommateService.util.ErrorMessage;

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
