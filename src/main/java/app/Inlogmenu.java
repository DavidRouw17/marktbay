package app;

import dao.GebruikerDao;
import domein.Gebruiker;
import util.Console;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

public class Inlogmenu {

    EntityManager em;
    GebruikerDao gd;
    Console c;

    public Inlogmenu() {
        this.em = Persistence.createEntityManagerFactory("marktbayDB").createEntityManager();
        this.gd = new GebruikerDao(em);
        this.c = new Console();
    }

    public Inlogmenu(EntityManager em, Console c) {
        this.em = em;
        this.gd = new GebruikerDao(em);
        this.c = c;
    }

    public void start() {
        System.out.println();
        System.out.println("******************************************");
        System.out.println("Welkom bij de marktbay inlogpagina!");
        System.out.println("Hier kunt u inloggen.");
        System.out.println("******************************************");
        System.out.println();

        System.out.print("Uw gebruikersnaam: ");
        String gebruikersnaam = c.vraagInput();

        System.out.print("Uw wachtwoord: ");
        String wachtwoord = String.valueOf(c.vraagInput().hashCode());

        Gebruiker g = valideerGebruikersnaam(gebruikersnaam);
        if (!(g == null)) {
            valideerWachtwoord(wachtwoord, g);

            System.out.println("Welkom " + g.getGebruikersnaam() + ", u wordt doorverwezen naar het gebruikersmenu.");
            new Gebruikersmenu(c).start(g);
        }
    }

    Gebruiker valideerGebruikersnaam(String gebruikersnaam) {
        Gebruiker g = null;

        try {
            g = gd.zoekOpGebruikersnaam(gebruikersnaam);
        } catch (NoResultException e) {
            System.out.println("Gebruikersnaam niet gevonden, u wordt verwezen naar het hoofdmenu om een account te maken.");
            new Hoofdmenu(c).start();
        }

        return g;
    }

    void valideerWachtwoord(String wachtwoord, Gebruiker g) {
        if (g.getWachtwoord().equals(wachtwoord)) {
        } else {
            System.out.println("Uw wachtwoord klopt niet, probeer het nog eens.");
            new Inlogmenu(em, c).start();
        }
    }
}
