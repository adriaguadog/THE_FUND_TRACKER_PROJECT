package org.example.fund_tracker_project.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fund_tracker_project.model.Activo;
import org.example.fund_tracker_project.model.TipoActivo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GestorAPI {

    private HttpClient client;

    public List<Activo> llamarAPI() throws IOException, InterruptedException {
        String url = "https://api.twelvedata.com/etf?country=United%20States&source=docs";
        HttpRequest request= HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response= client.send(request,HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode dataNode = root.get("data");

        //mapeo
        List<Activo> activos =
                mapper.readValue(dataNode.toString(),
                        new TypeReference<List<Activo>>() {});
        for (Activo a : activos) {
            a.setTipoActivo(TipoActivo.ETF);
        }
        return activos;
    }

    public GestorAPI() {
        client= HttpClient.newHttpClient();
    }
}
