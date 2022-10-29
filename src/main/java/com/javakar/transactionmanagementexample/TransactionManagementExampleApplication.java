package com.javakar.transactionmanagementexample;

import com.javakar.transactionmanagementexample.author.InsertFirstAuthorService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TransactionManagementExampleApplication {

    private final InsertFirstAuthorService insertFirstAuthorService;
    public TransactionManagementExampleApplication(InsertFirstAuthorService insertFirstAuthorService) {
        this.insertFirstAuthorService = insertFirstAuthorService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TransactionManagementExampleApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            System.out.println("=========== Initialing Transaction =================================");
            insertFirstAuthorService.insertFirstAuthor();
            System.out.println("============================================");
        };
    }

}
