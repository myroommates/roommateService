package be.flo.roommateService.services.impl;

import be.flo.roommateService.models.entities.Survey;
import play.db.jpa.JPA;
import be.flo.roommateService.services.SurveyService;

/**
 * Created by florian on 19/02/15.
 */
public class SurveyServiceImpl extends CrudServiceImpl<Survey> implements SurveyService {

    @Override
    public Survey getByKey(String key) {


        String r = "SELECT s FROM Survey s WHERE s.key =:key";

        return JPA.em().createQuery(r,Survey.class)
                .setParameter("key",key)
                .getSingleResult();
    }
}
