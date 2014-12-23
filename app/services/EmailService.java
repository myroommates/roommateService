package services;

import models.entities.Roommate;

/**
 * Created by florian on 6/12/14.
 */
public interface EmailService {

    void sendEmail(Roommate roommate, String title, String body);
}
