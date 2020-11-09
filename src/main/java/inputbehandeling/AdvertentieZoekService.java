package inputbehandeling;

import app.Gebruikersmenu;
import app.Hoofdmenu;
import dao.AdvertentieDao;
import dao.DienstCategorieDao;
import dao.GebruikerDao;
import dao.ProductCategorieDao;
import domein.*;
import util.Console;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class AdvertentieZoekService {

    EntityManager em;
    GebruikerDao gd;
    ProductCategorieDao pd;
    DienstCategorieDao dd;
    AdvertentieDao ad;
    Console c;
    private final Gebruiker gebruiker;

    public AdvertentieZoekService(Gebruiker g) {
        this.c = new Console();
        this.em = Persistence.createEntityManagerFactory("marktbayDB").createEntityManager();
        this.gd = new GebruikerDao(em);
        this.pd = new ProductCategorieDao(em);
        this.dd = new DienstCategorieDao(em);
        this.ad = new AdvertentieDao(em);
        this.gebruiker = gd.get(g.getId());
    }

    public AdvertentieZoekService(Gebruiker g, Console c, EntityManager em) {
        this.c = c;
        this.em = em;
        this.gd = new GebruikerDao(em);
        this.pd = new ProductCategorieDao(em);
        this.dd = new DienstCategorieDao(em);
        this.ad = new AdvertentieDao(em);
        this.gebruiker = gd.get(g.getId());
    }

    public void start() {
        System.out.println("Waar wil je op zoeken?");
        System.out.println("[1] Een product (op categorie)");
        System.out.println("[2] Een dienst (op categorie)");
        System.out.println("[terug] Ga terug naar gebruikersmenu");
        System.out.print("Uw keuze: ");

        switch (c.vraagInput()) {
            case "1":
                zoekProductOpCategorie();
                break;
            case "2":
                zoekDienstOpCategorie();
                break;
            case "terug":
                new Gebruikersmenu(c).start(gebruiker);
                break;
            default:
                System.out.println("Input niet herkend, probeer het nog eens.");
                new AdvertentieZoekService(gebruiker, c, em).start();
                break;
        }
    }

    void zoekDienstOpCategorie() {
        System.out.println("Je kan kiezen uit de volgende categorieën: ");
        List<DienstCategorie> catList = dd.findAll();
        ArrayList<String> catNamen = new ArrayList<>();
        catList.forEach(c -> catNamen.add(c.getCategorie()));
        catNamen.forEach(System.out::println);

        System.out.print("Uw keuze: ");
        String keuze = c.vraagInput();
        if (catNamen.contains(keuze)) {
            List<Dienst> l = dd.zoekOpNaam(keuze).getDienstenMetDezeCategorie();
            l.forEach(System.out::println);
            if (l.size() == 0) System.out.println("Er zijn nog geen diensten met deze categorie toegevoegd..");
            start();
        } else {
            System.out.println("Categorie niet gevonden, probeer het nog eens.");
            zoekDienstOpCategorie();
        }
    }

    void zoekProductOpCategorie() {
        System.out.println("Je kan kiezen uit de volgende categorieën: ");
        List<ProductCategorie> catList = pd.findAll();
        ArrayList<String> catNamen = new ArrayList<>();
        catList.forEach(c -> catNamen.add(c.getCategorie()));
        catNamen.forEach(System.out::println);

        System.out.print("Uw keuze: ");
        String keuze = c.vraagInput();
        if (catNamen.contains(keuze)) {
            List<Product> l = pd.zoekOpNaam(keuze).getProductenMetDezeCategorie();
            l.forEach(System.out::println);

            if (l.size() == 0) {
                System.out.println("Er zijn nog geen producten met deze categorie toegevoegd..");
            }
            start();
        } else {
            System.out.println("Categorie niet gevonden, probeer het nog eens.");
            zoekProductOpCategorie();
        }
    }

    public void zoekEigenAdvertenties() {
        System.out.println();
        System.out.println("Uw advertenties: ");
        System.out.println();
        List<Advertentie> advList = gebruiker.getAangebodenAdvertenties();
        advList.forEach(System.out::println);
        new Gebruikersmenu(c).start(gebruiker);
    }

}
