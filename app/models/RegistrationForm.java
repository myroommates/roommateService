package models;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.i18n.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 18/12/14.
 */
public class RegistrationForm extends RoommateForm{

    private boolean keepSessionOpen;

    public boolean isKeepSessionOpen() {
        return keepSessionOpen;
    }

    public void setKeepSessionOpen(boolean keepSessionOpen) {
        this.keepSessionOpen = keepSessionOpen;
    }
}
