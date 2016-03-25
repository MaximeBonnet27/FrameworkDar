package com.wasp.controller;

import com.wasp.model.Profil;
import com.wasp.model.Todo;
import com.wasp.service.ProfilService;
import com.wasp.util.annotations.Controller;
import com.wasp.util.annotations.Request;
import com.wasp.util.annotations.RequestBody;
import com.wasp.util.annotations.RequestMapping;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import com.wasp.util.views.IView;
import com.wasp.util.views.ViewModel;

import java.util.List;

import static com.wasp.util.httpComponent.request.enums.MethodType.GET;
import static com.wasp.util.httpComponent.request.enums.MethodType.POST;
import static com.wasp.util.httpComponent.response.enums.EStatus.PRECONDITION_FAILED;

@Controller
public class WebSiteController {
    private static final ProfilService service = new ProfilService();


    @RequestMapping(resource = "/logout", methods = {GET})
    public IHttpResponse logout(@Request IHttpRequest request) {
        request.getHttpSession().remove("todo_profil");
        return new HttpResponseBuilder().redirect("welcome").build();
    }

    @RequestMapping(resource = "/|/welcome", methods = {GET})
    public IHttpResponse welcoming(@Request IHttpRequest request) {
        Profil profil = (Profil) request.getHttpSession().get("todo_profil");
        if (profil != null)
            return new HttpResponseBuilder()
                    .redirect("home")
                    .setEntity(profil)
                    .build();
        return new HttpResponseBuilder()
                .setEntity(createWelcomeView(""))
                .build();
    }

    @RequestMapping(resource = "/|/welcome", methods = {POST})
    public IHttpResponse welcoming(@RequestBody Profil profil, @Request IHttpRequest request) {
        Profil profilByPseudo = service.findProfilByPseudo(profil.getPseudo());

        if (profilByPseudo == null || !profilByPseudo.getPassword().equals(profil.getPassword()))
            return new HttpResponseBuilder()
                    .setEntity(createWelcomeView("mauvais pseudo ou mot de passe"))
                    .build();

        request.getHttpSession().put("todo_profil", profilByPseudo);
        return new HttpResponseBuilder()
                .redirect("home")
                .setEntity(profilByPseudo)
                .build();

    }

    @RequestMapping(resource = "/sigin", methods = {GET})
    public IView sigin() {
        return createSiginView("");
    }

    @RequestMapping(resource = "/sigin", methods = {POST})
    public IHttpResponse sigin(@RequestBody Profil profil, @Request IHttpRequest request) {
        if (profil.getPseudo() == null)
            return new HttpResponseBuilder()
                    .setEntity(createSiginView("pseudo vide"))
                    .build();

        if (profil.getPassword() == null)
            return new HttpResponseBuilder()
                    .setEntity(createSiginView("password vide"))
                    .build();

        if (service.isExist(profil.getPseudo()))
            return new HttpResponseBuilder()
                    .setEntity(createSiginView("pseudo deja pris"))
                    .build();

        service.saveProfil(profil);
        request.getHttpSession().put("todo_profil", profil);
        return new HttpResponseBuilder()
                .redirect("home")
                .setEntity(profil)
                .build();
    }

    @RequestMapping(resource = "/home", methods = {POST})
    public IHttpResponse homeView(@RequestBody Profil profil) {
        Profil profilByPseudo = service.findProfilByPseudo(profil.getPseudo());
        if (profilByPseudo == null || !profilByPseudo.getPassword().equals(profil.getPassword()))
            return new HttpResponseBuilder()
                    .status(PRECONDITION_FAILED)
                    .build();

        return new HttpResponseBuilder()
                .setEntity(createHomeView(profilByPseudo))
                .build();
    }

    @RequestMapping(resource = "/home", methods = {GET})
    public IHttpResponse homeView(@Request IHttpRequest request) {
        Profil profil = (Profil) request.getHttpSession().get("todo_profil");
        if (profil == null)
            return new HttpResponseBuilder()
                    .redirect("welcome")
                    .build();

        return new HttpResponseBuilder()
                .setEntity(createHomeView(profil))
                .build();
    }

    private ViewModel createHomeView(Profil profil) {
        ViewModel home = new ViewModel("home");
        home.addTemplate("head", new ViewModel("templates/head"));
        home.putAttribute("pseudo", profil.getPseudo());

        List<Todo> todos = profil.getTodoList().getTodos();
        for (int i = todos.size() - 1; 0 < i; i--) {
            Todo todo = todos.get(i);
            ViewModel todoView = new ViewModel("templates/todo");
            if (todo.getIsDone()) {
                todoView.putAttribute("checked", "checked");
                todoView.putAttribute("validate", "validate");
            }
            todoView.putAttribute("todo", todo);
            home.addTemplate("todo", todoView);
        }
        return home;
    }

    private ViewModel createWelcomeView(String error) {
        ViewModel welcome = new ViewModel("welcome");
        welcome.addTemplate("head", new ViewModel("templates/head"));
        welcome.putAttribute("error", error);
        return welcome;
    }

    private ViewModel createSiginView(String error) {
        ViewModel sigin = new ViewModel("sigin");
        sigin.addTemplate("head", new ViewModel("templates/head"));
        sigin.putAttribute("error", error);
        return sigin;
    }
}
