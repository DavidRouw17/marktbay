package dao;

import domein.DienstCategorie;
import domein.Gebruiker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DienstCategorieDaoTest {

    @Mock
    private EntityManager emMock;

    @InjectMocks
    private DienstCategorieDao target;

    @Mock
    private TypedQuery<DienstCategorie> tqMock;



    @Test
    void zoekOpNaam() {
        //given
        String categorieNaam = "naam";
        when(emMock.createNamedQuery(anyString(), eq(DienstCategorie.class))).thenReturn(tqMock);
        //when
        target.zoekOpNaam(categorieNaam);
        //
        verify(emMock).createNamedQuery(eq("DienstCategorie.findByName"), eq(DienstCategorie.class));
        verify(tqMock).setParameter(eq("naam"), eq(categorieNaam));
        verify(tqMock).getSingleResult();
    }
}