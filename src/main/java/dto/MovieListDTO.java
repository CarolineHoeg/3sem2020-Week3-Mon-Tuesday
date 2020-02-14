package dto;

import entities.Movie;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author carol
 */
public class MovieListDTO {

    private List<MovieDTO> movies = new ArrayList<>();

    public MovieListDTO(List<Movie> movies) {
        for (Movie movie : movies) {
            this.movies.add(new MovieDTO(movie));
        }
    }

    public List<MovieDTO> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieDTO> movies) {
        this.movies = movies;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

}
