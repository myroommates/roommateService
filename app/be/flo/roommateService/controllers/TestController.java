package be.flo.roommateService.controllers;

import be.flo.roommateService.controllers.technical.SecurityController;
import play.db.jpa.Transactional;
import be.flo.roommateService.controllers.technical.AbstractController;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by florian on 2/05/15.
 */
public class TestController  extends AbstractController {

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result index2() {
        return ok(views.html.test2.render());
    }

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result index() {
        return ok(views.html.test.render());
    }
}
