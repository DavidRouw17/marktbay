package inputbehandeling;

import dao.DienstCategorieDao;
import dao.GebruikerDao;
import dao.ProductCategorieDao;
import domein.DienstCategorie;
import domein.Gebruiker;
import domein.ProductCategorie;
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

@ExtendWith(MockitoExtension.class)
class AdvertentieServiceTest {

    @Mock
    ConsoleReader cr;

    Console cMock;

    private static final EntityManager em = Persistence.createEntityManagerFactory("H2").createEntityManager();
    private static final GebruikerDao gd = new GebruikerDao(em);
    private static final ProductCategorieDao pd = new ProductCategorieDao(em);
    private static final DienstCategorieDao dd = new DienstCategorieDao(em);
    private AdvertentieService target;
    static Gebruiker david;

    @BeforeAll
    static void setUpMore(){
        pd.slaOpInDB(new ProductCategorie("Fietsen"));
        pd.slaOpInDB(new ProductCategorie("PC"));
        pd.slaOpInDB(new ProductCategorie("Java"));
        dd.slaOpInDB(new DienstCategorie("Java-Developer"));
        dd.slaOpInDB(new DienstCategorie("Schilderen"));
        dd.slaOpInDB(new DienstCategorie("Schoonmaken"));
        david = new Gebruiker("David", "David@David.nl", "David!");
        gd.slaOpInDB(david);
    }


    @BeforeEach
    void setUp() {

        cMock = new Console();
        cMock.setConsoleReader(cr);
        target = new AdvertentieService(david, cMock, em);

    }

    @Test
    void werktSetUp(){
        System.out.println(pd.findAll().size());
    }

}