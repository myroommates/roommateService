package services;

import model.entities.Language;
import model.entities.Roommate;
import util.EmailMessage;

/**
 * Created by florian on 6/12/14.
 */
public interface EmailService {

    void sendEmail(Roommate roommate, String title, String body);
}
