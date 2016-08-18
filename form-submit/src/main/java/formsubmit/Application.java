package formsubmit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartException;

@SpringBootApplication
public class Application {
	@Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> container.addErrorPages(new ErrorPage(MultipartException.class, "/uploadError"));
    }
	public static void main(String[] args) {
	        SpringApplication.run(Application.class, args);	        
	    }
}
