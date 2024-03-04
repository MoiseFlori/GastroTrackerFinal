package com.example.GastroProject.configurations;
import com.example.GastroProject.service.dataLoader.DataLoaderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner {

    private final DataLoaderService dataLoaderService;

    public AppStartupRunner(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    @Bean
    CommandLineRunner loadDataOnStartup() {
        return args -> {
            dataLoaderService.loadData();
            System.out.println("All data loaded from CSV files successfully!");
        };
    }
}
