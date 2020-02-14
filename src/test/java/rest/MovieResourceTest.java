package rest;

import entities.Movie;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

public class MovieResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    //Read this line from a settings-file  since used several places
    private static final String TEST_DB = "jdbc:mysql://localhost:3307/startcode";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private Movie m1;
    private Movie m2;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.CREATE);

        //Set System property so the project executed by the Grizly-server wil use this same database
        System.setProperty("IS_TEST", TEST_DB);
        httpServer = startServer();

        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        m1 = new Movie(1994, "The Nightmare before Christmas", 
                new String[]{"Ken Page", "Cathrine O'Hara", "Danny Elfman"});
        m2 = new Movie(2005, "Corpse Bride", 
                new String[]{"Johnny Depp", "Helena Bonham Carter", "Emily Watson"});
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.persist(m1);
            em.persist(m2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerConnection() {
        given().when()
                .get("/movie").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }
    
    @Test
    public void testGetCount() {
        given().when()
                .get("/movie/count").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body(is("2"));
    }

    @Test
    public void testGetAll() {
        given().when()
                .get("/movie/all").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("movies", hasSize(2))
                .body("movies.name", hasItems(m1.getName(), m2.getName()))
                .body("movies.releaseYear", hasItems(m1.getReleaseYear(),
                        m2.getReleaseYear()));
    }

    @Test
    public void testGetByName() {
        given().when()
                .get("/movie/name/{name}", m2.getName()).
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("movies.name", hasItem(m2.getName()))
                .body("movies.releaseYear", hasItem(m2.getReleaseYear()));
    }
    
    @Test 
    public void testGetById() {
        given().when()
                .get("/movie/{id}", m1.getId()).
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", is(m1.getName()))
                .body("releaseYear", is(m1.getReleaseYear()));
    }
}
