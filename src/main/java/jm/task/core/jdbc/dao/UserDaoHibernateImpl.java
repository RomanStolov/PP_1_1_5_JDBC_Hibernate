package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT, " +
                    "name VARCHAR(25) NOT NULL, " +
                    "lastName VARCHAR(25) NOT NULL, " +
                    "age TINYINT NOT NULL, " +
                    "CHECK((age > 0 AND age < 100) AND (name != '') AND (lastName != '')), " +
                    "PRIMARY KEY (id))";
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка при создании таблицы!");
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS users";
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка при удалении таблицы!");
            e.printStackTrace();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении пользователя в таблицу!");
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            User userDelete = session.get(User.class, id);
            if (userDelete != null) {
                session.delete(userDelete);
            } else {
                System.out.println("Нет пользователя для удаления с id=" + id + "!");
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка при удалении пользователя из таблицы по 'id'!");
            e.printStackTrace();
        }
    }

//    // Обычный вариант - Ментор принял
//    @Override
//    public List<User> getAllUsers() {
//        List<User> usersResult = null;
//        try (Session session = getSessionFactory().openSession()) {
//            session.beginTransaction();
//            usersResult = session.createQuery("SELECT u FROM User u", User.class).getResultList();
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            System.out.println("Ошибка при получении списка пользователей из таблицы!");
//            e.printStackTrace();
//        }
//        return usersResult;
//    }

    // Короткий вариант без транзакций - То что ментор запрашивал через "TypedQuery"
    @Override
    public List<User> getAllUsers() {
        List<User> usersResult = null;
        try (Session session = getSessionFactory().openSession()) {
            TypedQuery<User> typedQuery = session.createQuery("FROM User");
            usersResult = typedQuery.getResultList();
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка пользователей из таблицы!");
            e.printStackTrace();
        }
        return usersResult;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            String sql = "TRUNCATE TABLE users";
            session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка при удалении всех пользователей из таблицы!");
            e.printStackTrace();
        }
    }
}
