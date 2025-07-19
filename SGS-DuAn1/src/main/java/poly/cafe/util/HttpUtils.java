package poly.cafe.util;

public class HttpUtils {

    public static String sendGet(String url, String apiKey) {
        try {
            java.net.URL obj = new java.net.URL(url);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("apikey", apiKey);
            con.setRequestProperty("Accept", "application/json");

            java.io.BufferedReader in = new java.io.BufferedReader(
                    new java.io.InputStreamReader(con.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    public static String sendPost(String url, String json, String apiKey) {
        try {
            java.net.URL obj = new java.net.URL(url);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("apikey", apiKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            try (java.io.OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            java.io.BufferedReader in = new java.io.BufferedReader(
                    new java.io.InputStreamReader(con.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

public static String sendPatch(String url, String json, String apiKey) {
    try {
        // In ra URL đầy đủ để debug
        System.out.println("[DEBUG] Full PATCH URL: " + url);

        java.net.URL obj = new java.net.URL(url);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST"); // PATCH override
        con.setRequestProperty("X-HTTP-Method-Override", "PATCH");

        con.setRequestProperty("apikey", apiKey);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (java.io.OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        java.io.BufferedReader in = new java.io.BufferedReader(
            new java.io.InputStreamReader(con.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    } catch (Exception e) {
        e.printStackTrace();
        return "";
    }
}

    public static String sendDelete(String url, String apiKey) {
        try {
            java.net.URL obj = new java.net.URL(url);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("apikey", apiKey);
            con.setRequestProperty("Accept", "application/json");
            int responseCode = con.getResponseCode();
            return String.valueOf(responseCode);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
