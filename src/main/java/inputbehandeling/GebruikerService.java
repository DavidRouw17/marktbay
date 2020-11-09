package inputbehandeling;

import app.Gebruikersmenu;
import app.Hoofdmenu;
import dao.GebruikerDao;
import domein.Gebruiker;
import enums.Bezorgwijze;
import exceptions.AanmaakGebruikerAfgebrokenException;
import util.Console;

import javax.persistence.EntityManager;
import java.util.ArrayList;

public class GebruikerService {

    private GebruikerDao gd;
    private Console c;

    public GebruikerService(EntityManager em, Console c) {
        this.gd = new GebruikerDao(em);
        this.c = c;
    }

    public void nieuweGebruiker() {
        System.out.println();
        System.out.println("******************************************");
        System.out.println("Welkom nieuwe gebruiker!");
        System.out.println("******************************************");
        try {
            System.out.print("Gewenste gebruikersnaam (max 20 tekens): ");
            String gebruikersnaam = gebruikersnaamMetChecks(c.vraagInput());
            System.out.print("Uw emailadres: ");
            String email = emailadresMetChecks(c.vraagInput());
            System.out.print("Uw wachtwoord: ");
            String wachtwoord = wachtwoordMetChecks(c.vraagInput());

            Gebruiker nieuweGebruiker = new Gebruiker(gebruikersnaam, email, wachtwoord);
            verzendwijzenKiezen(nieuweGebruiker);
            gd.slaOpInDB(nieuweGebruiker);

            System.out.println("******************************************");
            System.out.println("Registratie compleet.");
            System.out.println("Welkom, " + gebruikersnaam + "!");
            System.out.println("U wordt nu doorverwezen naar het gebruikersmenu!");
            System.out.println("******************************************");

            new Gebruikersmenu(c).start(nieuweGebruiker);

        } catch (AanmaakGebruikerAfgebrokenException e) {
            System.out.println("Registratie afgebroken. Graag tot ziens!");
            new Hoofdmenu(c).start();
        }

    }


    String gebruikersnaamMetChecks(String g) {
        ArrayList<String> bestaandeNamen = gd.gebruikersNamenLijst();


        String gebruikersnaam = g;

        boolean teLang = gebruikersnaam.length() > 20;
        boolean bestaatAl = bestaandeNamen.contains(gebruikersnaam);
        boolean isLeeg = gebruikersnaam.trim().length() == 0;

        while (teLang || bestaatAl || isLeeg) {
            if (teLang) {
                System.out.println("Gebruikersnaam te lang! Probeer het nog eens, of typ 'exit'");
                System.out.print("Gewenste gebruikersnaam (max 20 tekens): ");
            } else if (bestaatAl) {
                System.out.println("Gebruikersnaam bestaat al! Probeer het nog eens, of typ 'exit'");
                System.out.print("Gewenste gebruikersnaam (max 20 tekens): ");
            } else {
                System.out.println("Vergeet niet iets te typen!");
                System.out.print("Gewenste gebruikersnaam (max 20 tekens): ");
            }

            gebruikersnaam = c.vraagInput();
            if (gebruikersnaam.equals("exit")) throw new AanmaakGebruikerAfgebrokenException();

            teLang = gebruikersnaam.length() > 20;
            bestaatAl = bestaandeNamen.contains(gebruikersnaam);
            isLeeg = gebruikersnaam.trim().length() == 0;

        }
        return gebruikersnaam;
    }

    String emailadresMetChecks(String e) {
        ArrayList<String> bestaandeEmails = gd.emailadresLijst();
        String email = e;

        boolean teLang = email.length() > 250;
        boolean bestaatAl = bestaandeEmails.contains(email);
        boolean legeString = email.trim().length() == 0;
        boolean spatieInMail = email.split(" ").length != 1;

        while (teLang || bestaatAl || legeString || spatieInMail) {
            if (teLang) {
                System.out.println("Emailadres te lang! Probeer het nog eens, of typ 'exit'");
                System.out.print("Uw emailadres: ");
            } else if (bestaatAl) {
                System.out.println("Emailadres bestaat al! Probeer het nog eens, of typ 'exit'");
                System.out.println("Uw emailadres: ");
            } else if (legeString) {
                System.out.println("Emailadres mag niet niks zijn! Probeer het nog eens, of typ 'exit'");
                System.out.println("Uw emailadres: ");
            } else {
                System.out.println("Emailadres kan geen spatie bevatten! Probeer het nog eens, of typ 'exit'");
                System.out.println("Uw emailadres: ");
            }

            email = c.vraagInput();
            if (email.equals("exit")) throw new AanmaakGebruikerAfgebrokenException();

            teLang = email.length() > 250;
            bestaatAl = bestaandeEmails.contains(email);
            legeString = email.trim().length() == 0;
            spatieInMail = email.split(" ").length != 1;

        }
        return email;
    }

    String wachtwoordMetChecks(String w) {
        String wachtwoord = w;
        boolean teLang = wachtwoord.length() > 250;
        boolean isLeeg = wachtwoord.trim().length() == 0;

        while (teLang || isLeeg) {
            if (teLang) {
                System.out.println("Wachtwoord te lang! Probeer het nog eens, of typ 'exit'");
                System.out.println("Uw wachtwoord: ");
            } else {
                System.out.println("Wachtwoord mag niet leeg zijn! Probeer het nog eens, of typ 'exit'");
                System.out.println("Uw wachtwoord: ");
            }
            wachtwoord = c.vraagInput();
            if (wachtwoord.equals("exit")) throw new AanmaakGebruikerAfgebrokenException();

            teLang = wachtwoord.length() > 250;
            isLeeg = wachtwoord.trim().length() == 0;
        }

        System.out.print("Typ nogmaals uw wachtwoord, ter verificatie: ");
        String wachtwoordV = c.vraagInput();

        if (!wachtwoord.equals(wachtwoordV)) {
            System.out.println("Wachtwoord komt niet overeen! Probeer het nog eens.");
            wachtwoordMetChecks(wachtwoord);
        }
        return wachtwoord;
    }

    void verzendwijzenKiezen(Gebruiker g) {
        System.out.println();
        System.out.println("Kies uw verzendwijzen, u kunt kiezen uit:");
        for (Bezorgwijze value : Bezorgwijze.values()) {
            System.out.println("  -" + value.toString().toLowerCase());
        }
        System.out.println();
        System.out.println("Typ uw keuze(s), gescheiden door een komma en een spatie.");
        System.out.println("Bijvoorbeeld: optie1, optie2");
        System.out.print("Uw keuzes: ");
        String keuzes = c.vraagInput();
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

    void adresToevoegen(Gebruiker g) {
        System.out.println();
        System.out.println("Omdat u voor de optie 'thuis afhalen' heeft gekozen, hebben we ook uw adres nodig:");
        System.out.print("Uw adres: ");
        String adres = c.vraagInput();
        boolean teLang = adres.length() > 255;
        boolean isLeeg = adres.trim().length() == 0;
        while (teLang || isLeeg) {
            if (teLang) {
                System.out.println("Adres te lang! Probeer het nog eens, of typ 'exit'");
                System.out.println("Uw adres: ");
            } else {
                System.out.println("Adres mag niet leeg zijn! Probeer het nog eens, of typ 'exit'");
                System.out.println("Uw adres: ");
            }
            adres = c.vraagInput();
            if (adres.equals("exit")) throw new AanmaakGebruikerAfgebrokenException();

            teLang = adres.length() > 250;
            isLeeg = adres.trim().length() == 0;
        }
        g.setAdres(adres);


    }


}
