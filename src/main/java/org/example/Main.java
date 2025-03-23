package org.example;

import jakarta.persistence.OptimisticLockException;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int MAX_THREAD_POOL = 8;
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Items.class)
                .buildSessionFactory();

        fillTable();

        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);

        for (int i = 0; i < MAX_THREAD_POOL; i++){
            executorService.execute(()-> processItems());
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            // Ждем завершения всех потоков
        }
        checkResult();
    }

    public static void fillTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < 40; i++){
            session.save(new Items(0));
        }
        transaction.commit();
        session.close();
    }

    public static void processItems()  {
        for (int i = 0; i < 20000; i++){
            Session session = null;

            boolean retry = true;
            while (retry){
                try {
                    session = sessionFactory.openSession();
                    session.beginTransaction();

                    int random_id = (int) (Math.random() * 40) + 1;
                    Items item = session.get(Items.class, random_id);

                    synchronized (item) {
                        item.setVal(item.getVal() + 1);
                    }
                    session.saveOrUpdate(item);
//                    System.err.println(Thread.currentThread().getName() + " обновляет запись с id=" + random_id);

                    Thread.sleep(5);
                    session.getTransaction().commit();

                    retry = false;
                } catch (StaleObjectStateException | OptimisticLockException e) {
                    if (session.getTransaction() != null)
                        session.getTransaction().rollback();
                    retry = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } finally {
                    if (session != null)
                        session.close();
                }
            }
        }
    }
    public static void checkResult() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Long totalSum = session.createQuery("select SUM(val) from Items", Long.class)
                .uniqueResult();

        transaction.commit();
        session.close();

        System.out.println("Общая сумма: " + totalSum);
        if (totalSum != MAX_THREAD_POOL * 20000) {
            System.out.println("Ошибка: Сумма не соответствует ожидаемой!");
        } else {
            System.out.println("Успех: Сумма корректна!");
        }
    }
}