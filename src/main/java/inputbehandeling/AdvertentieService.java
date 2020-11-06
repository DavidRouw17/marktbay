package inputbehandeling;

import app.Gebruikersmenu;
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

import static app.GebruikerInput.gebruikerInput;

public class AdvertentieService {

    EntityManager em;
    GebruikerDao gd;
    AdvertentieDao ad;
    ProductCategorieDao pd;
    DienstCategorieDao dd;
    Gebruiker gebruiker;
    Console c;

    public AdvertentieService(Gebruiker g) {
        this.c = new Console();
        this.em = Persistence.createEntityManagerFactory("marktbayDB").createEntityManager();
        gd = new GebruikerDao(em);
        ad = new AdvertentieDao(em);
        pd = new ProductCategorieDao(em);
        dd = new DienstCategorieDao(em);
        this.gebruiker = gd.get(g.getId());
    }

    public AdvertentieService(Gebruiker g, Console c, EntityManager em){
        //testing
        this.c = c;
        this.em = em;
        gd = new GebruikerDao(em);
        ad = new AdvertentieDao(em);
        pd = new ProductCategorieDao(em);
        dd = new DienstCategorieDao(em);
        this.gebruiker = gd.get(g.getId());
    }

    public void nieuweAdvertentie() {
        System.out.println();
        System.out.println("******************************************");
        System.out.println("Hier maak je je eigen advertentie. ");
        System.out.println("******************************************");
        System.out.println("Wat wil je aanbieden?");
        System.out.println();
        System.out.println("[1] Product");
        System.out.println("[2] Dienst");
        System.out.println("[terug] terug naar gebruikersmenu");
        System.out.print("Uw keuze: ");

        switch (c.vraagInput()) {
            case "1":
                nieuwProduct();
                break;
            case "2":
                nieuweDienst();
                break;
            case "terug":
                new Gebruikersmenu(c).start(gebruiker);
                break;
            default:
                System.out.println("Input onbekend, probeer het nog eens");
                nieuweAdvertentie();
                break;
        }
    }

     void nieuwProduct() {
        String titel = verkrijgTitel();
        Double prijs = verkrijgPrijs();
        Advertentie a = new Product(titel, prijs, gebruiker);
        voegProductCategorieToe(a);
        advertentieAfmaken(a);
    }

     void nieuweDienst() {
        String titel = verkrijgTitel();
        Double prijs = verkrijgPrijs();
        Advertentie a = new Dienst(titel, prijs, gebruiker);
        voegDienstCategorieToe(a);
        advertentieAfmaken(a);
    }

     void voegProductCategorieToe(Advertentie a) {
        List<ProductCategorie> catList = pd.findAll();
        ArrayList<String> catNamen = new ArrayList<>();
        catList.forEach(c -> catNamen.add(c.getCategorie()));
        System.out.println("Voeg hier de categorie(en) toe die van toepassing zijn op uw product.");
        System.out.println("U kunt kiezen uit: ");
        catNamen.forEach(System.out::println);
        System.out.println();
        System.out.println("Typ hier je keuzes, gescheiden door een komma en een spatie.");
        System.out.println("Voorbeeld: optie1, optie 2");
        System.out.print("Keuze(s): ");
        String keuzes = c.vraagInput();
        String[] keuzesSplit = keuzes.split(", ");

        for (String s : keuzesSplit) {
            try {
                ProductCategorie p = pd.zoekOpNaam(s);
                ((Product) a).addCategorie(p);
            } catch (Exception e) {
                System.out.println("Niet gelukt om " + s + " toe te voegen..");
            }
        }

        if (((Product)a).getCategorieLijst().size() == 0){
            System.out.println("Geen categorieën toegevoegd. Probeer het nog eens!");
            voegProductCategorieToe(a);
        }
    }

