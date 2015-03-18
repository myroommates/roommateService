package services;

import models.entities.technical.AbstractEntity;

/**
 * Created by florian on 17/12/14.
 */
public interface CrudService<T extends AbstractEntity> {

    public void saveOrUpdate(T t);

    public T findById(Long id);

    public void remove(T entity);
}
