package formsubmit.config;

import java.io.IOException;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;

import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
public class SslConfig {
	
	private SSLConfigProperties sslConfigProperty;
	@Autowired
	public SslConfig(SSLConfigProperties sslConfigProperty) {
		this.sslConfigProperty = sslConfigProperty; 
	}
	//@Bean
    public EmbeddedServletContainerFactory servletContainer() throws IOException {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(createSslConnector());
		//JettyEmbeddedServletContainerFactory jetty = new JettyEmbeddedServletContainerFactory();
		//jetty.addServerCustomizers(customizers);
        return tomcat;
    }

    private Connector createSslConnector() throws IOException {
        Connector connector = new Connector(Http11NioProtocol.class.getName());
        Http11NioProtocol protocol =
                (Http11NioProtocol) connector.getProtocolHandler();
        connector.setPort(sslConfigProperty.getServerPort());
        connector.setSecure(true);
        connector.setScheme(sslConfigProperty.getScheme());
        protocol.setSSLEnabled(true);
        protocol.setKeyAlias(sslConfigProperty.getAlias());
        protocol.setKeystorePass(sslConfigProperty.getKeystorePassword());
        protocol.setKeyPass(sslConfigProperty.getKeystorePassword());
        protocol.setKeystoreFile(sslConfigProperty.getKeystorefile().getFile().getAbsolutePath());        
        protocol.setSslProtocol(sslConfigProperty.getSslProtocol());
        return connector;
    }
}
