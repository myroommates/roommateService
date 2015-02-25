package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.Faq;
import models.entities.Roommate;
import models.entities.Survey;
import services.FaqService;
import services.SurveyService;

import java.util.List;

/**
 * Created by florian on 19/02/15.
 */
public class SurveyServiceImpl extends CrudServiceImpl<Survey> implements SurveyService {

    @Override
    public List<Survey> getAll() {
        return Ebean.find(Survey.class).findList();
    }

    @Override
    public Survey getByKey(String key) {
        List<Survey> list = Ebean.createNamedQuery(Survey.class, Survey.FIND_BY_KEY)
                .setParameter(Survey.PARAM_KEY, key)
                .findList();

        if (list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
