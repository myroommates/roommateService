package services.impl;

import com.avaje.ebean.Ebean;
import models.entities.technical.AbstractEntity;
import services.CrudService;

import java.lang.reflect.ParameterizedType;

/**
 * Created by florian on 17/12/14.
 */
public abstract class CrudServiceImpl<T extends AbstractEntity> implements CrudService<T> {


    protected Class<T> entityClass;

    public CrudServiceImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public void saveOrUpdate(T entity) {
        if (entity.getId() == null) {
            entity.save();
        } else {
            entity.update();
        }
    }

    @Override
    public T findById(Long id) {
        return Ebean.find(entityClass)
                .setId(id)
                .findUnique();
    }

    @Override
    public void remove(T entity) {
        entity.delete();
    }
}
