package dao;

import domein.ProductCategorie;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ProductCategorieDao extends GeneriekeDao<ProductCategorie> {

    public ProductCategorieDao(EntityManager em) {
        super(em);
    }

    public ProductCategorie zoekOpNaam(String naam) {
        TypedQuery<ProductCategorie> namedQuery = em.createNamedQuery("ProductCategorie.findByName", ProductCategorie.class);
        namedQuery.setParameter("naam", naam);
        return namedQuery.getSingleResult();
    }
}
