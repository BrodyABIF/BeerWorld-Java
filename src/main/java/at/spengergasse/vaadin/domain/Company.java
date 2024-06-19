package at.spengergasse.vaadin.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Company extends AbstractEntity {



    private String name;

    public Company() {}

    public Company(String name) {
        this.name = name;
    }
}
