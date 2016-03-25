package com.wasp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Profil {
    private String pseudo;
    private String password;
    private TodoList todoList;

    public Profil() {
        this.todoList=new TodoList();
    }

    public Profil(String pseudo, String password, TodoList todoList) {
        this.pseudo = pseudo;
        this.password = password;
        this.todoList = todoList;
    }

    @XmlElement
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    @XmlElement
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlElement
    public TodoList getTodoList() {
        if(this.todoList==null)
            this.todoList=new TodoList();
        return todoList;
    }

    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }

    @Override
    public String toString() {
        return "Profil{" +
                "pseudo='" + pseudo + '\'' +
                ", password='" + password + '\'' +
                ", todoList=" + todoList +
                '}';
    }
}
