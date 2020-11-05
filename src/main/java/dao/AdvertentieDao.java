package dao;

import domein.Advertentie;
import domein.Dienst;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AdvertentieDao extends GeneriekeDao<Advertentie> {
    public AdvertentieDao(EntityManager em) {
        super(em);
    }

    public List<Advertentie> zoekOpSoort(String soort){
        TypedQuery<Advertentie> namedQuery = em.createNamedQuery("Advertentie.findBySoort", Advertentie.class);
        namedQuery.setParameter("soort", soort);
        return namedQuery.getResultList();
    }

    
}
