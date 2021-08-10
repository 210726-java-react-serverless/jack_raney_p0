package com.revature.registrar.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.revature.registrar.exceptions.DataSourceException;
import com.revature.registrar.models.Faculty;
import com.revature.registrar.models.Student;
import com.revature.registrar.models.User;
import com.revature.registrar.util.MongoClientFactory;
import org.bson.Document;

public class UserRepository implements CrudRepository<User> {
    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public User save(User newResource) {
        Document newUserDoc;
        if(newResource.isFaculty()) {
            Faculty fac = (Faculty) newResource;
            newUserDoc = getFacultyDoc(fac);
        } else {
            Student stu = (Student) newResource;
            newUserDoc = getStudentDoc(stu);
        }

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase bookstoreDb = mongoClient.getDatabase("project0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("users");

            usersCollection.insertOne(newUserDoc);
            //newResource.setId(newUserDoc.get("_id").toString());

            return newResource;

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    private Document getStudentDoc(Student stu) {
        Document newUserDoc = new Document("firstName", stu.getFirstName())
                .append("lastName", stu.getLastName())
                .append("email", stu.getEmail())
                .append("username", stu.getUsername())
                .append("password", stu.getPassword())
                .append("id", stu.getId())
                .append("classes", stu.getClassesAsDoc())
                .append("isFaculty", false);

        return newUserDoc;
    }

    private Document getFacultyDoc(Faculty fac) {
        Document newUserDoc = new Document("firstName", fac.getFirstName())
                .append("lastName", fac.getLastName())
                .append("email", fac.getEmail())
                .append("username", fac.getUsername())
                .append("password", fac.getPassword())
                .append("id", fac.getId())
                .append("classes", fac.getClassesAsDoc())
                .append("isFaculty", true);

        return newUserDoc;
    }

    @Override
    public boolean update(User updatedResource) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    public User findUserByCredentials(String username, String password) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase bookstoreDb = mongoClient.getDatabase("project0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("users");
            Document queryDoc = new Document("username", username)
                    .append("password", password);

            Document authUserDoc = usersCollection.find(queryDoc).first();

            if (authUserDoc == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            if ((boolean)authUserDoc.get("isFaculty")) {
                Faculty fac = mapper.readValue(authUserDoc.toJson(), Faculty.class);
                fac.setFaculty(true); //TODO: No idea why this isn't done by the mapper
                System.out.println("FAC");
                System.out.println(fac);
                return fac;
            } else {
                Student stu = mapper.readValue(authUserDoc.toJson(), Student.class);
                System.out.println("STU");
                System.out.println(stu);
                return stu;
            }

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }
}
