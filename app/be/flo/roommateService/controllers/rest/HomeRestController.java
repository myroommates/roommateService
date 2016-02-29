package be.flo.roommateService.controllers.rest;

import be.flo.roommateService.controllers.technical.AbstractController;
import be.flo.roommateService.controllers.technical.AdminSecurityRestController;
import be.flo.roommateService.converter.HomeToHomeConverter;
import be.flo.roommateService.dto.HomeDTO;
import be.flo.roommateService.models.entities.Home;
import play.db.jpa.Transactional;
import play.mvc.Result;
import play.mvc.Security;
import be.flo.roommateService.services.HomeService;
import be.flo.roommateService.services.impl.HomeServiceImpl;
import be.flo.roommateService.util.ErrorMessage;
import be.flo.roommateService.util.exception.MyRuntimeException;

/**
 * Created by florian on 8/12/14.
 */
public class HomeRestController extends AbstractController {

    //service
    private HomeService homeService = new HomeServiceImpl();



    @Security.Authenticated(AdminSecurityRestController.class)
    @Transactional
    public Result update(Long id){

        HomeDTO dto = extractDTOFromRequest(HomeDTO.class);

        if(!securityController.getCurrentUser().getHome().getId().equals(id)){
            throw new MyRuntimeException(ErrorMessage.NOT_YOU_HOME,id);
        }

        Home home = securityController.getCurrentUser().getHome();

        home.setMoneySymbol(dto.getMoneySymbol());

        homeService.saveOrUpdate(home);

        //convert
        HomeToHomeConverter homeToHomeConverter = new HomeToHomeConverter(securityController.getCurrentUser());

        return ok(homeToHomeConverter.convert(home));
    }
}
