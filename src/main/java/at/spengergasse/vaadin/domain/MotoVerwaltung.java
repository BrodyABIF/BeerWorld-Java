package at.spengergasse.vaadin.domain;

public class MotoVerwaltung extends AbstractModel{

    private String name;

    public MotoVerwaltung(Long id, String name) {
        super(id);
        this.name = name;
    }
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

}
