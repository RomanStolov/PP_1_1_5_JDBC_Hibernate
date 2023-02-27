package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
// Для создания таблицы в MySQL Workbench вручную
// CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT, name VARCHAR(25) NOT NULL, lastName VARCHAR(25) NOT NULL, age TINYINT NOT NULL, CHECK((age > 0 AND age < 100) AND (name != '') AND (lastName != '')), PRIMARY KEY (id));
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT, " +
                    "name VARCHAR(25) NOT NULL, " +
                    "lastName VARCHAR(25) NOT NULL, " +
                    "age TINYINT NOT NULL, " +
                    "CHECK((age > 0 AND age < 100) AND (name != '') AND (lastName != '')), " +
                    "PRIMARY KEY (id))";
            Query query = session.createSQLQuery(sql);
//                    .addEntity(User.class);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка при создании таблицы!");
//            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS users";
            Query query = session.createSQLQuery(sql);
//            .addEntity(User.class);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка при удалении таблицы!");
//            e.printStackTrace();
        }

    }

//// Вариант №1 - Без отмены транзакции
//    @Override
//    public void saveUser(String name, String lastName, byte age) {
//        try (Session session = Util.getSessionFactory().getCurrentSession()) {
//            session.beginTransaction();
//            session.save(new User(name, lastName, age));
//            session.getTransaction().commit();
//            System.out.println("User с именем – " + name + " добавлен в базу данных");
//        } catch (Exception e) {
//            System.out.println("Ошибка при добавлении пользователя в таблицу!");
////            e.printStackTrace();
//        }
//    }

// Вариант №2 - С отменой транзакции
    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = null;
        try {
            session = Util.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении пользователя в таблицу!");
//            e.printStackTrace();
            if (session.isOpen()) {
                session.getTransaction().rollback();
                System.out.println("Транзакция отменена!");
            }
        } finally {
            if (session.isOpen()) {
                session.close();
                System.out.println("Сессия закрыта!");
            }
        }
    }

//// Вариант №1 - Без отмены транзакции
//    @Override
//    public void removeUserById(long id) {
//        try (Session session = Util.getSessionFactory().openSession()) {
//            session.beginTransaction();
//            User userDelete = session.get(User.class, id);;
//            session.delete(userDelete);
//            session.getTransaction().commit();
//            System.out.println("User с id – " + id + " удален из базы данных");
//        } catch (Exception e) {
//            System.out.println("Ошибка при удалении пользователя из таблицы по 'id'!");
////            e.printStackTrace();
//        }
//    }

// Вариант №2 - С отменой транзакции
    @Override
    public void removeUserById(long id) {
        Session session = null;
        try {
            session = Util.getSessionFactory().openSession();
            session.beginTransaction();
//            User userDelete = session.load(User.class, id);;
//            session.delete(userDelete);
            User userDelete = session.get(User.class, id);;
            session.remove(userDelete);
            session.getTransaction().commit();
            System.out.println("User с id – " + id + " удален из базы данных");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении пользователя из таблицы по 'id'!");
//            e.printStackTrace();
            if (session.isOpen()) {
                session.getTransaction().rollback();
                System.out.println("Транзакция отменена!");
            }
        } finally {
            if (session.isOpen()) {
                session.close();
                System.out.println("Сессия закрыта!");
            }
        }
    }

//// Вариант №1 - Без отмены транзакции
//    @Override
//    public List<User> getAllUsers() {
//        List<User> usersResult = null;
//        try (Session session = Util.getSessionFactory().openSession()) {
//            session.beginTransaction();
////            usersResult = session.createQuery("FROM User", User.class).list();
//            usersResult = session.createQuery("FROM User", User.class).getResultList();
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            System.out.println("Ошибка при получении списка пользователей из таблицы!");
////            e.printStackTrace();
//        }
//        return usersResult;
//    }

// Вариант №2 - С отменой транзакции
    @Override
    public List<User> getAllUsers() {
        List<User> usersResult = null;
        Session session = null;
        try {
            session = Util.getSessionFactory().openSession();
            session.beginTransaction();
//            usersResult = session.createQuery("FROM User", User.class).list();
            usersResult = session.createQuery("FROM User", User.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка пользователей из таблицы!");
//            e.printStackTrace();
            if (session.isOpen()) {
                session.getTransaction().rollback();
                System.out.println("Транзакция отменена!");
            }
        } finally {
            if (session.isOpen()) {
                session.close();
//                System.out.println("Сессия закрыта!");
            }
        }
        return usersResult;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
//            String sql = "DELETE FROM users";
            String sql = "TRUNCATE TABLE users";
            Query query = session.createSQLQuery(sql);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка при удалении всех пользователей из таблицы!");
//            e.printStackTrace();
        }
    }
}
