package com.trx.frecorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class FlightRecorderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightRecorderApplication.class, args);
    }

}
