package at.spengergasse.vaadin.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)


public class Beer extends AbstractEntity {
    private String sorte;
    private int stammwuerze;
    private double alkoholanteil;
    private int braujahr;
    private double preis;
    private Long showId;

    public Beer() {}

//    public Beer(String sorte, int wuerze, double alkGehalt, int bJahr, double preis, Long showId) {
//        this.sorte = sorte;
//        this.stammwuerze = wuerze;
//        this.alkoholanteil = alkGehalt;
//        this.braujahr = bJahr;
//        this.preis = preis;
//        this.showId = showId;
//    }
}
