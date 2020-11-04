package domein;

import javax.persistence.*;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "DienstCategorie.findAll", query = "SELECT d FROM DienstCategorie d"),
        @NamedQuery(name = "DienstCategorie.findByName", query = "SELECT d FROM DienstCategorie d WHERE d.categorie LIKE :naam")
})
public class DienstCategorie extends GeneriekObject {

        @Column(nullable = false)
        private String categorie;

    @ManyToMany
    @JoinTable(name = "dienstdienstcategorie",
            joinColumns = @JoinColumn(name = "categorie"),
            inverseJoinColumns = @JoinColumn(name = "dienst"))
    private List<Dienst> advList;

        public DienstCategorie() {
        }

        public DienstCategorie(String categorie) {
            this.categorie = categorie;
        }

        public String getCategorie() {
            return categorie;
        }

        public void addDienst(Dienst d){
            advList.add(d);
        }

    }


