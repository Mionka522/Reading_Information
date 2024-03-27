package org.example.sql;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

@Configuration
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Scanner scanner = new Scanner(System.in);

        String name = "";
        String password = "";
        String dbUrl = "";
        boolean validinput = false;

 while (!validinput){
    try{
        System.out.print("Введите адрес сервера: ");
        String host = scanner.nextLine();

        System.out.print("Введите порт: ");
        String port = scanner.nextLine();

        System.out.print("Введите название существующей базы данных PostgreSql: ");
        String url = scanner.nextLine();

        System.out.print("Введите username: ");
        name = scanner.nextLine();

        System.out.print("Введите password: ");
        password = scanner.nextLine();


        dbUrl = "jdbc:postgresql://"+host+":"+port+"/"+url;
        validinput = getValidConnect(dbUrl, name, password); // Если данные введены корректно, выходим из цикла

        } catch (Exception e) {


            System.out.println("Наданный момент удалите таблицу personTableForApp в базе данных " +
                "и запустите приложение заново");

        System.exit(1); // остановка программы

        }
}
        scanner.close();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(name);
        dataSource.setPassword(password);
        return dataSource;
    }
    public boolean getValidConnect(String url,String username,String password) {
        //Проверка соеденения с базой данных
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Соединение с базой данных установлено");
            if(connection != null) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Не удалось установить соединение с базой данных");
            System.out.println("Введите данные повторно\n");

        }
        return false;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        //Существует ли таблица
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            String[] tableTypes = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", tableTypes);

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Table name: " + tableName);
                if (tableName.equals("personTableForApp")) {
                    System.out.println(tableName+ " уже существует");

                }
            }
        }catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        //Создание таблицы и её заполнение
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("create.sql"));
        databasePopulator.addScript(new ClassPathResource("data.sql"));

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);

        return initializer;
    }
}