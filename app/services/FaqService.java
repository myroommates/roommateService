package services;

import models.entities.Event;
import models.entities.Faq;

import java.util.List;

/**
 * Created by florian on 19/02/15.
 */
public interface FaqService extends CrudService<Faq> {

    public List<Faq> getAll();
}
