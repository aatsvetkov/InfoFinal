package service;

import models.Answer;
import models.Field;
import models.Option;
import models.Type;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.JPA;
import service.validation.FieldValidation;
import sun.reflect.generics.tree.Tree;

import javax.persistence.Query;
import java.util.*;

/**
 * Created by Alexander on 10.02.2016.
 */
public class FieldResponseService {

    /**
     * Create field from the completed form
     * */
    public static Set<String> addFieldInDB() {
        DynamicForm formDyn = Form.form().bindFromRequest();
        Map<String, String> map = formDyn.data();
        Field field = new Field();
        Set<Option> options = new HashSet<>();
        for (String s : map.keySet()) {
            if (s.equals("type")) {
                if (map.get(s).equals(Type.CHECK_BOX.name()) || map.get(s).equals(Type.COMBO_BOX.name()) || map.get(s).equals(Type.RADIO_BUTTON.name())) {
                    field.setType(Type.valueOf(map.get(s)));
                    for (int i = 0; map.get("option[" + i + "]") != null; i++) {
                        Option option = new Option();
                        option.setName(map.get("option[" + i + "]"));
                        option.setField(field);
                        options.add(option);
                    }
                } else if (map.get(s).equals(Type.DATE.name()) || map.get(s).equals(Type.TEXTAREA.name()) || map.get(s).equals(Type.SLIDER.name()) || map.get(s).equals(Type.SINGLE_LINE_TEXT.name())) {
                    field.setType(Type.valueOf(map.get(s)));
                }
            } else if (s.equals("label")) {
                field.setLabel(map.get(s));
            } else if (s.equals("isactive")) {
                field.setIsActive(true);
            } else if (s.equals("required")) {
                field.setRequired(true);
            }
        }
        field.setOptions(options);
        if (FieldValidation.validationField(field).size()!=0){
            return FieldValidation.validationField(field);
        } else {
            JPA.em().persist(field);
            return null;
        }

    }

    /**
     Save response from the main page to database.
     * */
    public static models.Form saveResponse(){
        models.Form form = new models.Form();
        DynamicForm formDyn = Form.form().bindFromRequest();
        Set<Answer> answers = new HashSet<>();
        Map<String, String> map = formDyn.data();
        Query queryFields = JPA.em().createQuery("select e from Field e where e.isActive = true");
        List<Field> allFields = queryFields.getResultList();
        for (Field f : allFields) {
            Set<Option> options = new HashSet<>();
            Answer answer = new Answer();
            for (String s : map.keySet()) {
                if (s.startsWith(Integer.toString(f.getFieldId()))) {
                    if (f.getType() != Type.SINGLE_LINE_TEXT && f.getType() != Type.TEXTAREA && f.getType() != Type.DATE && f.getType() != Type.SLIDER) {
                        Option option = JPA.em().find(Option.class, Integer.parseInt(map.get(s)));
                        options.add(option);
                    } else {
                        if(map.get(s)!=""){
                            answer.setValue(map.get(s));
                        }
                        continue;
                    }
                }
            }
            answer.setField(f);
            answer.setOptions(options);
            answer.setForm(form);
            answers.add(answer);
        }
        form.setAnswers(answers);
        JPA.em().persist(form);
        return form;
    }

    public static List<Field> getAllActiveFields(){
        Query query = JPA.em().createQuery("select e from Field e where e.isActive = true");
        List<Field> fields = query.getResultList();
        return fields;
    }
    public static List<models.Form> getAllResponses(){
        Query query = JPA.em().createQuery("select e from Form e");
        List<models.Form> forms = query.getResultList();
        return forms;
    }
    public static List<Field> getAllFields(){
        Query queryFields = JPA.em().createQuery("select e from Field e");
        List<Field> fields = queryFields.getResultList();
        return fields;
    }
    public static void deleteFieldById(){
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        int id = Integer.parseInt(dynamicForm.get("delid"));
        Field field = JPA.em().find(Field.class, id);
        JPA.em().remove(field);
        JPA.em().createQuery("delete from Form r where r.answers.size=0").executeUpdate();
    }


}
