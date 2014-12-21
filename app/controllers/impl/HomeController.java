package controllers.impl;

import controllers.technical.AbstractController;
import dto.HomeDTO;
import dto.ListDTO;
import model.entities.Home;
import play.db.ebean.Transactional;
import play.mvc.Result;
import services.HomeService;
import services.impl.HomeServiceImpl;

import java.util.List;

/**
 * Created by florian on 8/12/14.
 */
public class HomeController extends AbstractController {

    //service
    private HomeService homeService = new HomeServiceImpl();

    @Transactional
    public Result getAll() {

        List<Home> homes = homeService.findAll();

        ListDTO<HomeDTO> result = new ListDTO<>();

        for (Home home : homes) {

            HomeDTO homeDTO = new HomeDTO();
            homeDTO.setId(home.getId());
            result.addElement(homeDTO);
        }

        return ok(result);
    }
}
