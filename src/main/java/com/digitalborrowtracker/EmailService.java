package com.digitalborrowtracker;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EmailService {

    // EmailJS API URL
    private static final String EMAILJS_URL =
            "https://api.emailjs.com/api/v1.0/email/send";

    // EmailJS Service ID
    private static final String SERVICE_ID =
            "service_t36bcx4";

    // EmailJS Template ID
    private static final String TEMPLATE_ID =
            "template_f34nt9c";

    // EmailJS Public Key
    private static final String PUBLIC_KEY =
            "sql1uKWY-6XeBzcZz";

    public static void sendLoginEmail(String receiverEmail,
                                      String username) throws Exception {

        // Get EmailJS Private Key from Windows Environment Variable
        String privateKey =
                System.getenv("EMAILJS_PRIVATE_KEY");
        


        // Check whether the Private Key was loaded
        if (privateKey == null || privateKey.trim().isEmpty()) {

            throw new Exception(
                    "EMAILJS_PRIVATE_KEY environment variable was not found."
            );
        }

        // Email subject
        String subject =
                "Login Successful - Digital Borrow & Return Tracker";

        // Email message
        String message =
                "You have successfully logged in to "
                + "Digital Borrow & Return Tracker.\n\n"
                + "Your account login was successful.";

        // Create JSON request
        String json =
                "{"
                + "\"service_id\":\"" + SERVICE_ID + "\","
                + "\"template_id\":\"" + TEMPLATE_ID + "\","
                + "\"user_id\":\"" + PUBLIC_KEY + "\","
                + "\"accessToken\":\"" + escapeJson(privateKey) + "\","
                + "\"template_params\":{"
                + "\"name\":\"" + escapeJson(username) + "\","
                + "\"to_email\":\"" + escapeJson(receiverEmail) + "\","
                + "\"subject\":\"" + escapeJson(subject) + "\","
                + "\"message\":\"" + escapeJson(message) + "\""
                + "}"
                + "}";

        // Create URL
        URI uri = URI.create(EMAILJS_URL);
        URL url = uri.toURL();

        // Open connection
        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        // HTTP method
        connection.setRequestMethod("POST");

        // Request headers
        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        connection.setRequestProperty(
                "Accept",
                "application/json"
        );

        // Allow sending data
        connection.setDoOutput(true);

        // Send JSON request
        try (OutputStream outputStream =
                     connection.getOutputStream()) {

            outputStream.write(
                    json.getBytes(StandardCharsets.UTF_8)
            );
        }

        // Get response code
        int responseCode =
                connection.getResponseCode();

        System.out.println(
                "EmailJS Response Code: " + responseCode
        );

        // Read EmailJS response
        java.io.InputStream responseStream;

        if (responseCode >= 400) {

            responseStream =
                    connection.getErrorStream();

        } else {

            responseStream =
                    connection.getInputStream();
        }

        if (responseStream != null) {

            Scanner scanner =
                    new Scanner(
                            responseStream,
                            StandardCharsets.UTF_8
                    );

            String responseMessage =
                    scanner.useDelimiter("\\A").hasNext()
                            ? scanner.next()
                            : "";

            scanner.close();

            System.out.println(
                    "EmailJS Response: " + responseMessage
            );
        }

        // Check whether EmailJS accepted the request
        if (responseCode != 200) {

            throw new Exception(
                    "EmailJS failed. HTTP response code: "
                    + responseCode
            );
        }

        System.out.println(
                "Login email sent successfully through EmailJS!"
        );

        connection.disconnect();
    }

    // Escape special characters for JSON
    private static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}