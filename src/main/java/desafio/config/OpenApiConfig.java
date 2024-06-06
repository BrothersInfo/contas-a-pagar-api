package desafio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI beanOpenAPIConfig() {

        License license = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Contas a Pagar")
                .version("1.0")
                .description("API para gerenciamento de contas a pagar")
                .license(license);

        return new OpenAPI()
                .info(info);
    }
}
