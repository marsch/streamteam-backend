package streamteam.web;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

public class Webservice {
	
	

	private static URI getBaseURI(String host, int port) {
		return UriBuilder.fromUri("http://" + host + "/").port(port).build();
	}

	public static HttpServer startServer(String host, int port)
			throws IllegalArgumentException, NullPointerException, IOException {

		return GrizzlyServerFactory.createHttpServer(getBaseURI(host, port),
				createResourceConfig());
	}

	public static ResourceConfig createResourceConfig() {
		ResourceConfig rc = new PackagesResourceConfig(
				"streamteam.web.resources");

		return rc;
	}


	public static void main(String[] args) {
		HttpServer httpServer;
		try {
			httpServer = Webservice.startServer("0.0.0.0", 3000);
			httpServer.start();
			
			System.in.read();
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
