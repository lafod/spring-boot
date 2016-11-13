package formsubmit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "ssl.config")
public class SSLConfigProperties {

	private int serverPort;
	private String scheme;
	private String alias;
	private String keystorePassword;
	private Resource keystorefile;
	private String sslProtocol;
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getKeystorePassword() {
		return keystorePassword;
	}
	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}
	public Resource getKeystorefile() {
		return keystorefile;
	}
	public void setKeystorefile(String keystorefile) {
		this.keystorefile = new DefaultResourceLoader().getResource(keystorefile);
	}
	public String getSslProtocol() {
		return sslProtocol;
	}
	public void setSslProtocol(String sslProtocol) {
		this.sslProtocol = sslProtocol;
	}
	
	
}
