package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            System.out.println("Подключение к базе данных успешно!");
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}