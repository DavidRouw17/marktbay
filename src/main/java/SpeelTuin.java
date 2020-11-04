import app.Gebruikersmenu;
import dao.AdvertentieDao;
import dao.DienstCategorieDao;
import dao.GebruikerDao;
import dao.ProductCategorieDao;
import domein.*;
import enums.Bezorgwijze;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class SpeelTuin {

    public static void main(String[] args) {
    //new SpeelTuin().vulDB();
        EntityManager em = Persistence.createEntityManagerFactory("marktbayDB").createEntityManager();
        GebruikerDao gd = new GebruikerDao(em);
        ProductCategorieDao pd = new ProductCategorieDao(em);
        AdvertentieDao ad = new AdvertentieDao(em);
        DienstCategorieDao dd = new DienstCategorieDao(em);



    }

    public void vulDB(){
        EntityManager em = Persistence.createEntityManagerFactory("marktbayDB").createEntityManager();
        GebruikerDao gd = new GebruikerDao(em);
        ProductCategorieDao pd = new ProductCategorieDao(em);
        AdvertentieDao ad = new AdvertentieDao(em);

        Gebruiker d = new Gebruiker("David", "David", "David");
        gd.slaOpInDB(d);

        Product p = new Product("Mooie fiets!", 12.00, d);

        pd.slaOpInDB(new ProductCategorie("Fietsen"));
        pd.slaOpInDB(new ProductCategorie("Tegels"));
        pd.slaOpInDB(new ProductCategorie("Autoos"));
        pd.slaOpInDB(new ProductCategorie("Trump"));

        p.addCategorie(pd.zoekOpNaam("Fietsen"));

        gd.updateAndDetach(d);

    }
}
