package service.validation;

import models.Field;
import models.Type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alexander on 15.02.2016.
 */
public class FieldValidation {

    public static Set<String> validationField(Field field) {
        Set<String> errors = new HashSet<>();
        if (field.getLabel().length() > 255) {
            errors.add("label");
        }
        if (field.getType() == Type.CHECK_BOX && field.getOptions().size() < 1) {
            errors.add("option");
        }
        if ((field.getType() == Type.COMBO_BOX || field.getType() == Type.RADIO_BUTTON) && field.getOptions().size() < 2) {
            errors.add("option");
        }
        if (Arrays.asList(Type.values()).contains(field.getType()) == false) {
            errors.add("type");
        }
        return errors;
    }
}
