package util.email.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.SmallestMailboxRouter;
import util.email.actors.EmailServiceActor;
import util.email.messages.EmailMessage;

import java.io.IOException;

// annotate as Spring Service for IOC
//@Service
public class EmailService {


    public ActorSystem system;
    public ActorRef emailActorRef;

    /**
     * Uses the smallest inbox strategy to keep 20 instances alive ready to send out email
     * @see akka.routing.SmallestMailboxRouter
     */
    
    public EmailService() throws IOException {
        system = ActorSystem.create("awacsystem");
        emailActorRef = system.actorOf(new Props(EmailServiceActor.class).withRouter
                (new SmallestMailboxRouter(1)), "emailService");
    }

    /**
     * public interface to send out emails that dispatch the message to the listening actors
     * @param email the email message
     */
    
    public void send(EmailMessage email) {
        emailActorRef.tell(email, emailActorRef);
    }
}
