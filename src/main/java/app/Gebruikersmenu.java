package app;

import domein.Gebruiker;
import inputbehandeling.AdvertentieService;

import static app.GebruikerInput.gebruikerInput;

public class Gebruikersmenu {

    public void start(Gebruiker g){
        System.out.println();
        System.out.println("******************************************");
        System.out.println("Welkom " + g.getGebruikersnaam() + ", dit is het gebruikersmenu.");
        System.out.println("Toets uw keuze!");
        System.out.println("******************************************");
        System.out.println();
        System.out.println("[1] Zoek advertentie");
        System.out.println("[2] Bekijk eigen advertenties");
        System.out.println("[3] Maak nieuwe advertentie");
        System.out.println("[x] Sluit MarktBay");
        System.out.println("[terug] Log uit");
        System.out.println();
        System.out.print("Uw keuze: ");

        switch (gebruikerInput()){
            case "1":
            case "2":
            case "3":
                new AdvertentieService(g).nieuweAdvertentie(); break;
            case "x":
                System.out.println("Fijne dag gewenst!"); break;
            case "terug":
                new Hoofdmenu().start(); break;
            default:
                System.out.println("Input niet herkend, probeer het nog eens.");
                new Gebruikersmenu().start(g); break;
        }
    }
}
