package dto;

import entities.Movie;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author carol
 */
public class MovieDTO {
    private int releaseYear;
    private String name;
    private String[] actors;
    
    public MovieDTO(Movie movie) {
        this.releaseYear = movie.getReleaseYear();
        this.name = movie.getName();
        this.actors = movie.getActors();
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getActors() {
        return actors;
    }

    public void setActors(String[] actors) {
        this.actors = actors;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MovieDTO other = (MovieDTO) obj;
        if (this.releaseYear != other.releaseYear) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Arrays.deepEquals(this.actors, other.actors)) {
            return false;
        }
        return true;
    }

    
}
