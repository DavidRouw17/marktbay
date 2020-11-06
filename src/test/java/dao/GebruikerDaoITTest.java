package dao;

import domein.Gebruiker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

//inclusief tests voor generieke dao

class GebruikerDaoITTest {

    private final EntityManager em = Persistence.createEntityManagerFactory("H2").createEntityManager();
    private final GebruikerDao target = new GebruikerDao(em);

    @BeforeEach
    void setUp() {
        Gebruiker david = new Gebruiker("David", "David@David.nl", "David!");
        target.slaOpInDB(david);
    }

    @Test
    void wanneerErEenGebruikerWordtAangemaaktEnOpgeslagenKomtDezeInDeDB(){
        Gebruiker henk = new Gebruiker("Henk", "henk", "henk");
        target.slaOpInDB(henk);
        assertEquals(2, target.dbSize());
    }

    @Test
    void zoekenOpGebruikersnaamGeeftGoedeGebruikerTerug(){
        Gebruiker g = target.zoekOpGebruikersnaam("David");
        assertEquals("David@David.nl", g.getEmailadres());
    }

    @Test
    void verwijderenVanGebruikerWerkt(){
        target.remove(target.zoekOpGebruikersnaam("David"));
        assertEquals(0, target.dbSize());
    }

    @Test
    void verkrijgenGebruikerOpUniekeIDWerkt(){
        Gebruiker g = target.get(1L);
        assertEquals("David@David.nl", g.getEmailadres());
    }

    @Test
    void updatenObjectWerkt(){
        Gebruiker g = target.get(1L);
        g.setAdres("LangeLindeLaan");
        target.updateAndDetach(g);
        Gebruiker B = target.get(1L);
        assertEquals("LangeLindeLaan", B.getAdres());
    }
}