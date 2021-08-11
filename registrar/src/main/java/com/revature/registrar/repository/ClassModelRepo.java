package com.revature.registrar.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.revature.registrar.exceptions.DataSourceException;
import com.revature.registrar.models.ClassModel;
import com.revature.registrar.models.Faculty;
import com.revature.registrar.models.Student;
import com.revature.registrar.util.MongoClientFactory;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.util.*;

public class ClassModelRepo implements CrudRepository<ClassModel>{
    @Override
    public ClassModel findById(int id) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase bookstoreDb = mongoClient.getDatabase("project0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("classes");
            Document queryDoc = new Document("id", id);

            Document authClassDoc = usersCollection.find(queryDoc).first();

            if (authClassDoc == null) {
                return null;
            } else {
                //Convert from millis to Calendar
                Date d = new Date((long)authClassDoc.get("openWindow"));
                Calendar openDate = new Calendar.Builder()
                        .setInstant(d)
                        .build();
                d = new Date((long)authClassDoc.get("closeWindow"));
                Calendar closeDate = new Calendar.Builder()
                        .setInstant(d)
                        .build();
                authClassDoc.remove("openWindow");
                authClassDoc.remove("closeWindow");
                //authClassDoc.append("openWindow", openDate);
                //authClassDoc.append("closeWindow", closeDate);
                ObjectMapper mapper = new ObjectMapper();
                ClassModel auth = mapper.readValue(authClassDoc.toJson(), ClassModel.class);
                auth.setOpenWindow(openDate);
                auth.setCloseWindow(closeDate);
                return auth;
            }

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    @Override
    public ClassModel save(ClassModel newResource) {
        Document newUserDoc = new Document("name", newResource.getName())
                .append("capacity", newResource.getCapacity())
                .append("description", newResource.getDescription())
                .append("openWindow", newResource.getOpenWindow().getTimeInMillis())
                .append("closeWindow", newResource.getCloseWindow().getTimeInMillis())
                .append("id", newResource.getId())
                .append("students", newResource.getStudentsAsDoc())
                .append("faculty", newResource.getFacultyAsDoc());

        System.out.println(newUserDoc);

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase bookstoreDb = mongoClient.getDatabase("project0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("classes");

            usersCollection.insertOne(newUserDoc);
            System.out.println(newUserDoc);
            return newResource;

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    public List<ClassModel> findOpenClasses() {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase bookstoreDb = mongoClient.getDatabase("project0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("classes");

            long current = Calendar.getInstance().getTimeInMillis();

            Document query = new Document()
                    .append("openWindow", new Document("$lt", current))
                    .append("closeWindow", new Document("$gt", current));

            List<ClassModel> result = new ArrayList<>();
            for (Document doc: usersCollection.find(query)) {
                Date d = new Date((long)doc.get("openWindow"));
                Calendar openDate = new Calendar.Builder()
                        .setInstant(d)
                        .build();
                d = new Date((long)doc.get("closeWindow"));
                Calendar closeDate = new Calendar.Builder()
                        .setInstant(d)
                        .build();

                ObjectMapper mapper = new ObjectMapper();
                ClassModel classModel = mapper.readValue(doc.toJson(), ClassModel.class);
                classModel.setOpenWindow(openDate);
                classModel.setCloseWindow(closeDate);
                result.add(classModel);
            }

            if (result.size() == 0) {
                return null;
            } else {
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    @Override
    public boolean update(ClassModel updatedResource) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase bookstoreDb = mongoClient.getDatabase("project0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("classes");

            Bson updates = Updates.combine(
                    Updates.set("capacity", updatedResource.getCapacity()),
                    Updates.set("description", updatedResource.getDescription()),
                    Updates.set("openWindow", updatedResource.getOpenWindow().getTimeInMillis()),
                    Updates.set("closeWindow", updatedResource.getCloseWindow().getTimeInMillis()),
                    Updates.set("students", updatedResource.getStudentsAsDoc()),
                    Updates.set("faculty", updatedResource.getFacultyAsDoc()));

            Document query = new Document().append("id",  updatedResource.getId());
            usersCollection.updateOne(query, updates);
            return true;

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }


    //WHAT IF IT IS NOT IN THE DB???
    @Override
    public boolean deleteById(int id) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase bookstoreDb = mongoClient.getDatabase("project0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("classes");
            Document queryDoc = new Document("id", id);
            usersCollection.deleteOne(queryDoc);
            return true;

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }

    }
}
