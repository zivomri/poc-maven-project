package com.example;

import org.codehaus.plexus.util.StringUtils;
import com.thoughtworks.xstream.XStream;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // 1. Using Plexus Utils
        String[] strings = {"Hello", "World"};
        String concatenated = StringUtils.join(strings, " ");
        logger.info("Concatenated String: " + concatenated);
        
        // 2. Using XStream
        XStream xStream = new XStream();
        xStream.alias("person", Person.class);
        Person person = new Person("John", 30);
        String xml = xStream.toXML(person);
        logger.info("XML Representation: " + xml);
        
        // 3. Using Logback
        logger.info("This is a log message from Logback");
        
        // 4. Using PostgreSQL
        String url = "jdbc:postgresql://localhost:5432/mydb";
        String user = "myuser";
        String password = "mypassword";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            logger.info("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            logger.error("Connection failed: ", e);
        }
    }

    public static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }
}