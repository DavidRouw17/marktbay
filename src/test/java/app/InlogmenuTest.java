package app;

import dao.GebruikerDao;
import domein.Gebruiker;
import inputbehandeling.AdvertentieZoekService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.Console;
import util.ConsoleReader;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InlogmenuTest {

    @Mock
    ConsoleReader cr;

    Console cMock;

    private static final EntityManager em = Persistence.createEntityManagerFactory("H2").createEntityManager();
    private static final GebruikerDao gd = new GebruikerDao(em);
    private Inlogmenu target;
    static Gebruiker david;

    @BeforeAll
    static void SetUp(){
        david = new Gebruiker("David", "David@David.nl", "David!");
        gd.slaOpInDB(david);
    }

    @BeforeEach
    void setUp() {
        cMock = new Console();
        cMock.setConsoleReader(cr);
        target = new Inlogmenu(em, cMock);
    }

    @Test
    void inloggenWerktHappyFlow(){
        //given
        when(cMock.vraagInput()).thenReturn("David", "David!", "x");
        //when
        target.start();
        //then
        //gaatallesgoed
    }

    @Test
    void valideerGebruikersNaamGeeftGebruikersnaamTerugBijGoedeInput(){
        //given
        String goedeInput = "David";
        //when
        Gebruiker resultaat = target.valideerGebruikersnaam(goedeInput);
        //then
        assertEquals(goedeInput, resultaat.getGebruikersnaam());
    }

    @Test
    void valideerGebruikersNaamGeeftNullTerugAlsDezeNietBestaat(){
        //given
        String Input = "Henk";
        when(cMock.vraagInput()).thenReturn("x");
        //when
        Gebruiker resultaat = target.valideerGebruikersnaam(Input);
        //then
        assertNull(resultaat);
    }

    @Test
    void wachtwoordValidatieDoetNiksAlsHetKlopt(){
        //given
        String wachtwoord = String.valueOf("David!".hashCode());
        //when
        target.valideerWachtwoord(wachtwoord, david);
        //then
        //geen errors
    }

    @Test
    void terugNaarHoofdmenuAlsWachtwoordNietKlopt(){
        //given
        String wachtwoord = String.valueOf("foutWachtwoord".hashCode());
        when(cMock.vraagInput()).thenReturn("x");
        //when
        target.valideerWachtwoord(wachtwoord, david);
        //then
        //geen errors
    }
}