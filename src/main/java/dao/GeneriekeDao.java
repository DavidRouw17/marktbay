package dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GeneriekeDao<T> {

    protected EntityManager em;


    public GeneriekeDao(EntityManager em) {
        this.em = em;
    }

    public T get(long id) {
        return em.find(T(), id);
    }

    public T getDetached(long id) {
        T t = em.find(T(), id);
        em.detach(t);
        return t;
    }


    public T slaOpInDB(T e) {
        em.getTransaction().begin();
        em.persist(e);
        detach();
        em.getTransaction().commit();
        return e;
    }

    void detach() {
        em.flush();
        em.clear();
    }

    public void updateAndDetach(T e) {
        em.getTransaction().begin();
        T merged = em.merge(e);
        detach();
        em.getTransaction().commit();
    }

    public void remove(T e) {
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    public boolean isManaged(T e) {
        return em.contains(e);
    }

    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + typeSimple() + " e ", T()).getResultList();
    }

    public List<T> findAllWithNamedQuery() {
        TypedQuery<T> tq = em.createNamedQuery(typeSimple() + ".findAll", T());
        return tq.getResultList();
    }

    public int dbSize(){
        return findAll().size();
    }

    private String typeSimple() {
        return T().getSimpleName();
    }

    @SuppressWarnings("unchecked")
    private Class<T> T() {
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
