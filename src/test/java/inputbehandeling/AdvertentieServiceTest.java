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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
        david = new Gebruiker("David", "David@David.nl", "David!");
        gd.slaOpInDB(david);
    }


    @BeforeEach
    void setUp() {


        cMock = new Console();
        cMock.setConsoleReader(cr);
        target = new AdvertentieService(david, cMock, em);
        p = new Product("Fiets te koop!", 150.00, david);
        d = new Dienst("Lekker schilderen.", 150.00, david);

    }

    @Test
    void testDatNieuwProductAangemaaktKanWorden(){
        //given
        String titel = "titel";
        String prijs = "1400";
        String categorie = "PC";
        String beschrijving = "beschrijving";
        String bijlage = "bijlage";
        when(cMock.vraagInput()).thenReturn("1", titel, prijs, categorie, "1", beschrijving, "1", bijlage, "x");
        //when
        target.nieuweAdvertentie();
        //then
        Gebruiker heteDavid = gd.zoekOpGebruikersnaam("David");
        Advertentie a = heteDavid.getAangebodenAdvertenties().get(0);
        assertEquals(titel, a.getTitel());
        assertEquals(Double.parseDouble(prijs), a.getPrijs());
        assertEquals(beschrijving, a.getOmschrijving());
        assertEquals(bijlage, a.getBijlage());
    }

    @Test
    void testDatNieuweDienstAangemaaktKanWorden(){
        //given
        String titel = "titel";
        String prijs = "1400";
        String categorie = "Schoonmaken";
        String beschrijving = "beschrijving";
        String bijlage = "bijlage";
        when(cMock.vraagInput()).thenReturn("2", titel, prijs, categorie, "1", beschrijving, "1", bijlage, "x");
        //when
        target.nieuweAdvertentie();
        //then
        Gebruiker heteDavid = gd.zoekOpGebruikersnaam("David");
        assertEquals(1, heteDavid.getAangebodenAdvertenties().size()); //1, in beforeAll wordt David niet geupdate, dus dit enige advertentie in db
        Advertentie a = heteDavid.getAangebodenAdvertenties().get(0);
        assertEquals(titel, a.getTitel());
        assertEquals(Double.parseDouble(prijs), a.getPrijs());
        assertEquals(beschrijving, a.getOmschrijving());
        assertEquals(bijlage, a.getBijlage());
    }

    @Test
    void testDatOnbekendeInputNietWerktHoofdmenu(){
        //given
        String titel = "titel";
        String prijs = "1400";
        String categorie = "Schoonmaken";
        String beschrijving = "beschrijving";
        String bijlage = "bijlage";
        when(cMock.vraagInput()).thenReturn("blaaa", "2", titel, prijs, categorie, "1", beschrijving, "1", bijlage, "x");
        //when
        target.nieuweAdvertentie();
        //then
        Gebruiker heteDavid = gd.zoekOpGebruikersnaam("David");
        Advertentie a = heteDavid.getAangebodenAdvertenties().get(0);
        assertEquals(titel, a.getTitel());
        assertEquals(Double.parseDouble(prijs), a.getPrijs());
        assertEquals(beschrijving, a.getOmschrijving());
        assertEquals(bijlage, a.getBijlage());
    }

    @Test
    void testDatJeTerugKanHoofdmenu(){
        //given
        when(cMock.vraagInput()).thenReturn("terug", "x");
        //when
        target.nieuweAdvertentie();
    }

    @Test
    void testDatAdvertentieGoedAfgemaaktKanWorden(){
        //given
        String beschrijving = "beschrijving";
        String bijlage = "bijlage";
        when(cMock.vraagInput()).thenReturn("1", beschrijving, "1", bijlage, "x");
        //when
        target.advertentieAfmaken(p);
        //then
        assertEquals(beschrijving, p.getOmschrijving());
        assertEquals(bijlage, p.getBijlage());
    }

    @Test
    void testDatTitelGoedBepaaldKanWorden() {
        //given
        String goede_titel = "Goede titel";
        when(cMock.vraagInput()).thenReturn(goede_titel);
        //when
        String titel = target.verkrijgTitel();
        //then
        assertEquals(goede_titel, titel);
    }

    @Test
    void testDatTitelNietNiksMagZijn() {
        //given
        String goede_titel = "Goede titel";
        String foute_titel = " ";
        when(cMock.vraagInput()).thenReturn(foute_titel, goede_titel);
        //when
        String titel = target.verkrijgTitel();
        //then
        assertEquals(goede_titel, titel);
    }

    @Test
    void testDatTitelNietTeLangMagZijn() {
        //given
        String goede_titel = "Goede titel";
        String foute_titel = "Loremipsumdolorsitamet,nonummyligulavolutpathacintegernonummy." +
                "Suspendisseultricies,congueetiamtellus,eratlibero,nullaeleifend," +
                "maurispellentesque.Suspendisseintegerpraesentvel," +
                "integergravidamauris,fringillavehiculalacinianonsjdflk;ajdflkjasfadfasdfafdasdfd@gmail.com";
        when(cMock.vraagInput()).thenReturn(foute_titel, goede_titel);
        //when
        String titel = target.verkrijgTitel();
        //then
        assertEquals(goede_titel, titel);
    }

    @Test
    void testDatPrijsBepaaldKanWorden() {
        //given
        String goede_prijs = "15.00";
        when(cMock.vraagInput()).thenReturn(goede_prijs);
        //when
        double prijs = target.verkrijgPrijs();
        //then
        assertEquals(Double.parseDouble(goede_prijs), prijs);
    }

    @Test
    void testDatPrijsNietLeegMagZijn() {
        //given
        String goede_prijs = "15.00";
        String foute_prijs = " ";
        when(cMock.vraagInput()).thenReturn(foute_prijs, goede_prijs);
        //when
        double prijs = target.verkrijgPrijs();
        //then
        assertEquals(Double.parseDouble(goede_prijs), prijs);
    }

    @Test
    void testDatPrijsNietNiksMagZijn() {
        //given
        String goede_prijs = "15.00";
        String foute_prijs = "";
        when(cMock.vraagInput()).thenReturn(foute_prijs, goede_prijs);
        //when
        double prijs = target.verkrijgPrijs();
        //then
        assertEquals(Double.parseDouble(goede_prijs), prijs);
    }

    @Test
    void testDatPrijsGoedIngevoerdMoetWorden() {
        //given
        String goede_prijs = "15.00";
        String foute_prijs = "15sf";
        when(cMock.vraagInput()).thenReturn(foute_prijs, goede_prijs);
        //when
        double prijs = target.verkrijgPrijs();
        //then
        assertEquals(Double.parseDouble(goede_prijs), prijs);
    }

    @Test
    void testDatCategorieToegevoegdKanWordenAanProduct(){
        //given
        String goede_input = "Fietsen, Java";
        when(cMock.vraagInput()).thenReturn(goede_input);
        //when
        target.voegProductCategorieToe(p);
        //then
        assertEquals(2, ((Product)p).getCategorieLijst().size());
    }

    @Test
    void testDatNietBestaandeProductCategorieenNietToegevoegdKunnenWorden(){
        //given
        String goede_input = "Fietsen, Java, Geitjes";
        when(cMock.vraagInput()).thenReturn(goede_input);
        //when
        target.voegProductCategorieToe(p);
        //then
        assertEquals(2, ((Product)p).getCategorieLijst().size());
    }

    @Test
    void testDatEenProductEenCategorieMoetHebben(){
        //given
        String goede_input = "Fietsen, Java";
        String foute_input = "Geitjes";
        String foute_input2 = "Eitjes";
        when(cMock.vraagInput()).thenReturn(foute_input, foute_input2, goede_input);
        //when
        target.voegProductCategorieToe(p);
        //then
        assertEquals(2, ((Product)p).getCategorieLijst().size());
    }

    @Test
    void testDatCategorieToegevoegdKanWordenAanDienst(){
        //given
        String goede_input = "Java-Developer, Schilderen";
        when(cMock.vraagInput()).thenReturn(goede_input);
        //when
        target.voegDienstCategorieToe(d);
        //then
        assertEquals(2, ((Dienst)d).getCategorieLijst().size());
    }

    @Test
    void testDatNietBestaandeDienstCategorieenNietToegevoegdKunnenWorden(){
        //given
        String goede_input = "Java-Developer, Schilderen, Poolen";
        when(cMock.vraagInput()).thenReturn(goede_input);
        //when
        target.voegDienstCategorieToe(d);
        //then
        assertEquals(2, ((Dienst)d).getCategorieLijst().size());
    }

    @Test
    void testDatEenDienstEenCategorieMoetHebben(){
        //given
        String goede_input = "Java-Developer, Schilderen";
        String foute_input = "Poolen";
        String foute_input2 = "Bowlen";
        when(cMock.vraagInput()).thenReturn(foute_input, foute_input2, goede_input);
        //when
        target.voegDienstCategorieToe(d);
        //then
        assertEquals(2, ((Dienst)d).getCategorieLijst().size());
    }

    @Test
    void testDatEenBeschrijvingAanAdvertentieToegevoegdKanWorden(){
        //given
        String goede_input = "Mooie fiets, goed spul";
        when(cMock.vraagInput()).thenReturn(goede_input);
        //when
        target.voegBeschrijvingToe(p);
        //then
        assertEquals(goede_input, p.getOmschrijving());
    }

    @Test
    void testDatEenBeschrijvingNietLeegMagZijn(){
        //given
        String goede_input = "Mooie fiets, goed spul";
        String foute_input = " ";
        when(cMock.vraagInput()).thenReturn(foute_input, goede_input);
        //when
        target.voegBeschrijvingToe(p);
        //then
        assertEquals(goede_input, p.getOmschrijving());
    }

    @Test
    void testDatEenBeschrijvingNietTeLangMagZijn(){
        //given
        String goede_input = "Mooie fiets, goed spul";
        String foute_input = "Loremipsumdolorsitamet,nonummyligulavolutpathacintegernonummy." +
                "Suspendisseultricies,congueetiamtellus,eratlibero,nullaeleifend," +
                "maurispellentesque.Suspendisseintegerpraesentvel," +
                "integergravidamauris,fringillavehiculalacinianonsjdflk;ajdflkjasfadfasdfafdasdfd@gmail.com";
        when(cMock.vraagInput()).thenReturn(foute_input, goede_input);
        //when
        target.voegBeschrijvingToe(p);
        //then
        assertEquals(goede_input, p.getOmschrijving());
    }

    @Test
    void testDatEenBijlageAanAdvertentieToegevoegdKanWorden(){
        //given
        String goede_input = "Mooie fiets, goed spul";
        when(cMock.vraagInput()).thenReturn(goede_input);
        //when
        target.voegBijlageToe(p);
        //then
        assertEquals(goede_input, p.getBijlage());
    }

    @Test
    void testDatEenBijlageNietLeegMagZijn(){
        //given
        String goede_input = "Mooie fiets, goed spul";
        String foute_input = " ";
        when(cMock.vraagInput()).thenReturn(foute_input, goede_input);
        //when
        target.voegBijlageToe(p);
        //then
        assertEquals(goede_input, p.getBijlage());
    }

    @Test
    void testDatEenBijlageNietTeLangMagZijn(){
        //given
        String goede_input = "Mooie fiets, goed spul";
        String foute_input = "Loremipsumdolorsitamet,nonummyligulavolutpathacintegernonummy." +
                "Suspendisseultricies,congueetiamtellus,eratlibero,nullaeleifend," +
                "maurispellentesque.Suspendisseintegerpraesentvel," +
                "integergravidamauris,fringillavehiculalacinianonsjdflk;ajdflkjasfadfasdfafdasdfd@gmail.com";
        when(cMock.vraagInput()).thenReturn(foute_input, goede_input);
        //when
        target.voegBijlageToe(p);
        //then
        assertEquals(goede_input, p.getBijlage());
    }

}