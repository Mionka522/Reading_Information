package org.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.UriEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;


@Component
@UriEndpoint(scheme = "jdbc", title = "PostgreSQL", syntax = "jdbc:postgresql://host:port/test_db", producerOnly = true)
public class DatabaseReader extends RouteBuilder {

    ApplicationContext context;
    @Autowired
    public DatabaseReader(ApplicationContext context) {
        this.context = context;
    }
    @Override
    public void configure(){
        DataSource dataSource = context.getBean("dataSource", DataSource.class);
            getContext().getRegistry().bind("test_db", dataSource);
            //Таймер 15 сек
            from("timer:base?period=15000")
                    .routeId("JDBC route")
                    .setBody(simple("select * from personTableForApp"))
                    .to("jdbc:test_db")
                    .log(">>> ${body}");

    }


}