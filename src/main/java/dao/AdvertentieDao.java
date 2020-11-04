package dao;

import domein.Advertentie;

import javax.persistence.EntityManager;

public class AdvertentieDao extends GeneriekeDao<Advertentie> {
    public AdvertentieDao(EntityManager em) {
        super(em);
    }
}
