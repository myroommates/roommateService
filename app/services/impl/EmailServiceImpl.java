package services.impl;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.SmallestMailboxRouter;
import models.entities.Roommate;
import play.Configuration;
import services.EmailService;
import services.VelocityGeneratorService;
import util.email.ProjectData;
import util.email.actors.EmailServiceActor;
import util.email.messages.EmailMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by florian on 6/12/14.
 */
public class EmailServiceImpl implements EmailService {

    //services
    private final VelocityGeneratorService velocityGeneratorService = new VelocityGeneratorServiceImpl();

    private String projectUrl = Configuration.root().getString("project.url");
    private String projectName = Configuration.root().getString("project.name");

    private final static String VELOCITY_BASIC_EMAIL = "basicEmailStructure.vm";
    public ActorSystem system;
    public ActorRef emailActorRef;

    public EmailServiceImpl() {

        system = ActorSystem.create("awacsystem");
        emailActorRef = system.actorOf(new Props(EmailServiceActor.class).withRouter
                (new SmallestMailboxRouter(1)), "emailService");

    }

    @Override
    public void sendEmail(Roommate roommate, String title, String body) {
        sendEmail(roommate.getEmail(), title, body);
    }

    @Override
    public void sendEmail(String email, String title, String body) {

        //load velocity content
        Map<String, Object> values = new HashMap<>();

        values.put("content", body);
        values.put("project", getProjectData());

        String velocityContent = velocityGeneratorService.generate(VELOCITY_BASIC_EMAIL, values);

        EmailMessage emailMessage = new EmailMessage(email, title, velocityContent);

        emailActorRef.tell(emailMessage, emailActorRef);
    }


    private ProjectData getProjectData() {

        ProjectData projectData = new ProjectData();

        projectData.setName(projectName);
        projectData.setUrl(projectUrl);

        return projectData;
    }
}
