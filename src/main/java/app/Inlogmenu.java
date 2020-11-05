package app;

import dao.GebruikerDao;
import domein.Gebruiker;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import static app.GebruikerInput.gebruikerInput;

public class Inlogmenu {

    EntityManager em = Persistence.createEntityManagerFactory("marktbayDB").createEntityManager();
    GebruikerDao gd = new GebruikerDao(em);


    public void start(){
        System.out.println();
        System.out.println("******************************************");
        System.out.println("Welkom bij de marktbay inlogpagina!");
        System.out.println("Hier kunt u inloggen.");
        System.out.println("******************************************");
        System.out.println();

        System.out.print("Uw gebruikersnaam: ");
        String gebruikersnaam = gebruikerInput();

        System.out.print("Uw wachtwoord: ");
        String wachtwoord = String.valueOf(gebruikerInput().hashCode());

        Gebruiker g = valideerGebruikersnaam(gebruikersnaam);
        if (!(g == null)) {
            valideerWachtwoord(wachtwoord, g);

            System.out.println("Welkom " + g.getGebruikersnaam() + ", u wordt doorverwezen naar het gebruikersmenu.");
            new Gebruikersmenu().start(g);
        }
    }

    private Gebruiker valideerGebruikersnaam(String gebruikersnaam){
        Gebruiker g = null;

        try{
            g = gd.zoekOpGebruikersnaam(gebruikersnaam);
        } catch (NoResultException e){
            System.out.println("Gebruikersnaam niet gevonden, u wordt verwezen naar het hoofdmenu om een account te maken.");
            new Hoofdmenu().start();
        }

        return g;
    }

    private void valideerWachtwoord(String wachtwoord, Gebruiker g) {
            if (g.getWachtwoord().equals(wachtwoord)) {
            } else {
                System.out.println("Uw wachtwoord klopt niet, probeer het nog eens.");
                new Inlogmenu().start();
            }
    }
}
