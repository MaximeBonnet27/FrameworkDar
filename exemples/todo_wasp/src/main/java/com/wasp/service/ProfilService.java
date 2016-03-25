package com.wasp.service;

import com.wasp.model.Profil;
import com.wasp.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProfilService {
    private static final List<Profil> dataBase=new ArrayList<>();

    private static String nextID(){
        return "u"+UUID.randomUUID();
    }
    public Profil findProfilByPseudo(String pseudo){
        Optional<Profil> profil = dataBase.stream().filter(p -> p.getPseudo().equals(pseudo)).findFirst();
        if(profil.isPresent())
            return profil.get();
        return null;
    }

    public Profil findProfilByPseudoAndPassword(String pseudo,String password){
        Optional<Profil> profil = dataBase.stream().filter(p -> p.getPseudo().equals(pseudo)).findFirst();
        if(profil.isPresent() && profil.get().getPassword().equals(password))
            return profil.get();
        return null;
    }

    public void saveProfil(Profil profil){
        dataBase.add(profil);
    }

    public void deleteProfilByPseudo(String pseudo){
        Optional<Profil> profil = dataBase.stream().filter(p -> p.getPseudo().equals(pseudo)).findFirst();
        if(profil.isPresent())
            dataBase.remove(profil.get());
    }

    public boolean isExist(String pseudo){
        return findProfilByPseudo(pseudo)!=null;
    }

    public Todo addTodo(Profil profil,String content){
        Todo todo = new Todo(nextID(), content, false);
        profil.getTodoList().getTodos().add(todo);
        return todo;
    }
}
