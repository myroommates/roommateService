package services;

import models.entities.Faq;
import models.entities.Survey;

import java.util.List;

/**
 * Created by florian on 19/02/15.
 */
public interface SurveyService extends CrudService<Survey> {

    public List<Survey> getAll();

    public Survey getByKey(String key);

}
