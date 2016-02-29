package be.flo.roommateService.services.impl;

import be.flo.roommateService.models.entities.Home;
import play.db.jpa.JPA;
import be.flo.roommateService.services.HomeService;

/**
 * Created by florian on 11/11/14.
 */
public class HomeServiceImpl extends CrudServiceImpl<Home>  implements HomeService {


    @Override
    public Integer getCount() {

        String r = "SELECT h FROM Home h";

        return JPA.em().createQuery(r,Home.class).getResultList().size();
    }

}
