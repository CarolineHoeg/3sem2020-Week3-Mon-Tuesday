package facades;

import dto.MovieDTO;
import dto.MovieListDTO;
import utils.EMF_Creator;
import entities.Movie;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;
import static org.junit.jupiter.api.Assertions.*;

public class MovieFacadeTest {

    private static EntityManagerFactory emf;
    private static MovieFacade facade;
    private Movie m1;
    private Movie m2;

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST,Strategy.DROP_AND_CREATE);
       facade = MovieFacade.getMovieFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
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
    public void testGetAllMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(m1);
        movies.add(m2);
        MovieListDTO exp = new MovieListDTO(movies);
        
        MovieListDTO result = facade.getAllMovies();
        
        assertEquals(2, result.getMovies().size());
        assertTrue(result.getMovies().containsAll(exp.getMovies()));
    }
    
    @Test
    public void testGetByName() {
        List<Movie> movies = new ArrayList<>();
        movies.add(m1);
        MovieListDTO exp = new MovieListDTO(movies);
        
        MovieListDTO result = facade.getByName(m1.getName());
        
        assertEquals(1, result.getMovies().size());
        assertTrue(result.getMovies().containsAll(exp.getMovies()));
    }

    @Test
    public void testGetById() {
        MovieDTO exp = new MovieDTO(m1);
        MovieDTO result = facade.getById(m1.getId());
        assertEquals(exp, result);
    }
    
    @Test
    public void testGetCount() {
        int result = facade.getCount();
        assertEquals(2, result);
    }
    
}
