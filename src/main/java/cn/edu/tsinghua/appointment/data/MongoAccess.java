package cn.edu.tsinghua.appointment.data;

import cn.edu.tsinghua.appointment.domain.Appointment;
import cn.edu.tsinghua.appointment.config.SpringMongoConfig;
import cn.edu.tsinghua.appointment.domain.Status;
import cn.edu.tsinghua.appointment.domain.User;
import cn.edu.tsinghua.appointment.domain.UserType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MongoAccess {

    public final static ApplicationContext CTX = new AnnotationConfigApplicationContext(
            SpringMongoConfig.class);
    public final static MongoOperations MONGO = (MongoOperations) CTX
            .getBean("mongoTemplate");

	/*
     * User
	 */

    public User addUser(String _username, String _password, UserType _type) {
        User newUser = new User(_username, _password, _type);
        MONGO.save(newUser);
        return newUser;
    }

    public User addUser(String _username, String _password, String _fullname, String _mobile, UserType _type) {
        User newUser = new User(_username, _password, _fullname, _mobile, _type);
        MONGO.save(newUser);
        return newUser;
    }

    public User saveUser(User _user) {
        MONGO.save(_user);
        return _user;
    }

    public User getUserByUsername(String _username) {
        return MongoAccess.MONGO.findOne(new Query(Criteria.where("username")
                .is(_username)), User.class);
    }

    public User getUserByFullname(String _fullname) {
        return MongoAccess.MONGO.findOne(new Query(Criteria.where("fullname")
                .is(_fullname)), User.class);
    }

    public User getUserByMobile(String _mobile) {
        return MongoAccess.MONGO.findOne(new Query(Criteria.where("mobile")
                .is(_mobile)), User.class);
    }

	/*
     * Appointment
	 */

    public Appointment saveApp(Appointment _app) {
        MONGO.save(_app);
        return _app;
    }

    public void removeApp(String _appId) {
        MONGO.remove(new Query(Criteria.where("id").is(_appId)),
                Appointment.class);
    }

    public Appointment getAppById(String _appId) {
        return MONGO.findOne(new Query(Criteria.where("id").is(_appId)),
                Appointment.class);
    }

    public List<Appointment> getAppsByStudentId(String studentId) {
        return MONGO.find(new Query(Criteria.where("studentInfo.studentId").is(studentId)),
                Appointment.class);
    }

    public List<Appointment> getAppsBetweenDateTimes(LocalDateTime from, LocalDateTime to) {
        Query query = new Query(Criteria.where("startTime").gte(from).lte(to));
        query.addCriteria(Criteria.where("status").ne(Status.DELETED));
        query.with(new Sort(Direction.ASC, "startTime"));
        return MONGO.find(query, Appointment.class);
    }

    public List<Appointment> getAppsBetweenDates(LocalDate from, LocalDate to) {
        Query query = new Query(Criteria.where("startTime").gte(from).lte(to));
        query.addCriteria(Criteria.where("status").ne(Status.DELETED));
        query.with(new Sort(Direction.ASC, "startTime"));
        return MONGO.find(query, Appointment.class);
    }

    public List<Appointment> getAppsAfterDates(LocalDate from) {
        Query query = new Query(Criteria.where("startTime").gte(from));
        query.addCriteria(Criteria.where("status").ne(Status.DELETED));
        query.with(new Sort(Direction.ASC, "startTime"));
        return MONGO.find(query, Appointment.class);
    }

}
