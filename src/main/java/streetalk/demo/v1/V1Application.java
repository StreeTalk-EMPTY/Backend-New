package streetalk.demo.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class V1Application {

	public static void main(String[] args) {
		SpringApplication.run(V1Application.class, args);
	}

}
