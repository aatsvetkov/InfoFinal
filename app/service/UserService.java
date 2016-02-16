package service;

import models.Admin;
import play.db.jpa.JPA;

import java.util.List;

/**
 * Created by Alexander on 13.02.2016.
 */
public class UserService {

    public static boolean check(Admin admin) {
        boolean result = JPA.em().createQuery("select r FROM Admin r WHERE r.username = :username and r.password =:password")
                .setParameter("username", admin.getUsername()).setParameter("password", admin.getPassword()).getResultList().isEmpty();
        return !result;

    }
}
