import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class InvertedIndexLoader {
    private final String basePath;

    public InvertedIndexLoader(String basePath) {
        this.basePath = basePath;
    }

    public Map<String, Object> searchWord(String word) throws IOException {
        // Determinar el subdirectorio según la primera letra de la palabra
        String subDir = getSubdirectory(word);
        if (subDir == null) {
            return null; // Si no existe subdirectorio para esta palabra, devolvemos null
        }

        // Construir la ruta completa al archivo JSON de la palabra
        String filePath = basePath + File.separator + "English" + File.separator + subDir + File.separator + word + ".json";
        File jsonFile = new File(filePath);

        // Verificar si el archivo existe
        if (!jsonFile.exists()) {
            return null; // Si el archivo no existe, devolvemos null
        }

        // Leer el archivo JSON y devolver su contenido como un Map
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonFile, Map.class);
    }

    private String getSubdirectory(String word) {
        char firstChar = Character.toUpperCase(word.charAt(0)); // Tomamos la primera letra y la convertimos a mayúscula
        if (firstChar >= 'A' && firstChar <= 'D') {
            return "A-D";
        } else if (firstChar >= 'E' && firstChar <= 'H') {
            return "E-H";
        } else if (firstChar >= 'I' && firstChar <= 'L') {
            return "I-L";
        } else if (firstChar >= 'M' && firstChar <= 'P') {
            return "M-P";
        } else if (firstChar >= 'Q' && firstChar <= 'T') {
            return "Q-T";
        } else if (firstChar >= 'U' && firstChar <= 'Z') {
            return "U-Z";
        } else {
            return null; // Retorna null si la letra no está en el rango A-Z
        }
    }
}
