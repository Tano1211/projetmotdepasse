package ci.univmetiers.passgen;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Gère la communication HTTP avec le conteneur Docker de validation.
 */
public class DockerClient {
    // Port 8080 correspondant à notre serveur Java interne
    private static final String API_URL = "http://localhost:8080/validate";
    private final HttpClient client;

    public DockerClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public String evaluatePassword(String password) {
        try {
            // Échappement des caractères pour préserver le format JSON
            String safePassword = password.replace("\\", "\\\\").replace("\"", "\\\"");
            String jsonPayload = "{\"password\":\"" + safePassword + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            
            if (body.contains("\"label\":\"")) {
                return body.split("\"label\":\"")[1].split("\"")[0];
            }
            return "Indéterminé";

        } catch (Exception e) {
            return "Erreur de validation (Conteneur injoignable)";
        }
    }
}