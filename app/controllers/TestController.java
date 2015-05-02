package controllers;

import com.avaje.ebean.annotation.Transactional;
import controllers.technical.AbstractController;
import controllers.technical.SecurityController;
import converter.RoommateToInterfaceDataDTOConverter;
import converter.RoommateToRoommateDTOConverter;
import play.mvc.Result;
import play.mvc.Security;
import services.RoommateService;
import services.ShoppingItemService;
import services.TicketDebtorService;
import services.TicketService;
import services.impl.RoommateServiceImpl;
import services.impl.ShoppingItemServiceImpl;
import services.impl.TicketDebtorServiceImpl;
import services.impl.TicketServiceImpl;

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
