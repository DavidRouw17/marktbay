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

public class AdvertentieService {

    private final EntityManager em;
    private final GebruikerDao gd;
    private final AdvertentieDao ad;
    private final ProductCategorieDao pd;
    private final DienstCategorieDao dd;
    private final Gebruiker gebruiker;
    private final Console c;

    public AdvertentieService(Gebruiker g) {
        this.c = new Console();
        this.em = Persistence.createEntityManagerFactory("marktbayDB").createEntityManager();
        gd = new GebruikerDao(em);
        ad = new AdvertentieDao(em);
        pd = new ProductCategorieDao(em);
        dd = new DienstCategorieDao(em);
        this.gebruiker = gd.get(g.getId());
    }

    public AdvertentieService(Gebruiker g, Console c, EntityManager em) {
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

        if (((Product) a).getCategorieLijst().size() == 0) {
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
                System.out.println(s + " toegevoegd!");
            } catch (Exception e) {
                System.out.println("Niet gelukt om " + s + " toe te voegen..");
            }
        }
        if (((Dienst) a).getCategorieLijst().size() == 0) {
            System.out.println("Geen categorieën toegevoegd. Probeer het nog eens!");
            voegDienstCategorieToe(a);
        }

    }

    String verkrijgTitel() {
        System.out.print("Voer hier uw titel in: ");
        String titel = c.vraagInput();
        boolean teLang = titel.length() > 254;
        boolean isLeeg = titel.trim().length() == 0;

        while (teLang || isLeeg) {
            if (teLang) {
                System.out.print("Titel te lang! Probeer het nog eens: ");

            } else {
                System.out.print("Niks ingevuld, probeer het nog eens: ");
            }
            titel = c.vraagInput();
            teLang = titel.length() > 254;
            isLeeg = titel.trim().length() == 0;
        }
        return titel;
    }

    Double verkrijgPrijs() {
        System.out.print("Voer hier uw prijs in: ");
        double prijs = 0;
        boolean nietGoedIngevuld = true;
        while (nietGoedIngevuld) {
            try {
                prijs = Double.parseDouble(c.vraagInput());
            } catch (NumberFormatException e) {
                System.out.print("Geen geldig getal ingevoerd, probeer het nog eens: ");
                continue;
            }
            nietGoedIngevuld = false;
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
        new Gebruikersmenu(c).start(gebruiker);
    }

    void voegBeschrijvingToe(Advertentie a) {
        System.out.println();
        System.out.print("Voeg hier uw omschrijving toe: ");
        String beschrijving = c.vraagInput();
        boolean teLang = beschrijving.length() > 250;
        boolean isLeeg = beschrijving.trim().length() == 0;

        while (teLang || isLeeg) {
            if (teLang) {
                System.out.println("Titel te lang! Probeer het nog eens.");
                System.out.print("Voeg hier uw omschrijving toe: ");
            } else {
                System.out.println("Niks ingevuld, probeer het nog eens.");
                System.out.print("Voeg hier uw omschrijving toe: ");
            }
            beschrijving = c.vraagInput();
            teLang = beschrijving.length() > 250;
            isLeeg = beschrijving.trim().length() == 0;
        }
        a.setOmschrijving(beschrijving);
    }

    void voegBijlageToe(Advertentie a) {
        System.out.println();
        System.out.print("Voeg hier uw bijlage toe: ");
        String bijlage = c.vraagInput();
        boolean teLang = bijlage.length() > 250;
        boolean isLeeg = bijlage.trim().length() == 0;
        while (teLang || isLeeg) {
            if (teLang) {
                System.out.println("Titel te lang! Probeer het nog eens.");
                System.out.print("Voeg hier uw bijlage toe: ");
            } else {
                System.out.println("Niks ingevuld, probeer het nog eens.");
                System.out.print("Voeg hier uw bijlage toe: ");
            }
            bijlage = c.vraagInput();
            teLang = bijlage.length() > 250;
            isLeeg = bijlage.trim().length() == 0;
        }
        a.setBijlage(bijlage);
    }

    public void verwijderAdvertentie(){
        List<Advertentie> advList = gebruiker.getAangebodenAdvertenties();
        if (advList.size() == 0){
            System.out.println("U heeft geen advertenties.");
            new Gebruikersmenu(c).start(gebruiker);
        } else {
            System.out.println();
            System.out.println("Uw advertenties: ");
            System.out.println();
            int counter = 0;
            for (Advertentie advertentie : advList) {
                counter ++;
                System.out.println("[" + counter + "] " + "\n" + advertentie);
            }
            System.out.print("Toets het nummer van de advertentie die u wilt verwijderen: ");
            try{
                int i = Integer.parseInt(c.vraagInput());
                if (i > 0 && i <= counter){
                    ad.remove(advList.get(i - 1));
                    System.out.println("Advertentie verwijderd!");
                } else {
                    System.out.println("Dat getal was geen optie.");
                }
                new Gebruikersmenu(c).start(gebruiker);
            } catch (NumberFormatException e) {
                System.out.println("Input niet herkend. U wordt terug gestuurd naar het hoofdmenu.");
                new Hoofdmenu(c).start();
            }
        }
    }
}
