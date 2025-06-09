public class Movie extends MediaItem {

    public Movie(String title, int year, String genre, double rating) {
        super(title, year, genre, rating);
    }

    @Override
    public String getType() {
        return "Movie";
    }

    @Override
    public String toString() {
        return "";
    }
}
