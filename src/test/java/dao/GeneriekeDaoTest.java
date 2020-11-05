package dao;

import domein.Gebruiker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Mocks op code van de GebruikerDao, maar alleen op functies van GeneriekeDao.
 * Makkelijkste manier om die te mocken, niet gelukt op de abstracte generieke klasse.
 */


@ExtendWith(MockitoExtension.class)
class GeneriekeDaoTest {

    @Mock
    private EntityManager emMock;

    @InjectMocks
    private GebruikerDao target;


    @Mock
    private EntityTransaction etMock;

    @Mock
    private Gebruiker gebruikerMock;

    @Mock
    private TypedQuery<Gebruiker> tqMock;

    @Test
    void get() {
        //given
        long id = 15L;
        when(emMock.find(eq(Gebruiker.class), eq(id))).thenReturn(gebruikerMock);
        //when
        Gebruiker g = target.get(id);
        //then
        assertEquals(gebruikerMock, g);
        verify(emMock).find(eq(Gebruiker.class), eq(id));
    }

    @Test
    void getDetached() {
        //given
        long id = 15L;
        when(emMock.find(eq(Gebruiker.class), eq(id))).thenReturn(gebruikerMock);
        //when
        Gebruiker g = target.getDetached(id);
        //then
        assertEquals(gebruikerMock, g);
        verify(emMock).find(eq(Gebruiker.class), eq(id));
        verify(emMock).detach(eq(gebruikerMock));
    }

    @Test
    void saveAndDetach() {
        //given
        when(emMock.getTransaction()).thenReturn(etMock);
        //when
        target.slaOpInDB(gebruikerMock);
        //then
        verify(etMock).begin();
        verify(emMock).persist(eq(gebruikerMock));
        verify(emMock).flush();
        verify(emMock).clear();
        verify(etMock).commit();
    }

    @Test
    void updateAndDetach() {
        //given
        when(emMock.getTransaction()).thenReturn(etMock);
        //when
        target.updateAndDetach(gebruikerMock);
        //then
        verify(etMock).begin();
        verify(emMock).merge(eq(gebruikerMock));
        verify(emMock).flush();
        verify(emMock).clear();
        verify(etMock).commit();

    }

    @Test
    void remove() {
        //given
        when(emMock.getTransaction()).thenReturn(etMock);
        //when
        target.remove(gebruikerMock);
        //then
        verify(etMock).begin();
        verify(emMock).remove(eq(gebruikerMock));
        verify(etMock).commit();
    }

    @Test
    void isManaged() {
        //when
        target.isManaged(gebruikerMock);
        //then
        verify(emMock).contains(gebruikerMock);

    }

    @Test
    void findAllWithNamedQuery(){
        //given
        when(emMock.createNamedQuery(anyString(), eq(Gebruiker.class))).thenReturn(tqMock);
        //when
        target.findAllWithNamedQuery();
        //
        verify(emMock).createNamedQuery(anyString(), eq(Gebruiker.class));
        verify(tqMock).getResultList();
    }

}