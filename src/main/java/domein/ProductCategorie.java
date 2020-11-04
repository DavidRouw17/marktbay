package domein;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(name = "ProductCategorie.findAll", query = "SELECT p FROM ProductCategorie p"),
    @NamedQuery(name = "ProductCategorie.findByName", query = "SELECT p FROM ProductCategorie p WHERE p.categorie LIKE :naam")
        })
public class ProductCategorie extends GeneriekObject {

    @Column(nullable = false)
    private String categorie;

    @ManyToMany
    @JoinTable(name = "productproductcategorie",
            joinColumns = @JoinColumn(name = "categorie"),
            inverseJoinColumns = @JoinColumn(name = "product"))
    private List<Product> advList = new ArrayList<>();

    public ProductCategorie() {
    }

    public ProductCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void addProduct(Product p){
        advList.add(p);
    }

    public String getCategorie() {
        return categorie;
    }

}
