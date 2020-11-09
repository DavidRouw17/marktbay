package domein;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Dienst extends Advertentie {

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "dienstdienstcategorie",
            joinColumns = @JoinColumn(name = "dienst"),
            inverseJoinColumns = @JoinColumn(name = "categorie"))
    private List<DienstCategorie> categorieLijst;


    public Dienst() {    }

    public Dienst(String titel, double prijs, Gebruiker g) {
        super(titel, "Dienst", prijs, g);
        categorieLijst = new ArrayList<>();
    }


    public List<DienstCategorie> getCategorieLijst() {
        return categorieLijst;
    }

    public void addCategorie(DienstCategorie p) {
        this.categorieLijst.add(p);

    }

    public String categorieConvert(){
        StringBuilder sb = new StringBuilder();
        List<DienstCategorie> lijst = getCategorieLijst();
        for (DienstCategorie d : lijst) {
            sb.append("[" + d.toString() + "]");
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
