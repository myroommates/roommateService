package services.impl;

import models.entities.Roommate;
import play.Logger;
import services.EmailService;

/**
 * Created by florian on 6/12/14.
 */
public class EmailServiceImpl implements EmailService{

    @Override
    public void sendEmail(Roommate roommate, String title, String body) {
        Logger.info("*************** EMAIL");
        Logger.info("TO:"+roommate.getEmail());
        Logger.info("TITLE:"+title);
        Logger.info("BODY:"+body);
        Logger.info("*************** END EMAIL");
    }
}
