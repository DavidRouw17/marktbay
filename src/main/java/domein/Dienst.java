package domein;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
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
        p.addDienst(this);
    }
}
