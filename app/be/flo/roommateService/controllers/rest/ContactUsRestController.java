package be.flo.roommateService.controllers.rest;

import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.controllers.technical.SecurityRestController;
import be.flo.roommateService.dto.post.ContactUsDTO;
import be.flo.roommateService.dto.technical.ResultDTO;
import play.Configuration;
import play.db.jpa.Transactional;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.EmailService;
import be.flo.roommateService.services.impl.EmailServiceImpl;

/**
 * Created by florian on 6/12/14.
 */
public class ContactUsRestController extends AbstractController {

    //service
    private EmailService emailService = new EmailServiceImpl();

    final String contactEmail = Configuration.root().getString("mail.default.contact");

    @Security.Authenticated(SecurityRestController.class)
    @Transactional
    public Result contactUs() {

        ContactUsDTO dto = extractDTOFromRequest(ContactUsDTO.class);

        String content = "";

        content += "Subject:" + dto.getSubject() + "\n\ncontent:" + dto.getContent();

        emailService.sendEmail(contactEmail, "Roommate:contact", content);

        return ok(new ResultDTO());
    }
}
