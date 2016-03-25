package com.wasp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class TodoList {
    private List<Todo> todos;

    public TodoList() {
        this.todos =new ArrayList<>();
    }

    public TodoList(List<Todo> todos) {
        this.todos = todos;
    }

    @XmlElement(name = "todo")
    public List<Todo> getTodos() {
        if(this.todos ==null)
            this.todos =new ArrayList<>();
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    @Override
    public String toString() {
        return "TodoList{" +
                "todos=" + todos +
                '}';
    }
}
