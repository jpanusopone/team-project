package data_access;

import use_case.link_risk.LinkRiskUserDataAccessInterface;
import use_case.link_risk.LinkRiskResult;
import use_case.link_risk.LinkRiskApiException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GoogleSafeBrowsingLinkRiskDataAccessObject implements LinkRiskUserDataAccessInterface {

    private final String apiKey;

    private static final String RISK_SAFE       = "SAFE";
    private static final String RISK_DANGEROUS  = "DANGEROUS";
    private static final String RISK_UNKNOWN    = "UNKNOWN";

    public GoogleSafeBrowsingLinkRiskDataAccessObject(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("apiKey must not be null or blank");
        }
        this.apiKey = apiKey;
    }

    @Override
    public List<LinkRiskResult> checkUrls(List<String> urls) throws LinkRiskApiException {
        if (urls == null) {
            throw new IllegalArgumentException("urls must not be null");
        }

        List<String> normalizedUrls = normalizeUrls(urls);

        JsonObject responseJson;
        try {
            responseJson = callSafeBrowsingApi(normalizedUrls);
        } catch (IOException | URISyntaxException e) {
            throw new LinkRiskApiException("Failed to call Safe Browsing API", e);
        }

        Map<String, String> riskMap = parseResponseIntoRiskMap(responseJson);

        List<LinkRiskResult> results = new ArrayList<>();
        for (String url : normalizedUrls) {
            String riskLevel = riskMap.getOrDefault(url, RISK_SAFE);
            results.add(new LinkRiskResult(url, riskLevel));
        }

        return results;
    }

    private List<String> normalizeUrls(List<String> urls) {
        return urls.stream()
                .map(url -> {
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        return url;
                    } else {
                        return "http://" + url;
                    }
                })
                .collect(Collectors.toList());
    }

    private JsonObject callSafeBrowsingApi(List<String> urls) throws IOException, URISyntaxException {
        String apiUrl = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=" + apiKey;
        URI uri = new URI(apiUrl);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        JsonObject requestBody = buildRequestBody(urls);
        String json = requestBody.toString();

        try (var os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Safe Browsing API returned HTTP code " + responseCode);
        }

        try (InputStreamReader reader = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)) {
            JsonElement element = JsonParser.parseReader(reader);
            return element.getAsJsonObject();
        }
    }

    private JsonObject buildRequestBody(List<String> urls) {
        JsonObject root = new JsonObject();

        JsonObject client = new JsonObject();
        client.addProperty("clientId", "your-client-id");
        client.addProperty("clientVersion", "1.0");
        root.add("client", client);

        JsonObject threatInfo = new JsonObject();

        JsonArray threatEntries = new JsonArray();
        for (String url : urls) {
            JsonObject entry = new JsonObject();
            entry.addProperty("url", url);
            threatEntries.add(entry);
        }
        threatInfo.add("threatEntries", threatEntries);

        JsonArray listTypes = new JsonArray();
        listTypes.add("MALWARE");
        listTypes.add("SOCIAL_ENGINEERING");
        listTypes.add("UNWANTED_SOFTWARE");
        listTypes.add("POTENTIALLY_HARMFUL_APPLICATION");
        threatInfo.add("threatTypes", listTypes);

        JsonArray platformTypes = new JsonArray();
        platformTypes.add("ANY_PLATFORM");
        threatInfo.add("platformTypes", platformTypes);

        JsonArray threatEntryTypes = new JsonArray();
        threatEntryTypes.add("URL");
        threatInfo.add("threatEntryTypes", threatEntryTypes);

        root.add("threatInfo", threatInfo);

        return root;
    }

    private Map<String, String> parseResponseIntoRiskMap(JsonObject responseJson) {
        Map<String, String> riskMap = new HashMap<>();
        if (responseJson.has("matches")) {
            JsonArray matches = responseJson.getAsJsonArray("matches");
            for (JsonElement element : matches) {
                JsonObject matchObj = element.getAsJsonObject();
                String rawUrl = matchObj.get("threat").getAsJsonObject().get("url").getAsString();
                String threatType = matchObj.get("threatType").getAsString();
                String riskLevel = mapThreatTypeToRiskLevel(threatType);
                riskMap.put(rawUrl, riskLevel);
            }
        }
        return riskMap;
    }

    private String mapThreatTypeToRiskLevel(String threatType) {
        if (threatType == null) {
            return RISK_UNKNOWN;
        }
        switch (threatType) {
            case "MALWARE":
            case "SOCIAL_ENGINEERING":
            case "UNWANTED_SOFTWARE":
            case "POTENTIALLY_HARMFUL_APPLICATION":
                return RISK_DANGEROUS;
            default:
                return RISK_UNKNOWN;
        }
    }
}
