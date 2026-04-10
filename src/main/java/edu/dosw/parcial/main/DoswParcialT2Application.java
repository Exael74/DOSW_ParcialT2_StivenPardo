package edu.dosw.parcial.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "edu.dosw.parcial")
public class DoswParcialT2Application {

    public static void main(String[] args) {
        SpringApplication.run(DoswParcialT2Application.class, args);
    }

}
