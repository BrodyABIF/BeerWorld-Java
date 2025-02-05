package at.spengergasse.vaadin.domain;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Company extends AbstractEntity {

    private String name;
    private String Ort;
    private int plz;

    public Company() {}
}
