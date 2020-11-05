package domein;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@NamedQuery(name = "Dienst.getByCategorie", query = "SELECT d " +
//        "FROM Dienst d " +
//        "JOIN d.categorieLijst AS dc " +
//        "JOIN "
//        "WHERE dc.categorie LIKE :Categorie")
public class Dienst extends Advertentie {

    @ManyToMany(cascade = CascadeType.ALL)
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
                "***************";
    }
}
