package inputbehandeling;

import app.Gebruikersmenu;
import app.Hoofdmenu;
import dao.GebruikerDao;
import domein.Gebruiker;
import enums.Bezorgwijze;
import exceptions.AanmaakGebruikerAfgebrokenException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Scanner;

public class GebruikerService {

    EntityManager em = Persistence.createEntityManagerFactory("marktbayDB").createEntityManager();
    GebruikerDao gd = new GebruikerDao(em);
    Scanner in = new Scanner(System.in);


    public void nieuweGebruiker() {
        System.out.println();
        System.out.println("******************************************");
        System.out.println("Welkom nieuwe gebruiker!");
        System.out.println("******************************************");
        try {
            String gebruikersnaam = gebruikersnaamMetChecks();
            String email = emailadresMetChecks();
            String wachtwoord = wachtwoordMetChecks();

            Gebruiker nieuweGebruiker = new Gebruiker(gebruikersnaam, email, wachtwoord);
            verzendwijzenKiezen(nieuweGebruiker);
            gd.slaOpInDB(nieuweGebruiker);

            System.out.println("******************************************");
            System.out.println("Registratie compleet.");
            System.out.println("Welkom, " + gebruikersnaam + "!");
            System.out.println("U wordt nu doorverwezen naar het gebruikersmenu!");
            System.out.println("******************************************");

            new Gebruikersmenu().start(nieuweGebruiker);

        } catch (AanmaakGebruikerAfgebrokenException e) {
            System.out.println("Registratie afgebroken. Graag tot ziens!");
            new Hoofdmenu().start();
        }

    }


    private String gebruikersnaamMetChecks() {
        ArrayList<String> bestaandeNamen = gd.gebruikersNamenLijst();

        System.out.print("Gewenste gebruikersnaam (max 20 tekens): ");
        String gebruikersnaam = in.nextLine();

        boolean teLang = gebruikersnaam.length() > 20;
        boolean bestaatAl = bestaandeNamen.contains(gebruikersnaam);

        while (teLang || bestaatAl) {
            if (teLang) {
                System.out.println("Gebruikersnaam te lang! Probeer het nog eens, of typ 'exit'");
                System.out.print("Gewenste gebruikersnaam (max 20 tekens): ");
            } else {
                System.out.println("Gebruikersnaam bestaat al! Probeer het nog eens, of typ 'exit'");
                System.out.print("Gewenste gebruikersnaam (max 20 tekens): ");
            }

            gebruikersnaam = in.nextLine();
            if (gebruikersnaam.equals("exit")) throw new AanmaakGebruikerAfgebrokenException();

            teLang = gebruikersnaam.length() > 20;
            bestaatAl = bestaandeNamen.contains(gebruikersnaam);

        }
        return gebruikersnaam;
    }

    private String emailadresMetChecks() {
        ArrayList<String> bestaandeEmails = gd.emailadresLijst();
        System.out.print("Uw emailadres: ");
        String email = in.nextLine();


        boolean teLang = email.length() > 250;
        boolean bestaatAl = bestaandeEmails.contains(email);

        while (teLang || bestaatAl) {
            if (teLang) {
                System.out.println("Emailadres te lang! Probeer het nog eens, of typ 'exit'");
                System.out.print("Uw emailadres: ");
            } else {
                System.out.println("Emailadres bestaat al! Probeer het nog eens, of typ 'exit'");
                System.out.println("Uw emailadres: ");
            }

            email = in.nextLine();
            if (email.equals("exit")) throw new AanmaakGebruikerAfgebrokenException();

            teLang = email.length() > 250;
            bestaatAl = bestaandeEmails.contains(email);

        }
        return email;
    }

    private String wachtwoordMetChecks() {
        System.out.print("Uw wachtwoord: ");
        String wachtwoord = in.nextLine();
        boolean teLang = wachtwoord.length() > 250;

        while (teLang) {
            System.out.println("Wachtwoord te lang! Probeer het nog eens, of typ 'exit'");
            System.out.println("Uw wachtwoord: ");
            wachtwoord = in.nextLine();
            if (wachtwoord.equals("exit")) throw new AanmaakGebruikerAfgebrokenException();

            teLang = wachtwoord.length() > 250;
        }

        System.out.print("Typ nogmaals uw wachtwoord, ter verificatie: ");
        String wachtwoordV = in.nextLine();

        if (!wachtwoord.equals(wachtwoordV)) {
            System.out.println("Wachtwoord komt niet overeen! Probeer het nog eens.");
            wachtwoordMetChecks();
        }
        return wachtwoord;
    }

    private void verzendwijzenKiezen(Gebruiker g) {
        System.out.println();
        System.out.println("Kies uw verzendwijzen, u kunt kiezen uit:");
        for (Bezorgwijze value : Bezorgwijze.values()) {
            System.out.println("  -" + value.toString().toLowerCase());
        }
        System.out.println();
        System.out.println("Typ uw keuze(s), gescheiden door een komma en een spatie.");
        System.out.println("Bijvoorbeeld: optie1, optie2");
        System.out.print("Uw keuzes: ");
        String keuzes = in.nextLine();
        String[] keuzesSplit = keuzes.split(", ");
        for (String s : keuzesSplit) {
            switch (s) {
                case "afhalen":
                    g.addBezorgWijze(Bezorgwijze.AFHALEN);
                    adresToevoegen(g);
                    break;
                case "verzenden":
                    g.addBezorgWijze(Bezorgwijze.VERZENDEN);
                    break;
                case "magazijn":
                    g.addBezorgWijze(Bezorgwijze.MAGAZIJN);
                    break;
                case "rembours":
                    g.addBezorgWijze(Bezorgwijze.REMBOURS);
                    break;
                default:
                    System.out.println(s + " is geen goede input!");
                    break;
            }
        }
        if (g.getBezorgwijzen().size() == 0) {
            System.out.println("Het is niet gelukt om verzendwijzen toe te voegen. Probeer het nog eens!");
            verzendwijzenKiezen(g);
        }
        System.out.println("Uw voorkeuren zijn toegevoegd!");
    }

    private void adresToevoegen(Gebruiker g) {
        System.out.println();
        System.out.println("Omdat u voor de optie 'thuis afhalen' heeft gekozen, hebben we ook uw adres nodig:");
        System.out.print("Uw adres: ");
        String adres = in.nextLine();
        g.setAdres(adres);

        //todo if time permits nog checks toevoegen
    }


}
