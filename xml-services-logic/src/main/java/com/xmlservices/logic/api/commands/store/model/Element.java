package com.xmlservices.logic.api.commands.store.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Sergiu Indrie
 */
public class Element {

    private Integer id;
    private Integer fileId;
    private Integer nr;
    private String type;
    private String prefix;
    private String localname;
    private String data;
    private String encoding;
    private String version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getNr() {
        return nr;
    }

    public void setNr(Integer nr) {
        this.nr = nr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLocalname() {
        return localname;
    }

    public void setLocalname(String localname) {
        this.localname = localname;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Element");
        sb.append("{id=").append(id);
        sb.append(", fileId=").append(fileId);
        sb.append(", nr=").append(nr);
        sb.append(", type='").append(type).append('\'');
        sb.append(", prefix='").append(prefix).append('\'');
        sb.append(", localname='").append(localname).append('\'');
        sb.append(", data='").append(data).append('\'');
        sb.append(", encoding='").append(encoding).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
