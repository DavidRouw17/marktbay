package dao;

import domein.Gebruiker;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class GebruikerDao extends GeneriekeDao<Gebruiker> {

    public GebruikerDao(EntityManager em){
        super(em);
    }

//    @Override
//    public void slaOpInDB(Gebruiker g) {
//        ArrayList<String> mailList = emailadresLijst();
//        ArrayList<String> gebruikersnaamLijst = gebruikersNamenLijst();
//        if (!mailList.contains(g.getEmailadres()) || !gebruikersnaamLijst.contains(g.getGebruikersnaam())){
//            em.getTransaction().begin();
//            em.persist(g);
//            detach();
//            em.getTransaction().commit();
//        }
//        //performance?
//    }

    public Gebruiker zoekOpGebruikersnaam(String gebruikersnaam){
        TypedQuery<Gebruiker> namedQuery = em.createNamedQuery("Gebruiker.zoekOpGebruikersnaam", Gebruiker.class);
        namedQuery.setParameter("gebruikersnaam", gebruikersnaam);
        return namedQuery.getSingleResult();
    }

    public ArrayList<String> gebruikersNamenLijst(){
        ArrayList<String> result = new ArrayList<>();
        TypedQuery<Gebruiker> q = em.createQuery("SELECT g FROM Gebruiker g", Gebruiker.class);
        q.getResultList().forEach(r -> result.add(r.getGebruikersnaam()));
        return result;
    }

    public ArrayList<String> emailadresLijst(){
        ArrayList<String> result = new ArrayList<>();
        TypedQuery<Gebruiker> q = em.createQuery("SELECT g FROM Gebruiker g", Gebruiker.class);
        q.getResultList().forEach(r -> result.add(r.getEmailadres()));
        return result;
    }
}
