package app;


import inputbehandeling.GebruikerService;

import static app.GebruikerInput.gebruikerInput;

public class Hoofdmenu {

    public void start(){
        System.out.println();
        System.out.println("******************************************");
        System.out.println("Welkom bij de MarktBay inlogpagina!");
        System.out.println("Toets uw keuze!");
        System.out.println("******************************************");

        System.out.println("[1] Inloggen");
        System.out.println("[2] Registreren nieuwe gebruiker");
        System.out.println("[x] Afsluiten");
        System.out.print("Uw keuze: ");

        switch (gebruikerInput()) {
            case "1":
                System.out.println("Dit komt nog!"); break;
                //TODO Inloggen maken!
            case "2":
                new GebruikerService().nieuweGebruiker(); break;
            case "x":
                System.out.println("Tot ziens!"); break;
            default:
                System.out.println("Ongeldige keuze, probeer het nog eens!");
                new Hoofdmenu().start(); break;
        }
    }
}
