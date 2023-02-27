package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";
//    private static final String DB_DIALECT = "org.hibernate.dialect.MySQLDialect";
//    private static final String DB_DIALECT = "org.hibernate.dialect.MySQL5Dialect";
    private static final String DB_DIALECT = "org.hibernate.dialect.MySQL8Dialect";
// Включение логирования запросов к базе данных в консоль
//    private static final String SHOW_SQL = "true";
    private static final String SHOW_SQL = "false";
    private static final String CURRENT_SESSION_CONTEXT_CLASS = "thread";
// Создание схемы данных
// "validate" - Валидация: Hibernate проверит, совпадают ли имена и типа колонок и полей в базе и в аннотациях.
// "update" - Апдейт: Hibernate обновит таблицы в базе, если они или их колонки отличаются от ожидаемых.
// "create" - Пересоздание: Hibernate удалит все таблицы в базе и создаст их заново на основе данных из аннотаций.
// "create-drop" - Создание-удаление. В начале работы Hibernate создаст все таблицы, в конце работы – удалит их за собой.
// "none" - Hibernate вообще ничего не будет делать. Если база не совпадает с ожиданием, то будут ошибки во время выполнения запросов.
//    private static final String HBM2DDL_AUTO = "validate";
//    private static final String HBM2DDL_AUTO = "update";
//    private static final String HBM2DDL_AUTO = "create";
//    private static final String HBM2DDL_AUTO = "create-drop";
    private static final String HBM2DDL_AUTO = "none";


    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Properties properties = new Properties();
                properties.put(Environment.DRIVER, DB_DRIVER);
                properties.put(Environment.URL, DB_URL);
                properties.put(Environment.USER, DB_USERNAME);
                properties.put(Environment.PASS, DB_PASSWORD);
                properties.put(Environment.DIALECT, DB_DIALECT);
                properties.put(Environment.SHOW_SQL, SHOW_SQL);
                properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, CURRENT_SESSION_CONTEXT_CLASS);
// Задать вопрос ментору: "Почему не работает со значением HBM2DDL_AUTO = "validate" ???"
                properties.put(Environment.HBM2DDL_AUTO, HBM2DDL_AUTO);

// Вариант №1 - С "ServiceRegistry"
                Configuration configuration = new Configuration()
                        .setProperties(properties)
                        .addAnnotatedClass(User.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                sessionFactory = configuration
                        .buildSessionFactory(serviceRegistry);

// Вариант №2 - Без "ServiceRegistry"
//                sessionFactory = new Configuration()
//                        .setProperties(properties)
//                        .addAnnotatedClass(User.class)
//                        .buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

}
