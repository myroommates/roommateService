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

    @Security.Authenticated(SecurityController.class)
    public Result update(Long id) {

        HomeDTO dto = extractDTOFromRequest(HomeDTO.class);

        Roommate currentUser = securityController.getCurrentUser();

        //load home
        Home home = currentUser.getHome();

        //control id
        if(!home.getId().equals(id)){
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.NOT_YOU_HOME, id));
        }

        Home homeWithSameName = homeService.findByName(dto.getName());

        //control roommate name
        if (homeWithSameName != null && !homeWithSameName.equals(home)) {
            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.HOME_NAME_ALREADY_USED));
        }

        //edit
        home.setName(dto.getName());

        //operation
        homeService.saveOrUpdate(home);

        //return
        HomeToHomeConverter converter = new HomeToHomeConverter();
        return ok(converter.converter(home));
    }
}
