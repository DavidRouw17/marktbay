package domein;

import enums.Bezorgwijze;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "Gebruiker.findAll", query = "SELECT g FROM Gebruiker g"),
        @NamedQuery(name = "Gebruiker.zoekOpGebruikersnaam",
                query = "SElECT g FROM Gebruiker g WHERE g.gebruikersnaam LIKE :gebruikersnaam")
})
public class Gebruiker extends GeneriekObject {

    private String gebruikersnaam;
    private String emailadres;
    private String wachtwoord;

    private String adres;

    @ElementCollection
    private List<Bezorgwijze> bezorgwijzen;

    @OneToMany (mappedBy = "eigenaarAdvertentie",
                cascade = CascadeType.ALL,
                fetch = FetchType.EAGER)
    private List<Advertentie> aangebodenAdvertenties;

    public Gebruiker() {
    }

    public Gebruiker(String gebruikersnaam, String emailadres, String wachtwoord) {
        this.gebruikersnaam = gebruikersnaam;
        this.emailadres = emailadres;
        this.wachtwoord = String.valueOf(wachtwoord.hashCode());
        bezorgwijzen =  new ArrayList<>();
        aangebodenAdvertenties = new ArrayList<>();
    }

    public void addBezorgWijze(Bezorgwijze bezorgwijze){
        if(!bezorgwijzen.contains(bezorgwijze)){
            bezorgwijzen.add(bezorgwijze);
        }
    }

    public void addAdvertentie(Advertentie a){
        this.aangebodenAdvertenties.add(a);
    }

    public List<Advertentie> getAangebodenAdvertenties(){
        return aangebodenAdvertenties;
    }

    public List<Bezorgwijze> getBezorgwijzen(){
        return bezorgwijzen;
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    public String getEmailadres() {
        return emailadres;
    }

    public void setEmailadres(String emailadres) {
        this.emailadres = emailadres;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    @Override
    public String toString() {
        return "Gebruiker{" +
                "gebruikersnaam='" + gebruikersnaam + '\'' +
                ", emailadres='" + emailadres + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gebruiker)) return false;
        Gebruiker gebruiker = (Gebruiker) o;
        return getGebruikersnaam().equals(gebruiker.getGebruikersnaam()) &&
                getEmailadres().equals(gebruiker.getEmailadres());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGebruikersnaam(), getEmailadres());
    }
}
