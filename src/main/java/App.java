import app.Hoofdmenu;
import dao.GebruikerDao;
import domein.Gebruiker;
import inputbehandeling.AdvertentieService;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class App {


    public static void main(String[] args) {
//        EntityManager em = Persistence.createEntityManagerFactory("marktbayDB").createEntityManager();
//        GebruikerDao gd = new GebruikerDao(em);
//        Gebruiker g = gd.zoekOpGebruikersnaam("David");
//
//        new AdvertentieService(g).nieuweAdvertentie();
        new Hoofdmenu().start();
    }


}
