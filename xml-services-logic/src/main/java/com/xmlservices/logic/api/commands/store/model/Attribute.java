package com.xmlservices.logic.api.commands.store.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public class Attribute {

    private Integer id;
    private Integer elementId;
    private Integer nr;
    private String name;
    private String value;

    public Attribute(Integer id, Integer elementId, Integer nr, String name, String value) {
        this.id = id;
        this.elementId = elementId;
        this.nr = nr;
        this.name = name;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getElementId() {
        return elementId;
    }

    public void setElementId(Integer elementId) {
        this.elementId = elementId;
    }

    public Integer getNr() {
        return nr;
    }

    public void setNr(Integer nr) {
        this.nr = nr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Attribute");
        sb.append("{id=").append(id);
        sb.append(", elementId=").append(elementId);
        sb.append(", nr=").append(nr);
        sb.append(", name='").append(name).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
