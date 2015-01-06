package controllers.rest;

import controllers.rest.technical.AbstractRestController;
import controllers.rest.technical.SecurityRestController;
import converter.HomeToHomeConverter;
import dto.HomeDTO;
import dto.ListDTO;
import models.entities.Home;
import play.db.ebean.Transactional;
import play.mvc.Result;
import play.mvc.Security;
import services.HomeService;
import services.impl.HomeServiceImpl;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

import java.util.List;

/**
 * Created by florian on 8/12/14.
 */
public class HomeRestController extends AbstractRestController {

    //service
    private HomeService homeService = new HomeServiceImpl();

    //convert
    private HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter();

    @Security.Authenticated(SecurityRestController.class)
    @com.avaje.ebean.annotation.Transactional
    public Result update(Long id){

        HomeDTO dto = extractDTOFromRequest(HomeDTO.class);

        if(!securityRestController.getCurrentUser().getHome().getId().equals(id)){
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_HOME,id);
        }

        Home home = securityRestController.getCurrentUser().getHome();

        home.setMoneySymbol(dto.getMoneySymbol());

        homeService.saveOrUpdate(home);

        return ok(homeToHomeConverter.convert(home));
    }
}
