package dto.post;

import dto.technical.DTO;

import javax.validation.constraints.Pattern;

/**
 * Created by florian on 10/11/14.
 */
public class ReactivationDTO extends DTO {

    @Pattern(regexp = "^[a-zA-Z0-9]{12}$", message = "exactly 12 caracters")
    protected String reactivationKey;

    public ReactivationDTO() {
    }

    public String getReactivationKey() {
        return reactivationKey;
    }

    public void setReactivationKey(String reactivationKey) {
        this.reactivationKey = reactivationKey;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "reactivationKey='" + reactivationKey + '\'' +
                '}';
    }
}
