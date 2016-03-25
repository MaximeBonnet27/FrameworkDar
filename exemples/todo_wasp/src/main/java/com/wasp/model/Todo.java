package com.wasp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Todo {
    private String id;
    private String content;
    private Boolean isDone;

    public Todo() {
        isDone=false;
    }

    public Todo(String id, String content, Boolean isDone) {
        this.id = id;
        this.content = content;
        this.isDone = isDone;
    }

    @XmlElement
    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    @XmlElement
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}
