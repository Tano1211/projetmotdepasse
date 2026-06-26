import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ValidatorServer {

    public static void main(String[] args) throws IOException {
        // Création d'un serveur HTTP natif écoutant sur le port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/validate", new ValidateHandler());
        server.setExecutor(null);
        System.out.println("Conteneur de validation Java démarré sur le port 8080...");
        server.start();
    }

    static class ValidateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                // Extraction basique du mot de passe depuis le JSON {"password":"..."}
                String password = extractPassword(body);
                String scoreLabel = evaluatePassword(password);

                // Construction de la réponse JSON
                String response = "{\"label\":\"" + scoreLabel + "\"}";
                
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }

        private String extractPassword(String json) {
            try {
                String[] parts = json.split("\"password\":\"");
                return parts[1].substring(0, parts[1].indexOf("\""));
            } catch (Exception e) {
                return "";
            }
        }

        private String evaluatePassword(String password) {
            int length = password.length();
            int poolSize = 0;

            if (password.matches(".*[a-z].*")) poolSize += 26;
            if (password.matches(".*[A-Z].*")) poolSize += 26;
            if (password.matches(".*[0-9].*")) poolSize += 10;
            if (password.matches(".*[^a-zA-Z0-9].*")) poolSize += 32;

            if (poolSize == 0 || length == 0) return "Très faible";

            // Calcul de l'entropie
            double entropy = length * (Math.log(poolSize) / Math.log(2));

            if (entropy < 28) return "Très faible";
            if (entropy < 36) return "Faible";
            if (entropy < 60) return "Moyen";
            if (entropy < 100) return "Fort";
            return "Très fort";
        }
    }
}