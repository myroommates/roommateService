package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by florian on 20/12/14.
 */
public class HomeController extends AbstractController{

    @Security.Authenticated(SecurityController.class)
    @Transactional
    public Result index(){
        return redirect("/count/resume");
    }
}
