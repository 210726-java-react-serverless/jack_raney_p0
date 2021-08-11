package com.revature.registrar.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.revature.registrar.exceptions.DataSourceException;
import com.revature.registrar.models.Faculty;
import com.revature.registrar.models.Student;
import com.revature.registrar.models.User;
import com.revature.registrar.util.MongoClientFactory;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Provides methods to communicate and interact with the MongoDB users collection
 */
public class UserRepository implements CrudRepository<User> {

    /**
     * Searches the Database and returns a User with a matching ID
     * @param id
     * @return
     */
    @Override
    public User findById(int id) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase bookstoreDb = mongoClient.getDatabase("project0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("users");
            Document queryDoc = new Document("id", id);
            Document authUserDoc = usersCollection.find(queryDoc).first();

            if (authUserDoc == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            if ((boolean)authUserDoc.get("isFaculty")) {
                Faculty fac = mapper.readValue(authUserDoc.toJson(), Faculty.class);
                fac.setFaculty(true); //TODO: No idea why this isn't done by the mapper
                return fac;
            } else {
                Student stu = mapper.readValue(authUserDoc.toJson(), Student.class);
                return stu;
            }

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    /**
     * Stores a User, newResource, in the database
     * @param newResource
     * @return
     */
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

    /**
     * Private helper method which returns a Document representing a given Student
     * @param stu
     * @return
     */
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

    /**
     * Private helper method which returns a Document representing a given Faculty
     * @param fac
     * @return
     */
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

    /**
     * Private helper method which returns Bson representing a given Faculty
     * @param fac
     * @return
     */
    private Bson getFacultyUpdates(Faculty fac) {
        Bson updates = Updates.combine(
                Updates.set("firstName", fac.getFirstName()),
                Updates.set("lastName", fac.getLastName()),
                Updates.set("password", fac.getPassword()),
                Updates.set("classes", fac.getClassesAsDoc()));

        return updates;
    }

    /**
     * Private helper method which returns a Document representing a given Student
     * @param stu
     * @return
     */
    private Bson getStudentUpdates(Student stu) {
        Bson updates = Updates.combine(
                Updates.set("firstName", stu.getFirstName()),
                Updates.set("lastName", stu.getLastName()),
                Updates.set("password", stu.getPassword()),
                Updates.set("classes", stu.getClassesAsDoc()));

        return updates;
    }

    /**
     * Updates the fields of a database element with new data
     * @param updatedResource
     * @return
     */
    @Override
    public boolean update(User updatedResource) {
        Bson updates;
        if(updatedResource.isFaculty()) {
            Faculty fac = (Faculty) updatedResource;
            updates = getFacultyUpdates(fac);

        } else {
            Student stu = (Student) updatedResource;
            updates = getStudentUpdates(stu);
        }

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase bookstoreDb = mongoClient.getDatabase("project0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("users");

            Document query = new Document().append("id",  updatedResource.getId());
            usersCollection.updateOne(query, updates);
            //newResource.setId(newUserDoc.get("_id").toString());

            return true;

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }

    }

    /**
     * Returns a list of Users who have the classModel with a given id in their classes
     * @param id
     * @return
     */
    public List<User> findWithClass(int id) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase bookstoreDb = mongoClient.getDatabase("project0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("users");

            Document queryDoc = new Document("classes.id", id);
            List<Document> docs = new ArrayList<>();
            docs = usersCollection.find(queryDoc).into(docs);
            if (docs.size() == 0) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            List<User> userDocs = new ArrayList<>();
            for (Document d : docs) {
                if ((boolean)d.get("isFaculty")) {
                    Faculty fac = mapper.readValue(d.toJson(), Faculty.class);
                    fac.setFaculty(true); //TODO: No idea why this isn't done by the mapper
                    userDocs.add(fac);
                } else {
                    Student stu = mapper.readValue(d.toJson(), Student.class);
                    userDocs.add(stu);
                }
            }
            return userDocs;

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    /**
     * Not implemented, unnecessary
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(int id) {
        return false;
    }

    /**
     * Retrieves the User with a given username and password from the database
     * @param username
     * @param password
     * @return
     */
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
                return fac;
            } else {
                Student stu = mapper.readValue(authUserDoc.toJson(), Student.class);
                return stu;
            }

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }
}
