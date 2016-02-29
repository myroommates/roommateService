package be.flo.roommateService.models;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.i18n.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 23/12/14.
 */
public class RoommateForm{


    @Constraints.Required
    @Constraints.MinLength(2)
    @Constraints.MaxLength(50)
    private String name;

    @Constraints.Required
    @Constraints.MaxLength(3)
    private String nameAbrv;

    @Constraints.Required
    @Constraints.Email
    private String email;

    @Constraints.Required
    @Constraints.MinLength(6)
    @Constraints.MaxLength(18)
    private String password;

    @Constraints.Required
    private String repeatPassword;


    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if (!password.equals(repeatPassword)) {
            errors.add(new ValidationError("repeatPassword", Messages.get("generic.validation.repeatPassword")));
        }
        return errors.isEmpty() ? null : errors;
    }

    public String getNameAbrv() {
        return nameAbrv;
    }

    public void setNameAbrv(String nameAbrv) {
        this.nameAbrv = nameAbrv;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RoommateForm{" +
                "name='" + name + '\'' +
                ", nameAbrv='" + nameAbrv + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", repeatPassword='" + repeatPassword + '\'' +
                '}';
    }
}
