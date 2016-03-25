package com.wasp.controller;

import com.wasp.model.Profil;
import com.wasp.model.Todo;
import com.wasp.service.ProfilService;
import com.wasp.util.annotations.*;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;

import java.util.Optional;

import static com.wasp.util.httpComponent.common.enums.HttpContentTypes.*;
import static com.wasp.util.httpComponent.request.enums.MethodType.*;
import static com.wasp.util.httpComponent.response.enums.EStatus.BAD_REQUEST;
import static com.wasp.util.httpComponent.response.enums.EStatus.NOT_FOUND;

@Controller
public class
        ProfilController {
    private static final ProfilService service = new ProfilService();

    @RequestMapping(resource = "/profil", methods = {GET})
    public Profil authentification(@RequestVariable("pseudo") String pseudo, @RequestVariable("password") String password) {
        Profil profilByPseudo = service.findProfilByPseudoAndPassword(pseudo, password);
        if (profilByPseudo != null)
            return profilByPseudo;
        return null;
    }

    @RequestMapping(resource = "/profil", methods = {POST}, contentType = {APPLICATION_JSON, APPLICATION_XML, QUERY_STRING})
    public Profil register(@RequestBody Profil profil) {
        if (service.isExist(profil.getPseudo()))
            return null;
        service.saveProfil(profil);
        return profil;
    }

    @RequestMapping(resource = "/todo/(id)", methods = {DELETE})
    public IHttpResponse deleteTodo(@PathVariable("id") String id, @RequestVariable("pseudo") String pseudo) {
        Profil profilByPseudo = service.findProfilByPseudo(pseudo);

        if (profilByPseudo == null)
            return new HttpResponseBuilder().status(NOT_FOUND).content("pseudo ou mot de passe incorrect").build();

        Optional<Todo> todo = profilByPseudo.getTodoList().getTodos().stream().filter(t -> t.getId().equals(id)).findFirst();
        if (!todo.isPresent())
            return new HttpResponseBuilder().status(NOT_FOUND).content("aucun todo avec id = " + id).build();

        profilByPseudo.getTodoList().getTodos().remove(todo.get());
        return new HttpResponseBuilder().noContent().build();
    }

    @RequestMapping(resource = "/todo/(id)/validate",methods = {PUT})
    public IHttpResponse validateTodo(@PathVariable("id") String id, @RequestVariable("pseudo") String pseudo) {
        Profil profilByPseudo = service.findProfilByPseudo(pseudo);

        if (profilByPseudo == null)
            return new HttpResponseBuilder().status(NOT_FOUND).content("pseudo ou mot de passe incorrect").build();

        Optional<Todo> todo = profilByPseudo.getTodoList().getTodos().stream().filter(t -> t.getId().equals(id)).findFirst();
        if (!todo.isPresent())
            return new HttpResponseBuilder().status(NOT_FOUND).content("aucun todo avec id = " + id).build();

        todo.get().setIsDone(true);
        return new HttpResponseBuilder().noContent().build();
    }

    @RequestMapping(resource = "/todo/(id)/unvalidate",methods = {PUT})
    public IHttpResponse unvalidateTodo(@PathVariable("id") String id, @RequestVariable("pseudo") String pseudo) {
        Profil profilByPseudo = service.findProfilByPseudo(pseudo);

        if (profilByPseudo == null)
            return new HttpResponseBuilder().status(NOT_FOUND).content("pseudo ou mot de passe incorrect").build();

        Optional<Todo> todo = profilByPseudo.getTodoList().getTodos().stream().filter(t -> t.getId().equals(id)).findFirst();
        if (!todo.isPresent())
            return new HttpResponseBuilder().status(NOT_FOUND).content("aucun todo avec id = " + id).build();

        todo.get().setIsDone(false);
        return new HttpResponseBuilder().noContent().build();
    }

    @RequestMapping(resource = "/todo",methods = {POST},contentType = {QUERY_STRING,APPLICATION_JSON,APPLICATION_XML})
    public IHttpResponse addTodo(@RequestBody Todo todo, @RequestVariable("pseudo") String pseudo) {
        Profil profilByPseudo = service.findProfilByPseudo(pseudo);
        if(todo.getContent()==null || todo.getContent().isEmpty()){
            return new HttpResponseBuilder().status(BAD_REQUEST).content("todo is empty").build();
        }
        if (profilByPseudo == null)
            return new HttpResponseBuilder().status(NOT_FOUND).content("pseudo ou mot de passe incorrect").build();

        return new HttpResponseBuilder().setEntity(service.addTodo(profilByPseudo,todo.getContent())).build();
    }

}
