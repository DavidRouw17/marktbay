package domein;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product extends Advertentie {

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "productproductcategorie",
            joinColumns = @JoinColumn(name = "product"),
            inverseJoinColumns = @JoinColumn(name = "categorie"))
    private List<ProductCategorie> categorieLijst;

    public Product() {

    }

    public Product(String titel, double prijs, Gebruiker g) {
        super(titel, "Product", prijs, g);
        categorieLijst = new ArrayList<>();
    }


    public List<ProductCategorie> getCategorieLijst() {
        return categorieLijst;
    }

    public void addCategorie(ProductCategorie p) {
        this.categorieLijst.add(p);
    }

    public String categorieConvert(){
        StringBuilder sb = new StringBuilder();
        List<ProductCategorie> lijst = getCategorieLijst();
        for (ProductCategorie p : lijst) {
            sb.append("[" + p.toString() + "]");
            sb.append(" ");
        }
        return sb.toString();
    }

    public String toString(){
        return "***************" + "\n" +
                "Titel: " + getTitel() + "\n" +
                "Categorie: " + categorieConvert() + "\n" +
                "Prijs: " + getPrijs() + "\n" +
                "Aanbieder: " + getEigenaarNaam() + "\n" +
                        "***************";
    }

}
