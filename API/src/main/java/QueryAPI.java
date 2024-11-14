import static spark.Spark.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryAPI {
    private final InvertedIndexLoader indexLoader;
    private final int port;

    public QueryAPI(String basePath, int port) {
        this.indexLoader = new InvertedIndexLoader(basePath);
        this.port = port;
    }

    // Método para iniciar el servidor y configurar el endpoint
    public void startServer() {
        port(this.port);

        // Configuración del endpoint de búsqueda
        get("/search", (req, res) -> {
            String wordParam = req.queryParams("word");
            if (wordParam == null || wordParam.isEmpty()) {
                res.status(400);
                return "Parameter 'word' is required";
            }

            Map<String, Object> results = processSearch(wordParam);

            // Configurar el tipo de respuesta como JSON
            res.type("application/json");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(results); // Convertir el mapa completo a JSON
        });
    }

    // Método que procesa la búsqueda en el índice usando una lista de palabras
    private Map<String, Object> processSearch(String wordParam) throws IOException {
        // Separar las palabras por espacios o por el signo '+'
        List<String> words = Arrays.stream(wordParam.split("\\s+|\\+"))
                .map(String::trim)
                .collect(Collectors.toList());

        // Crear un mapa para almacenar los resultados de cada palabra
        Map<String, Object> results = new HashMap<>();

        for (String word : words) {
            // Buscar la palabra en el índice
            Map<String, Object> wordResult = indexLoader.searchWord(word);
            if (wordResult != null) {
                results.put(word, wordResult);
            } else {
                results.put(word, "Not found"); // Si no se encuentra la palabra, indicar "Not found"
            }
        }
        return results;
    }
}
