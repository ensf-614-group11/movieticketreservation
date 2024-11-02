package ensfgroup11.acmeplex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AcmeplexApplication {
	public static void main(String[] args) {
		SpringApplication.run(AcmeplexApplication.class, args);
	}
}