     void voegDienstCategorieToe(Advertentie a) {
        List<DienstCategorie> catList = dd.findAll();
        ArrayList<String> catNamen = new ArrayList<>();
        catList.forEach(c -> catNamen.add(c.getCategorie()));
        System.out.println("Voeg hier de categorie(en) toe die van toepassing zijn op uw product.");
        System.out.println("U kunt kiezen uit: ");
        catNamen.forEach(System.out::println);
        System.out.println();
        System.out.println("Typ hier je keuzes, gescheiden door een komma en een spatie.");
        System.out.println("Voorbeeld: optie1, optie 2");
        System.out.print("Keuze(s): ");
        String keuzes = c.vraagInput();
        String[] keuzesSplit = keuzes.split(", ");

        for (String s : keuzesSplit) {
            try {
                DienstCategorie p = dd.zoekOpNaam(s);
                ((Dienst) a).addCategorie(p);
            } catch (Exception e) {
                System.out.println("Niet gelukt om " + s + " toe te voegen..");
            }
        }
         if (((Dienst)a).getCategorieLijst().size() == 0){
             System.out.println("Geen categorieën toegevoegd. Probeer het nog eens!");
             voegDienstCategorieToe(a);
         }

    }

    String verkrijgTitel() {
        System.out.print("Voer hier uw titel in: ");
        String titel = c.vraagInput();
        if (titel.length() > 250) {
            System.out.println("Titel te lang! Probeer het nog eens.");
            verkrijgTitel();
        }
        if (titel.length() == 0) {
            System.out.println("Niks ingevuld, probeer het nog eens.");
            verkrijgTitel();
        }
        return titel;
    }

    Double verkrijgPrijs() {
        System.out.print("Voer hier uw prijs in: ");
        double prijs = 11.11d;
        try {
            prijs = Double.parseDouble(c.vraagInput());
        } catch (NumberFormatException e) {
            System.out.println("Geen geldig getal ingevoerd, probeer het nog eens.");
            verkrijgPrijs();
        } catch (NullPointerException e) {
            System.out.println("Niks ingevoerd, probeer het nog eens.");
            verkrijgPrijs();
        }
        return prijs;
    }

    void advertentieAfmaken(Advertentie a) {
        System.out.println("Wilt u een beschrijving toevoegen?");
        System.out.println("[1] ja");
        System.out.println("[2] nee");
        System.out.println();
        System.out.print("Toets uw keuze: ");
        if (c.vraagInput().equals("1")) voegBeschrijvingToe(a);

        System.out.println("Wilt u een bijlage toevoegen?");
        System.out.println("[1] ja");
        System.out.println("[2] nee");
        System.out.println();
        System.out.print("Toets uw keuze: ");
        if (c.vraagInput().equals("1")) voegBijlageToe(a);

        try {
            gd.updateAndDetach(gebruiker);
            System.out.println("Advertentie toegevoegd aan uw profiel!");
        } catch (RuntimeException e) {
            System.out.println("Er ging iets mis: " + e.getMessage() + ", bel uw java programmeur!");
        }
        new Gebruikersmenu().start(gebruiker);
    }

    void voegBeschrijvingToe(Advertentie a) {
        System.out.println();
        System.out.print("Voeg hier uw omschrijving toe: ");
        String beschrijving = c.vraagInput();
        if (beschrijving.length() > 250) {
            System.out.println("Titel te lang! Probeer het nog eens.");
            voegBeschrijvingToe(a);
        }
        if (beschrijving.trim().length() == 0) {
            System.out.println("Niks ingevuld, probeer het nog eens.");
            voegBeschrijvingToe(a);
        }
        a.setOmschrijving(beschrijving);
    }

    void voegBijlageToe(Advertentie a) {
        System.out.println();
        System.out.print("Voeg hier uw bijlage toe: ");
        String bijlage = c.vraagInput();
        if (bijlage.length() > 250) {
            System.out.println("Titel te lang! Probeer het nog eens.");
            voegBeschrijvingToe(a);
        }
        if (bijlage.trim().length() == 0) {
            System.out.println("Niks ingevuld, probeer het nog eens.");
            voegBeschrijvingToe(a);
        }
        a.setBijlage(bijlage);
    }
}
