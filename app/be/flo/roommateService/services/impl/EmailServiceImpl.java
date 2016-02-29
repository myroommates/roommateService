package be.flo.roommateService.services.impl;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.SmallestMailboxRouter;
import be.flo.roommateService.models.entities.Roommate;
import play.Configuration;
import be.flo.roommateService.services.EmailService;
import be.flo.roommateService.services.VelocityGeneratorService;
import be.flo.roommateService.util.email.ProjectData;
import be.flo.roommateService.util.email.actors.EmailServiceActor;
import be.flo.roommateService.util.email.messages.EmailMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by florian on 6/12/14.
 */
public class EmailServiceImpl implements EmailService {

    //be.flo.roommateService.services
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

        ProjectData projectData = getProjectData();

        body = body.replace("$project.url",projectData.getUrl());
        body = body.replace("$project.name",projectData.getName());

        title = title.replace("$project.url",projectData.getUrl());
        title = title.replace("$project.name",projectData.getName());

        values.put("content", body);
        values.put("project", projectData);

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
