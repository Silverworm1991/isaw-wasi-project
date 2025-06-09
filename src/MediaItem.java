public abstract class MediaItem {

    protected String title;
    protected int year;
    protected String genre;
    protected double rating;
    protected boolean watched;

    public MediaItem(String title, int year, String genre, double rating) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.rating = rating;
        this.watched = false;
    }

    public abstract String getType();

    public void markAsWatched() {
        this.watched = true;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isWatched() {
        return watched;
    }

    public String toString() {
        return "[" + getType() + "]" + title + " (" + year + ") - " + genre + ", Rating: " + rating + ", Watched: " + (watched ? "Yes" : "No");
    }
}
