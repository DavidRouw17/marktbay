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
import util.ConsoleWriter;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdvertentieZoekServiceTest {

    @Mock
    ConsoleReader cr;

    @Mock
    ConsoleWriter cw;

    Console cMock;

    private static final EntityManager em = Persistence.createEntityManagerFactory("H2").createEntityManager();
    private static final GebruikerDao gd = new GebruikerDao(em);
    private static final ProductCategorieDao pd = new ProductCategorieDao(em);
    private static final DienstCategorieDao dd = new DienstCategorieDao(em);
    private AdvertentieZoekService target;
    static Gebruiker david;

    static Advertentie p;
    static Advertentie d;

    @BeforeAll
    static void setUpMore() {
        pd.slaOpInDB(new ProductCategorie("Fietsen"));
        pd.slaOpInDB(new ProductCategorie("PC"));
        pd.slaOpInDB(new ProductCategorie("Java"));
        dd.slaOpInDB(new DienstCategorie("Java-Developer"));
        dd.slaOpInDB(new DienstCategorie("Schilderen"));
        dd.slaOpInDB(new DienstCategorie("Schoonmaken"));
        david = new Gebruiker("David", "David@David.nl", "David!");
        p = new Product("Fiets te koop!", 150.00, david);
        d = new Dienst("Lekker schilderen.", 150.00, david);
        gd.slaOpInDB(david);
    }


    @BeforeEach
    void setUp() {
        cMock = new Console();
        cMock.setConsoleReader(cr);
        target = new AdvertentieZoekService(david, cMock, em);
    }

    @Test
    void hoofdMenuWerktVoorProducten(){
        //given
        when(cMock.vraagInput()).thenReturn("1","Fietsen", "terug", "x");
        //when
        target.start();
        //then
        //niks, maar dat het maar niet fout gaat
    }

    @Test
    void hoofdMenuWerktVoorDiensten(){
        //given
        when(cMock.vraagInput()).thenReturn("2","Schilderen", "terug", "x");
        //when
        target.start();
        //then
        //niks, maar dat het maar niet fout gaat
    }

    @Test
    void hoofdMenuWerktVoorFouteInput(){
        //given
        when(cMock.vraagInput()).thenReturn("afalksdjf","1", "Fietsen", "terug", "x");
        //when
        target.start();
        //then
        //niks, maar dat het maar niet fout gaat
    }

    @Test
    void productenKunnenOpCategorieGevondenWorden(){
        //given
        when(cMock.vraagInput()).thenReturn("Fietsen", "terug", "x");
        //when
        target.zoekProductOpCategorie();
        //then
        //niks, maar dat het maar niet fout gaat
    }

    @Test
    void productenKunnenNietGezochtWordenAlsCategorieNietBestaat(){
        //given
        when(cMock.vraagInput()).thenReturn("Blabal","Fietsen", "terug", "x");
        //when
        target.zoekProductOpCategorie();
        //then
        //niks, maar dat het maar niet fout gaat
    }

    @Test
    void dienstenKunnenOpCategorieGevondenWorden(){
        //given
        when(cMock.vraagInput()).thenReturn("Schilderen", "terug", "x");
        //when
        target.zoekDienstOpCategorie();
        //then
        //niks, maar dat het maar niet fout gaat
    }

    @Test
    void dienstenKunnenNietGezochtWordenAlsCategorieNietBestaat(){
        //given
        when(cMock.vraagInput()).thenReturn("Blabal","Schilderen", "terug", "x");
        //when
        target.zoekDienstOpCategorie();
        //then
        //niks, maar dat het maar niet fout gaat
    }

    @Test
    void eigenAdvertentiesWordenWeergegeven(){
        //given
        when(cMock.vraagInput()).thenReturn("x");
        //when
        target.zoekEigenAdvertenties();
        //then
        //niks, maar dat het maar niet fout gaat
    }
}