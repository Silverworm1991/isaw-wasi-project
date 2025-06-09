import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        MediaLibrary mediaLibrary = new MediaLibrary();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            System.out.println("\n I saw, was 1 📺");
            System.out.println("1. Add Movie");
            System.out.println("2. Add Show");
            System.out.println("3. List all");
            System.out.println("4. Search by Title");
            System.out.println("5. Filter by Genre");
            System.out.println("6. Check as Watched");
            System.out.println("7. Check all of the Media available");
            System.out.println("0. Leave Program");
            System.out.println("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); //Consumes the newline

            switch (option) {
                case 1:
                    System.out.println("Movie's title: ");
                    String movieTitle = scanner.nextLine();
                    System.out.println("Year: ");
                    int movieYear = scanner.nextInt();
                    System.out.println("Genre :");
                    String movieGenre = scanner.nextLine();
                    System.out.println("Rating (0-10): ");
                    double movieRating = scanner.nextDouble();
                    scanner.nextLine();
                    Movie movie = new Movie(movieTitle, movieYear, movieGenre, movieRating);
                    mediaLibrary.addItem(movie);
                    break;

                case 2:
                    System.out.println("Show's title: ");
                    String showTitle = scanner.nextLine();
                    System.out.println("Year: ");
                    int showYear = scanner.nextInt();
                    System.out.println("Genre :");
                    String showGenre = scanner.nextLine();
                    System.out.println("Rating (0-10): ");
                    double showRating = scanner.nextDouble();
                    System.out.println("Seasons's watched: ");
                    int seasonsWatched = scanner.nextInt();
                    scanner.nextLine();
                    Shows show = new Shows(showTitle, showYear, showGenre, showRating, seasonsWatched);
                    mediaLibrary.addItem(show);
                    break;

                case 3:
                    mediaLibrary.listALL();
                    break;

                case 4:
                    System.out.println("Search by Title keywords: ");
                    String keyword = scanner.nextLine();
                    mediaLibrary.searchByTitle(keyword);
                    break;

                case 5:
                    System.out.println("Genre: ");
                    String genre = scanner.nextLine();
                    mediaLibrary.filteredByGenre(genre);
                    break;

                case 6:
                    System.out.println("Enter the Title to mark as watched: ");
                    String mediaToMark = scanner.nextLine();
                    mediaLibrary.markAsWatched(mediaToMark);
                    break;


                case 7:
                    System.out.println("Total of Watched Titles: " + mediaLibrary.getWatchedCount());
                    break;

                case 0:
                    running = false;
                    System.out.println("So long...Until next time!");
                    break;

                default:
                    System.out.println("Invalid option! Try again.");

            }
            scanner.close();
        }


    }
}