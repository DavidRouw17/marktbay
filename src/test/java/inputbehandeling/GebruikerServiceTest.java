package inputbehandeling;

import app.GebruikerInput;
import app.Gebruikersmenu;
import dao.GebruikerDao;
import domein.Gebruiker;
import exceptions.AanmaakGebruikerAfgebrokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.Console;
import util.ConsoleReader;
import util.ConsoleWriter;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GebruikerServiceTest {
    @Mock
    ConsoleReader cr;

    @Mock
    ConsoleWriter cw;


    Console cMock;

    private final EntityManager em = Persistence.createEntityManagerFactory("H2").createEntityManager();
    private final GebruikerDao gd = new GebruikerDao(em);
    private GebruikerService target;


    @BeforeEach
    void setUp(){
        Gebruiker david = new Gebruiker("David", "David@David.nl", "David!");
        gd.slaOpInDB(david);
        cMock = new Console();
        target = new GebruikerService(em, cMock);
    }

    @Test
    void gebruikersnaamDieNietBestaatKanGekozenWorden(){
        //given
        String naam = "Bert";
        //when
        String resultaat = target.gebruikersnaamMetChecks(naam);
        //then
        assertEquals(naam, resultaat);
    }

    @Test
    void gebruikersnaamDieBestaatKanNietGekozenWorden() {
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("Bert");

        String naam = "David";
        //when
        String resultaat = target.gebruikersnaamMetChecks(naam);

        //then
        assertEquals("Bert", resultaat);
    }

    @Test
    void gebruikersnaamDieTeLangIsKanNietGekozenWorden() {
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("Bert");

        String naam = "Ditzijnmeerdan20tekensendatmagniet!";
        System.out.println(gd.gebruikersNamenLijst());
        //when
        String resultaat = target.gebruikersnaamMetChecks(naam);

        //then
        assertEquals("Bert", resultaat);
    }

    @Test
    void gebruikersnaamZonderTekensKanNietGekozenWorden() {
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("Bert");

        String naam = "";
        System.out.println(gd.gebruikersNamenLijst());
        //when
        String resultaat = target.gebruikersnaamMetChecks(naam);

        //then
        assertEquals("Bert", resultaat);
    }

    @Test
    void gebruikerKanExitKiezenAlsAccountAlBestaat() {
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("exit");

        String naam = "David";
        System.out.println(gd.gebruikersNamenLijst());
        //when+then
        assertThrows(AanmaakGebruikerAfgebrokenException.class, ()-> {
            target.gebruikersnaamMetChecks(naam);
        });
    }

    @Test
    void emailDieNietBestaatKanGekozenWorden() {
        //given
        String email = "Ben@gmail.com";

        //when
        String resultaat = target.emailadresMetChecks(email);

        //then
        assertEquals(email, resultaat);
    }

    @Test
    void emailDieBestaatKanNietGekozenWorden(){
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("Bert@gmail.com");

        String email = "David@David.nl";
        //when
        String resultaat = target.emailadresMetChecks(email);

        //then
        assertEquals("Bert@gmail.com", resultaat);
    }

    @Test
    void emailMetMeerDan250CharsKanNietGekozenWorden(){
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("Bert@gmail.com");

        String email = "Loremipsumdolorsitamet,nonummyligulavolutpathacintegernonummy." +
                "Suspendisseultricies,congueetiamtellus,eratlibero,nullaeleifend," +
                "maurispellentesque.Suspendisseintegerpraesentvel," +
                "integergravidamauris,fringillavehiculalacinianonsjdflk;ajdflkjasfd@gmail.com";
        //when
        String resultaat = target.emailadresMetChecks(email);

        //then
        assertEquals("Bert@gmail.com", resultaat);
    }

    @Test
    void emailZonderInhoudKanNietGekozenWorden(){
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("Bert@gmail.com");

        String email = "";
        //when
        String resultaat = target.emailadresMetChecks(email);

        //then
        assertEquals("Bert@gmail.com", resultaat);
    }

    @Test
    void emailMetSpatieKanNietGekozenWorden(){
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("Bert@gmail.com");

        String email = "Bert @gmail.com";
        //when
        String resultaat = target.emailadresMetChecks(email);

        //then
        assertEquals("Bert@gmail.com", resultaat);
    }

    @Test
    void gebruikerKanExitKiezenAlsEmailAlBestaat() {
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("exit");

        String mail = "David@David.nl";
        System.out.println(gd.gebruikersNamenLijst());
        //when+then
        assertThrows(AanmaakGebruikerAfgebrokenException.class, ()-> {
            target.emailadresMetChecks(mail);
        });
    }

    @Test
    void WachtwoordVerificatieWerktAlsHetGoedGaat(){
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("Wachtwoord");

        String wachtwoord = "Wachtwoord";
        //when
        String result = target.wachtwoordMetChecks(wachtwoord);

        //then
        assertEquals(wachtwoord, result);
    }

    @Test
    void WachtwoordVerificatieWerktNietAlsHetFoutGaat(){
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("W8chtwoord", "Wachtwoord");

        String wachtwoord = "Wachtwoord";
        //when
        String result = target.wachtwoordMetChecks(wachtwoord);

        //then
        assertEquals(wachtwoord, result);
    }

    @Test
    void WachtwoordVerificatieWerktNietWachtwoordTeLangIs() {
        //given
        cMock.setConsoleReader(cr);
        when(cMock.vraagInput()).thenReturn("Wachtwoord", "Wachtwoord");

        String wachtwoord = "Loremipsumdolorsitamet,nonummyligulavolutpathacintegernonummy." +
                "Suspendisseultricies,congueetiamtellus,eratlibero,nullaeleifend," +
                "maurispellentesque.Suspendisseintegerpraesentvel," +
                "integergravidamauris,fringillavehiculalacinianonsjdflk;ajdflkjasfd@gmail.com";
        //when
        String result = target.wachtwoordMetChecks(wachtwoord);

        //then
        assertEquals("Wachtwoord", result);
    }
}