package be.flo.roommateService.services;

import be.flo.roommateService.models.entities.Roommate;

/**
 * Created by florian on 6/12/14.
 */
public interface EmailService {

    void sendEmail(Roommate roommate, String title, String body);

    void sendEmail(String email, String title, String body);
}
