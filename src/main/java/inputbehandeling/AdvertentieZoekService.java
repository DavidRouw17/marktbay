package inputbehandeling;

import app.Gebruikersmenu;
import dao.DienstCategorieDao;
import dao.ProductCategorieDao;
import domein.DienstCategorie;
import domein.Gebruiker;
import domein.ProductCategorie;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

import static app.GebruikerInput.gebruikerInput;

public class AdvertentieZoekService {

    EntityManager em = Persistence.createEntityManagerFactory("marktbayDB").createEntityManager();
    ProductCategorieDao pd = new ProductCategorieDao(em);
    DienstCategorieDao dd = new DienstCategorieDao(em);
    private Gebruiker gebruiker;

    public AdvertentieZoekService(Gebruiker g){
        this.gebruiker = g;
    }

    public void start(){
        System.out.println("Waar wil je op zoeken?");
        System.out.println("[1] Een product (op categorie)");
        System.out.println("[2] Een dienst (op categorie)");
        System.out.println("[terug] Ga terug naar gebruikersmenu");

        switch(gebruikerInput()){
            case "1":
                zoekProductOpCategorie(); break;
            case "2":
                zoekDienstOpCategorie(); break;
            case "terug":
                new Gebruikersmenu().start(gebruiker); break;
            default:
                System.out.println("Input niet herkend, probeer het nog eens.");
                new AdvertentieZoekService(gebruiker).start(); break;
        }
    }

    public void zoekDienstOpCategorie(){
        System.out.println("Je kan kiezen uit de volgende categorieën: ");
        List<DienstCategorie> catList = dd.findAll();
        ArrayList<String> catNamen = new ArrayList<>();
        catList.forEach(c -> catNamen.add(c.getCategorie()));
        catNamen.forEach(System.out::println);

        System.out.print("Uw keuze: ");
        String keuze = gebruikerInput();
        if (catNamen.contains(keuze)){
            dd.zoekOpNaam(keuze).getDienstenMetDezeCategorie().forEach(System.out::println);
            start();
        }
        else {
            System.out.println("Categorie niet gevonden, probeer het nog eens.");
            zoekDienstOpCategorie();
        }
    }

    public void zoekProductOpCategorie(){
        System.out.println("Je kan kiezen uit de volgende categorieën: ");
        List<ProductCategorie> catList = pd.findAll();
        ArrayList<String> catNamen = new ArrayList<>();
        catList.forEach(c -> catNamen.add(c.getCategorie()));
        catNamen.forEach(System.out::println);

        System.out.print("Uw keuze: ");
        String keuze = gebruikerInput();
        if (catNamen.contains(keuze)){
            pd.zoekOpNaam(keuze).getProductenMetDezeCategorie().forEach(System.out::println);
            start();
        }
        else {
            System.out.println("Categorie niet gevonden, probeer het nog eens.");
            zoekDienstOpCategorie();
        }
    }
}
