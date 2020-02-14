package facades;

import dto.MovieDTO;
import dto.MovieListDTO;
import entities.Movie;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class MovieFacade {

    private static MovieFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private MovieFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public MovieListDTO getAllMovies() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Movie> q = em.createNamedQuery("Movie.getAll", Movie.class);
            return new MovieListDTO(q.getResultList());
        } finally {
            em.close();
        }
    }

    public MovieListDTO getByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Movie> q = em.createNamedQuery("Movie.getByName", Movie.class);
            q.setParameter("name", name);
            return new MovieListDTO(q.getResultList());
        } finally {
            em.close();
        }
    }

    public MovieDTO getById(int id) {
        EntityManager em = getEntityManager();
        try {
            Movie movie = em.find(Movie.class, id);
            return new MovieDTO(movie);
        } finally {
            em.close();
        }
    }
    
    public int getCount() {
        EntityManager em = getEntityManager();
        try {
            Long count = (Long) em.createQuery("SELECT COUNT(m) FROM Movie m")
                    .getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }
    
//    public static void main(String[] args) {
//        Movie m1 = new Movie(1994, "The Nightmare before Christmas", new String[]{"Ken Page", "Cathrine O'Hara", "Danny Elfman"});
//        Movie m2 = new Movie(2005, "Corpse Bride", new String[]{"Johnny Depp", "Helena Bonham Carter", "Emily Watson"});
//        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            em.persist(m1);
//            em.persist(m2);
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//    }
}

