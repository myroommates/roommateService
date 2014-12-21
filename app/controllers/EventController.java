package controllers;

import controllers.technical.SecurityController;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by florian on 15/12/14.
 */
public interface EventController {

    public Result getAll();

    @Security.Authenticated(SecurityController.class)
    Result getById(Long id);

    @Security.Authenticated(SecurityController.class)
    Result update(Long id);

    @Security.Authenticated(SecurityController.class)
    Result create();

    @Security.Authenticated(SecurityController.class)
    Result remove(Long id);
}
