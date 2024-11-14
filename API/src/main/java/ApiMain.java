public class ApiMain {
    private  static final String jsonDatamartPath = "C:\\Users\\cgsos\\Documents\\Tercero\\Big Data\\JavaSearchEngine\\SearchEngine\\jsonDatamart";
    public static void main(String[] args) {
        QueryAPI queryAPI = new QueryAPI(jsonDatamartPath, 4567);
        queryAPI.startServer();

    }
}
