package controllers.rest;

import controllers.technical.AbstractController;
import controllers.technical.AdminSecurityRestController;
import controllers.technical.SecurityRestController;
import converter.HomeToHomeConverter;
import dto.HomeDTO;
import models.entities.Home;
import play.mvc.Result;
import play.mvc.Security;
import services.HomeService;
import services.impl.HomeServiceImpl;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

/**
 * Created by florian on 8/12/14.
 */
public class HomeRestController extends AbstractController {

    //service
    private HomeService homeService = new HomeServiceImpl();

    //convert
    private HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter();

    @Security.Authenticated(AdminSecurityRestController.class)
    @com.avaje.ebean.annotation.Transactional
    public Result update(Long id){

        HomeDTO dto = extractDTOFromRequest(HomeDTO.class);

        if(!securityController.getCurrentUser().getHome().getId().equals(id)){
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_HOME,id);
        }

        Home home = securityController.getCurrentUser().getHome();

        home.setMoneySymbol(dto.getMoneySymbol());

        homeService.saveOrUpdate(home);

        return ok(homeToHomeConverter.convert(home));
    }
}
