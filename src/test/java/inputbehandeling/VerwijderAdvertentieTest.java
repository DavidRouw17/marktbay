package inputbehandeling;

import dao.DienstCategorieDao;
import dao.GebruikerDao;
import dao.ProductCategorieDao;
import domein.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerwijderAdvertentieTest {

    @Mock
    ConsoleReader cr;

    Console cMock;

    private static final EntityManager em = Persistence.createEntityManagerFactory("H2").createEntityManager();
    private static final GebruikerDao gd = new GebruikerDao(em);
    private static final ProductCategorieDao pd = new ProductCategorieDao(em);
    private static final DienstCategorieDao dd = new DienstCategorieDao(em);
    private AdvertentieService target;
    private AdvertentieService target2;
    static Gebruiker david;
    static Gebruiker henk;

    Advertentie p;
    Advertentie d;

    @BeforeAll
    static void setUpMore() {
        pd.slaOpInDB(new ProductCategorie("Fietsen"));
        pd.slaOpInDB(new ProductCategorie("PC"));
        pd.slaOpInDB(new ProductCategorie("Java"));
        dd.slaOpInDB(new DienstCategorie("Java-Developer"));
        dd.slaOpInDB(new DienstCategorie("Schilderen"));
        dd.slaOpInDB(new DienstCategorie("Schoonmaken"));

    }

    @BeforeEach
    void setUp() {
        cMock = new Console();
        cMock.setConsoleReader(cr);

        david = new Gebruiker("David", "David@David.nl", "David!");
        henk = new Gebruiker("henk", "David@David.nl", "David!");
        p = new Product("Fiets te koop!", 150.00, david);
        d = new Dienst("Lekker schilderen.", 150.00, david);
        gd.slaOpInDB(david);
        gd.slaOpInDB(henk);
        target = new AdvertentieService(david, cMock, em);
        target2 = new AdvertentieService(henk, cMock, em);
    }

    @Test
    void happyFlow() {
        //given
        when(cMock.vraagInput()).thenReturn("1", "x");
        //when
        target.verwijderAdvertentie();
        //then
        Gebruiker g = gd.zoekOpGebruikersnaam("David");
        assertEquals(1, g.getAangebodenAdvertenties().size());
    }

    @Test
    void terugNaarMenuWanneerGebruikerGeenAdvertentiesHeeft() {
        //given
        when(cMock.vraagInput()).thenReturn("x");
        //when
        target2.verwijderAdvertentie();
        //then
        //verder geen input vereist
    }

    @Test
    void terugNaarMenuWanneerInputGeenGetalIs() {
        //given
        when(cMock.vraagInput()).thenReturn("ad", "x");
        //when
        target.verwijderAdvertentie();
        //then
        //verder geen input vereist
    }

    @Test
    void terugNaarMenuWanneerInputBuitenRangeLigt() {
        //given
        when(cMock.vraagInput()).thenReturn("3", "x");
        //when
        target.verwijderAdvertentie();
        //then
        //verder geen input vereist
    }
}