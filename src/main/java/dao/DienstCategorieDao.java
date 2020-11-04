package dao;

import domein.DienstCategorie;
import domein.ProductCategorie;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class DienstCategorieDao extends GeneriekeDao<DienstCategorie> {
    public DienstCategorieDao(EntityManager em) {
        super(em);
    }

    public DienstCategorie zoekOpNaam(String naam){
        TypedQuery<DienstCategorie> namedQuery = em.createNamedQuery("DienstCategorie.findByName", DienstCategorie.class);
        namedQuery.setParameter("naam", naam);
        return namedQuery.getSingleResult();
    }
}
