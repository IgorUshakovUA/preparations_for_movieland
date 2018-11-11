package com.ushakov;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    //=== Initial load of source data
    private static List<String> userLines;
    private static List<String> posterLines;
    private static List<String> genreLines;
    private static List<String> movieLines;
    private static List<String> reviewLines;

    static {
        try {
            userLines = Files.readAllLines(Paths.get("user.txt"), Charset.forName("UTF-8"));
            posterLines = Files.readAllLines(Paths.get("poster.txt"), Charset.forName("UTF-8"));
            genreLines = Files.readAllLines(Paths.get("genre.txt"), Charset.forName("UTF-8"));
            movieLines = Files.readAllLines(Paths.get("movie.txt"), Charset.forName("UTF-8"));
            reviewLines = Files.readAllLines(Paths.get("review.txt"), Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static List<Poster> posterList = new ArrayList<>();
    private static List<User> userList = new ArrayList<>();
    private static List<Movie> movieList = new ArrayList<>();
    private static List<Country> countryList = new ArrayList<>();
    private static List<Genre> genreList = new ArrayList<>();
    private static List<Group> genreGroupeList = new ArrayList<>();
    private static List<Group> countryGroupeList = new ArrayList<>();

    private static int currentCountryId;
    private static int currentCountryGroupId;
    private static int currentGenreGroupId;

    public static void main(String[] args) throws Exception {
        processUser();

        processGenre();

        processMovie();

        processPoster();

        processReview();

        //=== Create movie.sql
        saveToFile("movie.sql", movieList);

        //==- Create country.sql
        saveToFile("country.sql", countryList);

        //=== Create countryGroup.sql
        saveToFile("countryGroup.sql", countryGroupeList);

        //=== Create genreGroup.sql
        saveToFile("genreGroup.sql", genreGroupeList);
    }

    private static void processUser() throws Exception {
        //=== User parsing

        int currentId = 0;
        int currentLineNumber = 0;
        while (currentLineNumber < userLines.size()) {
            User newUser = new User();
            // id
            currentId++;
            newUser.id = currentId;
            // name
            String currentLine;
            do {
                currentLine = cleanTextContent(userLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            newUser.name = currentLine;
            // email
            do {
                currentLine = cleanTextContent(userLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            newUser.email = currentLine;
            // nickName
            do {
                currentLine = cleanTextContent(userLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            newUser.nickName = currentLine;

            userList.add(newUser);
        }

        //=== Create user.sql
        saveToFile("user.sql", userList);
    }

    private static void processGenre() throws Exception {
        //=== Genre parsing
        int currentId = 0;
        int currentLineNumber = 0;
        while (currentLineNumber < genreLines.size()) {
            Genre newGenre = new Genre();
            // id
            currentId++;
            newGenre.id = currentId;
            // name
            String currentLine;
            do {
                currentLine = cleanTextContent(genreLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            newGenre.name = currentLine;

            genreList.add(newGenre);
        }

        //=== Create genre.sql
        saveToFile("genre.sql", genreList);
    }

    private static void processPoster() throws Exception {
        //=== Poster processing

        for (int i = 1; i <= posterLines.size(); i++) {
            Poster newPoster = new Poster();
            String currentLine = cleanTextContent(posterLines.get(i - 1));
            newPoster.id = i;
            int splitIndex = currentLine.indexOf("https:") - 1;
            String nameRussian = currentLine.substring(0, splitIndex);
            newPoster.picturePath = currentLine.substring(splitIndex + 1);
            for (Movie movie : movieList) {
                if (movie.nameRussian.equalsIgnoreCase(nameRussian)) {
                    movie.posterId = i;
                    break;
                }
            }

            posterList.add(newPoster);
        }

        //=== Create poster.sql
        saveToFile("poster.sql", posterList);
    }

    private static void processReview() throws Exception {
        //== Review processing
        List<Review> reviewList = new ArrayList<>();
        int currentId = 0;
        int currentLineNumber = 0;
        while (currentLineNumber < reviewLines.size()) {
            Review newReview = new Review();
            // id
            currentId++;
            newReview.id = currentId;
            String currentLine;
            // movieId
            do {
                currentLine = cleanTextContent(reviewLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            for (Movie movie : movieList) {
                if (movie.nameRussian.equalsIgnoreCase(currentLine)) {
                    newReview.movieId = movie.id;
                    break;
                }
            }
            // userId
            do {
                currentLine = cleanTextContent(reviewLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            for (User user : userList) {
                if (user.name.equalsIgnoreCase(currentLine)) {
                    newReview.userId = user.id;
                    break;
                }
            }
            // comment
            do {
                currentLine = cleanTextContent(reviewLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            newReview.comment = currentLine;

            reviewList.add(newReview);
        }

        //=== Create review.sql
        saveToFile("review.sql", reviewList);
    }

    private static void processMovie() {
        //=== Movies & Country parsing
        int currentId = 1;
        int currentLineNumber = 0;
        int countryCurrentId = 0;
        while (currentLineNumber < movieLines.size()) {
            Movie newMovie = new Movie();
            Country newCountry = new Country();
            // id
            currentId++;
            newMovie.id = currentId;
            // names
            String currentLine;
            do {
                currentLine = cleanTextContent(movieLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            String[] names = currentLine.split("/");
            newMovie.nameRussian = names[0];
            newMovie.nameNative = names[1].replace("'", "''");
            // year
            do {
                currentLine = cleanTextContent(movieLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            newMovie.yearOfRelease = Integer.parseInt(currentLine);
            // Country
            do {
                currentLine = cleanTextContent(movieLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            newMovie.countryGroupId = processCountryGroup(currentLine);
            // Genre
            do {
                currentLine = cleanTextContent(movieLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            newMovie.genreGroupId = processGenreGroup(currentLine);
            // Description
            do {
                currentLine = cleanTextContent(movieLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            newMovie.description = currentLine.replace("'", "''");
            // Rating
            do {
                currentLine = cleanTextContent(movieLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            String[] rating = currentLine.split(":");
            newMovie.rating = Double.parseDouble(rating[1]);
            // Price
            do {
                currentLine = cleanTextContent(movieLines.get(currentLineNumber));
                currentLineNumber++;
            } while (currentLine.length() == 0);
            String[] price = currentLine.split(":");
            newMovie.price = Double.parseDouble(price[1]);

            movieList.add(newMovie);
        }
    }

    private static int processCountryGroup(String countries) {
        Group newGroup = new Group();
        newGroup.tableName = "countryGroup";
        newGroup.fieldName = "countryId";
        newGroup.idList = new ArrayList<>();
        for (String string : countries.split(",")) {
            String name = string.trim();
            if (name != null) {
                newGroup.idList.add(processCountry(name));
            }
        }

        for (Group group : countryGroupeList) {
            if (group.equals(newGroup)) {
                return group.id;
            }
        }

        currentCountryGroupId++;
        newGroup.id = currentCountryGroupId;
        countryGroupeList.add(newGroup);

        return currentCountryGroupId;
    }

    private static int processCountry(String name) {
        for (Country country : countryList) {
            if (country.name.equalsIgnoreCase(name)) {
                return country.id;
            }
        }
        currentCountryId++;
        Country newCountry = new Country();
        newCountry.id = currentCountryId;
        newCountry.name = name;
        countryList.add(newCountry);
        return currentCountryId;
    }

    private static int processGenreGroup(String genres) {
        Group newGroup = new Group();
        newGroup.tableName = "genreGroup";
        newGroup.fieldName = "genreId";
        newGroup.idList = new ArrayList<>();
        for (String string : genres.split(",")) {
            String name = string.trim();
            if (name != null) {
                newGroup.idList.add(processGenre(name));
            }
        }

        for (Group group : genreGroupeList) {
            if (group.equals(newGroup)) {
                return group.id;
            }
        }

        currentGenreGroupId++;
        newGroup.id = currentGenreGroupId;
        genreGroupeList.add(newGroup);

        return currentGenreGroupId;
    }

    private static int processGenre(String name) {
        for (Genre genre : genreList) {
            if (genre.name.equalsIgnoreCase(name)) {
                return genre.id;
            }
        }
        return 0;
    }

    private static <T> void saveToFile(String fileName, List<T> list) throws Exception {
        try (FileWriter fileWriter = new FileWriter(Paths.get(fileName).toFile());) {
            for (T element : list) {
                fileWriter.write(element.toString());
                fileWriter.write("\n");
            }

        }
    }

    private static String cleanTextContent(String text) {
        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }
}
