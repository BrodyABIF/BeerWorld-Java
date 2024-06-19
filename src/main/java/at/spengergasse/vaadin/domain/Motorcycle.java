package at.spengergasse.vaadin.domain;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)


public class Motorcycle extends AbstractEntity {
    private String bezeichnung;
//    private Date baujahr;
    private int hubraum;
    private int leistung;
    private int drehmoment;
    private int preis;

    public Motorcycle() {}


    public Motorcycle(String bez, int hub, int leist, int dreh, int price) {
        this.bezeichnung = bez;
//        this.baujahr = bau;
        this.hubraum = hub;
        this.leistung = leist;
        this.drehmoment = dreh;
        this.preis = price;


    }
}
