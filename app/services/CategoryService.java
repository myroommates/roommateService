package services;

import com.avaje.ebean.Ebean;
import entities.Category;
import entities.Home;
import entities.technical.AuditedAbstractEntity;

import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class CategoryService {

    public void saveOrUpdate(Category category) {
        if (category.getId() != null) {
            category.update();
        } else {
            category.save();
        }
    }

    public Category findById(Long id) {
        return Ebean.createNamedQuery(Category.class, Category.FIND_BY_ID)
                .setParameter(AuditedAbstractEntity.COL_ID, id)
                .findUnique();
    }

    public void remove(Category category) {
        category.delete();
    }

    public List<Category> findByHome(Home home) {

        return Ebean.createNamedQuery(Category.class, Category.FIND_BY_HOME)
                .setParameter(Category.COL_HOME, home)
                .findList();
    }
}
