package controllers;

import models.*;
import models.Field;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.*;
import service.FieldResponseService;
import service.ResponseService;
import service.Secured;
import service.UserService;
import views.html.*;

import javax.persistence.Query;
import java.util.*;


public class Application extends Controller {


    private static final Set<WebSocket.Out<String>> SOCKETS = new HashSet<>();


    public WebSocket<String> socket() {
        return WebSocket.whenReady((in, out) -> {
            SOCKETS.add(out);
            out.write("{\"count\":" + ResponseService.countResponses() + "}");
            in.onMessage((String message) -> {
                SOCKETS.forEach(s -> s.write(message));
            });
            in.onClose(() -> {
                SOCKETS.remove(out);
            });
        });
    }

    public Result login() {
        return ok(login.render(""));
    }

    @Transactional
    public Result authenticate() {
        DynamicForm formDyn = Form.form().bindFromRequest();
        Map<String, String> map = formDyn.data();
        Admin admin = new Admin();
        admin.setPassword(map.get("password"));
        admin.setUsername(map.get("username"));
        boolean success = UserService.check(admin);
        if (success) {
            session().clear();
            session("username", admin.getUsername());
            return ok();
        } else {
            session().clear();
            return badRequest(login.render("invalid username or password"));
        }
    }


    @Transactional
    public Result index() {
        List<Field> fields = FieldResponseService.getAllActiveFields();
        return ok(index.render(fields));
    }

    @Security.Authenticated(Secured.class)
    public Result createField() {
        return ok(createfield.render(null));
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public Result addField() {
        Set<String> errors = FieldResponseService.addFieldInDB();
        if (errors == null) {
            return redirect("/fields");
        } else {
            return badRequest();
        }

    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public Result getAllFields() {
        Query query = JPA.em().createQuery("select e from Field e");
        List<Field> fields = query.getResultList();
        return ok(fieldsTemplate.render(fields));
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public Result updateField(int id) {
        Field field = JPA.em().find(Field.class, id);
        return ok(createfield.render(field));
    }


    @Security.Authenticated(Secured.class)
    @Transactional
    public Result updateFieldPost() {
        DynamicForm formDyn = Form.form().bindFromRequest();
        int fieldId = Integer.parseInt(formDyn.get("fieldid"));
        JPA.em().remove(JPA.em().find(Field.class, fieldId));
        FieldResponseService.addFieldInDB();
        return redirect("/fields");
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public Result deleteField() {
        FieldResponseService.deleteFieldById();
        return redirect("/fields");
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public Result getAllResponses() {
        List<models.Form> forms = FieldResponseService.getAllResponses();
        List<Field> fields = FieldResponseService.getAllFields();
        return ok(responsesTemplate.render(forms, fields));
    }


    @Security.Authenticated(Secured.class)
    @Transactional
    public Result logout() {
        session().clear();
        return redirect("/login");
    }

    @Transactional
    public Result saveResponse() {
        models.Form response = FieldResponseService.saveResponse();
        for (WebSocket.Out<String> socket : SOCKETS) {
            socket.write("1");
        }
        return ok(thanks.render());
    }
}

