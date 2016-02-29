package be.flo.roommateService.services.impl;

import be.flo.roommateService.models.entities.technical.AbstractEntity;
import play.db.jpa.JPA;
import be.flo.roommateService.services.CrudService;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.text.Normalizer;
import java.util.List;

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
        try {
            JPA.em().persist(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public T findById(Long id) {

        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        cq.select(from);
        cq.where(cb.equal(from.get("id"), id));
        return getSingleResultOrNull(cq);

//        return JPA.em().createQuery("select p from " + entityClass.getName() + " p where p.id = :id")
//                .getParameter("id", id);
    }

    @Override
    public void remove(T entity) {
        JPA.em().remove(entity);
    }

    @Override
    public List<T> findAll() {
        return JPA.em().createQuery("select p from " + entityClass.getName() + " p").getResultList();
    }

    @Override
    public void saveOrUpdate(List<T> entities) {
        try {
            for (T entity : entities) {
                JPA.em().persist(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    protected T getSingleOrNull(TypedQuery<T> query) {
        List<T> resultList = query.getResultList();
        if (resultList.size() == 0) {
            return null;
        }
        if (resultList.size() > 1) {
            throw new RuntimeException("more than one result, expected max 1");
        }
        return resultList.get(0);

    }

    protected T getSingleResultOrNull(CriteriaQuery<T> cq) {
        T singleResult;
        try {
            singleResult = JPA.em().createQuery(cq).getSingleResult();
        } catch (NoResultException nre) {
            singleResult = null;
        }
        return singleResult;
    }

    protected T getFirstResultOrNull(CriteriaQuery<T> cq) {
        List<T> results;
        try {
            results = JPA.em().createQuery(cq).getResultList();
        } catch (NoResultException nre) {
            results = null;
        }
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    protected String normalizeForSearch(String s) {

        s = "%" + s.replaceAll(" ", "%") + "%";
        s = s.toLowerCase();
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[^\\p{ASCII}]", "");
        s = s.replaceAll("\\p{M}", "");
        return s;
    }
}
