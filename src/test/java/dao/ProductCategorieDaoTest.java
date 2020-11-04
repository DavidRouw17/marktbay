package dao;

import domein.DienstCategorie;
import domein.ProductCategorie;
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
class ProductCategorieDaoTest {

    @Mock
    private EntityManager emMock;

    @InjectMocks
    private ProductCategorieDao target;

    @Mock
    private TypedQuery<ProductCategorie> tqMock;

    @Test
    void zoekOpNaam() {
        //given
        String categorieNaam = "naam";
        when(emMock.createNamedQuery(anyString(), eq(ProductCategorie.class))).thenReturn(tqMock);
        //when
        target.zoekOpNaam(categorieNaam);
        //
        verify(emMock).createNamedQuery(eq("ProductCategorie.findByName"), eq(ProductCategorie.class));
        verify(tqMock).setParameter(eq("naam"), eq(categorieNaam));
        verify(tqMock).getSingleResult();
    }
}