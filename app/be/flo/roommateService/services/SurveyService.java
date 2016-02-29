package be.flo.roommateService.services;

import be.flo.roommateService.models.entities.Survey;

/**
 * Created by florian on 19/02/15.
 */
public interface SurveyService extends CrudService<Survey> {

    public Survey getByKey(String key);

}
