package app;


import inputbehandeling.GebruikerService;
import util.Console;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Hoofdmenu {

    EntityManager em = Persistence.createEntityManagerFactory("marktbayDB").createEntityManager();
    Console c;

    public Hoofdmenu() {
        c = new Console();
    }

    public Hoofdmenu(Console c) {
        this.c = c;
    }

    public void start() {
        System.out.println();
        System.out.println("******************************************");
        System.out.println("Welkom bij het MarktBay hoofdmenu!");
        System.out.println("Toets uw keuze!");
        System.out.println("******************************************");

        System.out.println("[1] Inloggen");
        System.out.println("[2] Registreren nieuwe gebruiker");
        System.out.println("[x] Afsluiten");
        System.out.print("Uw keuze: ");

        switch (c.vraagInput()) {
            case "1":
                new Inlogmenu().start();
                break;
            case "2":
                new GebruikerService(em, c).nieuweGebruiker();
                break;
            case "x":
                System.out.println("Tot ziens!");
                break;
            default:
                System.out.println("Ongeldige keuze, probeer het nog eens!");
                new Hoofdmenu().start();
                break;
        }
    }
}
