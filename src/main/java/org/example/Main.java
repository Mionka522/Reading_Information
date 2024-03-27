package org.example;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.example.sql.DatabaseConfig;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = null;
        boolean connected = false;
        while (!connected) {
            try {
                context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
                connected = true;
            } catch (BeanCreationException e) {
                System.out.println("Ошибка подключения к базе данных. Повторная попытка подключения...");
                try {
                    Thread.sleep(1000); // Ждем некоторое время перед повторной попыткой
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        checkDatabase(context);

    }
    public static void checkDatabase(ApplicationContext context) throws Exception {
        CamelContext camel = new DefaultCamelContext();
        camel.addRoutes(new DatabaseReader(context));
        camel.start();
        while (true) {
            Thread.sleep(1000);
        }
    }
}
