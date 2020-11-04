package dao;

import domein.Gebruiker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GebruikerDaoTest {

    @Mock
    private EntityManager emMock;

    @InjectMocks
    private GebruikerDao target;

    @Mock
    private TypedQuery<Gebruiker> tqMock;


    @Test
    void zoekOpGebruikersnaam() {
        //given
        String gebruikersnaam = "naam";
        when(emMock.createNamedQuery(anyString(), eq(Gebruiker.class))).thenReturn(tqMock);
        //when
        target.zoekOpGebruikersnaam(gebruikersnaam);
        //
        verify(emMock).createNamedQuery(eq("Gebruiker.zoekOpGebruikersnaam"), eq(Gebruiker.class));
        verify(tqMock).setParameter(eq("gebruikersnaam"), eq(gebruikersnaam));
        verify(tqMock).getSingleResult();
    }

    @Test
    void gebruikersNamenLijst() {
        //given
        when(emMock.createQuery(anyString(), eq(Gebruiker.class))).thenReturn(tqMock);
        //when
        target.gebruikersNamenLijst();
        //
        verify(emMock).createQuery(anyString(), eq(Gebruiker.class));
        verify(tqMock).getResultList();
    }

    @Test
    void emailadresLijst() {
        //given
        when(emMock.createQuery(anyString(), eq(Gebruiker.class))).thenReturn(tqMock);
        //when
        target.emailadresLijst();
        //
        verify(emMock).createQuery(anyString(), eq(Gebruiker.class));
        verify(tqMock).getResultList();
    }
}