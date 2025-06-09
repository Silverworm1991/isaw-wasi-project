import java.util.ArrayList;
import java.util.List;

public class MediaLibrary {

    private List<MediaItem> mediaItems;

    public MediaLibrary() {
        this.mediaItems = new ArrayList<>();
    }

    public void addItem(MediaItem item) {
        mediaItems.add(item);
        System.out.println("Added " + item.getTitle() + " with sucess.");
    }

    public void listALL() {
        if (mediaItems.isEmpty()) {
            System.out.println("There is no data in the arquive");
        }
        for (MediaItem item : mediaItems) {
            System.out.println(item);
        }
    }

    public void searchByTitle(String keyword) {
        boolean found = false;
        for (MediaItem item : mediaItems) {
            if (item.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(item);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No result was found for: " + keyword);
        }
    }

    public void filteredByGenre(String genre) {
        boolean found = false;
        for (MediaItem item : mediaItems) {
            if (item.getGenre().equalsIgnoreCase(genre)) {
                System.out.println(item);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No data associated with genre: " + genre);
        }
    }

    public void markAsWatched(String title) {
        for (MediaItem item : mediaItems) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                item.markAsWatched();
                System.out.println("Saved as viewed : " + item.getTitle());
                return;
            }
        }
        System.out.println("Title not found : " + title);
    }

    public int getWatchedCount() {
        int count = 0;
        for (MediaItem item : mediaItems) {
            if (item.isWatched()) {
                count++;
            }
        }
        return count;
    }


}


