package ch.puzzle.pcts;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@OpenAPIDefinition(servers = { @Server(url = "/", description = "Default Server URL") })
@SpringBootApplication
@ConfigurationPropertiesScan
public class PctsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PctsApplication.class, args);
    }

}
