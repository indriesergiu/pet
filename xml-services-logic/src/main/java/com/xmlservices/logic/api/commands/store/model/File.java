package com.xmlservices.logic.api.commands.store.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public class File {

    private Integer id;
    private String name;

    public File(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("File");
        sb.append("{id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
