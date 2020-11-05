package dao;

import domein.Advertentie;
import domein.Gebruiker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdvertentieDaoTest {

    @Mock
    private EntityManager emMock;

    @InjectMocks
    private AdvertentieDao target;


    @Mock
    private TypedQuery<Advertentie> tqMock;

    @Test
    void zoekOpSoort() {
        //given
        String naam = "Test";
        when(emMock.createNamedQuery(anyString(), eq(Advertentie.class))).thenReturn(tqMock);
        //when
        target.zoekOpSoort(naam);
        //then
        verify(emMock).createNamedQuery(anyString(), eq(Advertentie.class));
        verify(tqMock).setParameter(eq("soort"), eq(naam));
        verify(tqMock).getResultList();

    }
}