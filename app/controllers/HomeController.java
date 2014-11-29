package controllers;

import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.HomeToHomeConverter;
import dto.HomeDTO;
import entities.Home;
import entities.Roommate;
import play.mvc.Result;
import play.mvc.Security;
import services.HomeService;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

/**
 * Created by florian on 11/11/14.
 */
public class HomeController extends AbstractController {

    private HomeService homeService = new HomeService();
}
