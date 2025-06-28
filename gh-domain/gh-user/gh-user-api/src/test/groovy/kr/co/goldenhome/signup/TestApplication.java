package kr.co.goldenhome.signup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "kr.co.goldenhome")
@EnableJpaRepositories(basePackages = "kr.co.goldenhome")
@SpringBootApplication(scanBasePackages = "kr.co.goldenhome")
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
