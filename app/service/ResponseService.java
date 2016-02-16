package service;

import play.db.jpa.JPA;

import javax.persistence.Query;

/**
 * Created by Alexander on 16.02.2016.
 */
public class ResponseService {

    public static int countResponses() {
        int result = 0;
        try {
            result = JPA.withTransaction(() -> {
                Query query = JPA.em().createQuery("select e from Answer e");
                return query.getResultList().size();
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;

    }

}
