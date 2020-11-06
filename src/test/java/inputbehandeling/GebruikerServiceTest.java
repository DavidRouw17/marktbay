package inputbehandeling;

import dao.GebruikerDao;
import domein.Gebruiker;
import exceptions.AanmaakGebruikerAfgebrokenException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GebruikerServiceTest {
    @Mock
    ConsoleReader cr;

    Console cMock;

    private final EntityManager em = Persistence.createEntityManagerFactory("H2").createEntityManager();
    private final GebruikerDao gd = new GebruikerDao(em);
    private GebruikerService target;
    Gebruiker david;

    @BeforeEach
    void setUp() {
        david = new Gebruiker("David", "David@David.nl", "David!");
        gd.slaOpInDB(david);
        cMock = new Console();
        cMock.setConsoleReader(cr);
        target = new GebruikerService(em, cMock);
    }

    @Test
    void testOfGebruikerAangemaaktKanWorden(){
        //given
        when(cMock.vraagInput()).thenReturn("Henk", "Henk@Gmail.com", "Henk", "Henk", "rembours", "x");
        //when
        target.nieuweGebruiker();
        //then
        assertEquals("Henk@Gmail.com", gd.zoekOpGebruikersnaam("Henk").getEmailadres());
        assertEquals("Henk", gd.zoekOpGebruikersnaam("Henk").getGebruikersnaam());
        assertEquals(String.valueOf("Henk".hashCode()), gd.zoekOpGebruikersnaam("Henk").getWachtwoord());
        assertEquals("REMBOURS", gd.zoekOpGebruikersnaam("Henk").getBezorgwijzen().get(0).toString());
    }

    @Test
    void testOfGebruikerAanmakenGecanceldKanWorden(){
        //given
        when(cMock.vraagInput()).thenReturn("David", "exit", "x");
        //when
        target.nieuweGebruiker();



    }

    @Test
    void gebruikersnaamDieNietBestaatKanGekozenWorden() {
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

        when(cMock.vraagInput()).thenReturn("exit");

        String naam = "David";
        //when+then
        assertThrows(AanmaakGebruikerAfgebrokenException.class, () -> target.gebruikersnaamMetChecks(naam));
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
    void emailDieBestaatKanNietGekozenWorden() {
        //given

        when(cMock.vraagInput()).thenReturn("Bert@gmail.com");

        String email = "David@David.nl";
        //when
        String resultaat = target.emailadresMetChecks(email);

        //then
        assertEquals("Bert@gmail.com", resultaat);
    }

    @Test
    void emailMetMeerDan250CharsKanNietGekozenWorden() {
        //given

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
    void emailZonderInhoudKanNietGekozenWorden() {
        //given

        when(cMock.vraagInput()).thenReturn("Bert@gmail.com");

        String email = "";
        //when
        String resultaat = target.emailadresMetChecks(email);

        //then
        assertEquals("Bert@gmail.com", resultaat);
    }

    @Test
    void emailMetSpatieKanNietGekozenWorden() {
        //given

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

        when(cMock.vraagInput()).thenReturn("exit");

        String mail = "David@David.nl";
        //when+then
        assertThrows(AanmaakGebruikerAfgebrokenException.class, () -> target.emailadresMetChecks(mail));
    }

    @Test
    void wachtwoordVerificatieWerktAlsHetGoedGaat() {
        //given

        when(cMock.vraagInput()).thenReturn("Wachtwoord");

        String wachtwoord = "Wachtwoord";
        //when
        String result = target.wachtwoordMetChecks(wachtwoord);

        //then
        assertEquals(wachtwoord, result);
    }

    @Test
    void wachtwoordVerificatieWerktNietAlsHetFoutGaat() {
        //given

        when(cMock.vraagInput()).thenReturn("W8chtwoord", "Wachtwoord");

        String wachtwoord = "Wachtwoord";
        //when
        String result = target.wachtwoordMetChecks(wachtwoord);

        //then
        assertEquals(wachtwoord, result);
    }

    @Test
    void wachtwoordVerificatieWerktNietAlsWachtwoordTeLangIs() {
        //given

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

    @Test
    void wachtwoordVerificatieWerktNietAlsWachtwoordLeegIs() {
        //given

        when(cMock.vraagInput()).thenReturn("Wachtwoord", "Wachtwoord");

        String wachtwoord = "";
        //when
        String result = target.wachtwoordMetChecks(wachtwoord);

        //then
        assertEquals("Wachtwoord", result);
    }

    @Test
    void gebruikerKanExitKiezenAlsWachtwoordAanmakenMisGaat() {
        //given
        when(cMock.vraagInput()).thenReturn("exit");

        String ww = "";
        //when+then
        assertThrows(AanmaakGebruikerAfgebrokenException.class, () -> target.wachtwoordMetChecks(ww));
    }

    @Test
    void verzendWijzenToevoegenGaatGoed() {
        //given
        when(cMock.vraagInput()).thenReturn("verzenden, magazijn");
        //when
        target.verzendwijzenKiezen(david);
        //then
        assertEquals(2, david.getBezorgwijzen().size());
    }

    @Test
    void verzendWijzenToevoegenGaatFoutWaardoorNiksWordtToegevoegd() {
        //given
        when(cMock.vraagInput()).thenReturn("verzenden,magazijn", "rembours");
        //when
        target.verzendwijzenKiezen(david);
        //then
        assertEquals(1, david.getBezorgwijzen().size());
    }

    @Test
    void verzendWijzenToevoegenKanNietLeegZijnEnAfhalenWerkt() {
        //given
        when(cMock.vraagInput()).thenReturn("", "afhalen", "Adres");
        //when
        target.verzendwijzenKiezen(david);
        //then
        assertEquals(1, david.getBezorgwijzen().size());
        assertEquals("Adres", david.getAdres());
    }

    @Test
    void adresToevoegenWerkt(){
        //given
        String adres = "Adres!";
        when(cMock.vraagInput()).thenReturn(adres);
        //when
        target.adresToevoegen(david);
        //
        assertEquals(adres, david.getAdres());
    }

    @Test
    void adresMagNietLeegZijn(){
        //given
        String adres = "Adres!";
        when(cMock.vraagInput()).thenReturn(" ", adres);
        //when
        target.adresToevoegen(david);
        //
        assertEquals(adres, david.getAdres());
    }

    @Test
    void adresMagNietTeLangZijn(){
        //given
        String adres = "Adres!";
        when(cMock.vraagInput()).thenReturn("Loremipsumdolorsitamet,nonummyligulavolutpathacintegernonummy." +
                "Suspendisseultricies,congueetiamtellus,eratlibero,nullaeleifend," +
                "maurispellentesque.Suspendisseintegerpraesentvel," +
                "integergravidamauris,fringillavehiculalacinianonsjdflk;ajdflkjasfkjsdf;lkjasdfadfasdfasdd@gmail.com", adres);
        //when
        target.adresToevoegen(david);
        //
        assertEquals(adres, david.getAdres());
    }

    @Test
    void gebruikerKanExitKiezenAlsAdresAanmakenMisGaat() {
        //given
        when(cMock.vraagInput()).thenReturn(" ","exit");

        //when+then
        assertThrows(AanmaakGebruikerAfgebrokenException.class, () -> target.adresToevoegen(david));
        assertEquals(null, david.getAdres());
    }
}