package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.Faq;
import services.FaqService;

import java.util.List;

/**
 * Created by florian on 19/02/15.
 */
public class FaqServiceImpl extends CrudServiceImpl<Faq> implements FaqService {

    @Override
    public List<Faq> getAll() {
        return Ebean.find(Faq.class).findList();
    }
}
