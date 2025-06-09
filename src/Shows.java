public class Shows extends MediaItem {
    private int seasonsWatched;

    public Shows(String title, int year, String genre, double rating, int seasonsWatched) {
        super(title, year, genre, rating);
        this.seasonsWatched = seasonsWatched;
    }

    @Override
    public String getType() {
        return "Shows";
    }

    @Override
    public String toString() {
        return super.toString() + " ,Seasons Watched: + " + seasonsWatched;
    }

    public int getSeasonsWatched() {
        return seasonsWatched;
    }

    public void updateSeasonsWatched(int seasons) {
        this.seasonsWatched = seasons;
    }


}
