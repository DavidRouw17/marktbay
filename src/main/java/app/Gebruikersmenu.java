package app;

import domein.Gebruiker;
import inputbehandeling.AdvertentieService;
import inputbehandeling.AdvertentieZoekService;
import util.Console;

public class Gebruikersmenu {

    private Console c;


    public Gebruikersmenu() {
        c = new Console();
    }

    public Gebruikersmenu(Console c) {
        this.c = c;
    }

    public void start(Gebruiker g) {
        System.out.println();
        System.out.println("******************************************");
        System.out.println("Welkom " + g.getGebruikersnaam() + ", dit is het gebruikersmenu.");
        System.out.println("Toets uw keuze!");
        System.out.println("******************************************");
        System.out.println();
        System.out.println("[1] Zoek advertentie");
        System.out.println("[2] Bekijk eigen advertenties");
        System.out.println("[3] Maak nieuwe advertentie");
        System.out.println("[4] Verwijder advertentie");
        System.out.println("[x] Sluit MarktBay");
        System.out.println("[terug] Log uit");
        System.out.println();
        System.out.print("Uw keuze: ");

        switch (c.vraagInput()) {
            case "1":
                new AdvertentieZoekService(g).start();
                break;
            case "2":
                new AdvertentieZoekService(g).zoekEigenAdvertenties();
                break;
            case "3":
                new AdvertentieService(g).nieuweAdvertentie();
                break;
            case "4":
                new AdvertentieService(g).verwijderAdvertentie();
                break;
            case "x":
                System.out.println("Fijne dag gewenst!");
                break;
            case "terug":
                new Hoofdmenu().start();
                break;
            default:
                System.out.println("Input niet herkend, probeer het nog eens.");
                new Gebruikersmenu().start(g);
                break;
        }
    }
}
