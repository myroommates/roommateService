package services;

import com.avaje.ebean.Ebean;
import entities.Category;
import entities.Home;
import entities.technical.AuditedAbstractEntity;
import util.ErrorMessage;
import util.exception.MyRuntimeException;

import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class CategoryService {

    private ErrorMessageService errorMessageService = new ErrorMessageService();

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

    public Category findByHomeAndName(Home home, String name) {
        List<Category> list = Ebean.createNamedQuery(Category.class, Category.FIND_BY_HOME_AND_NAME)
                .setParameter(Category.COL_HOME, home)
                .setParameter(Category.COL_NAME, name)
                .findList();

        if (list.size() > 1) {
            String ids = "";
            for (Category category : list) {
                ids += category.getId() + "/";
            }

            throw new MyRuntimeException(errorMessageService.getMessage(ErrorMessage.UNIQUE_CONSTRAIN_VIOLATION, "category", "home and name", ids));
        }

        if (list.size() == 1) {
            return list.get(0);
        }

        return null;

    }
}
