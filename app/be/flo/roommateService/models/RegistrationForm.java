package be.flo.roommateService.models;

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
