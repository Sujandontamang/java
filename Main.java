import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Main {

    public static void main(String[] args) {
        String urlString = "https://graphhopper.com/api/1/vrp?key=42d9dc52-d604-4ee7-b76d-1681bf5e1782";
        String jsonFilePath = "tsp.json";

        try {
            // Read the JSON file
            String json = readFile(jsonFilePath);

            // Create the URL object
            URL url = new URL(urlString);

            // Open the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Write the JSON data to the request body
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            // Send the request and get the response
            int responseCode = connection.getResponseCode();

            // Read the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    System.out.println("Response: " + response.toString());
                }
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }
}
