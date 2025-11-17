package service;

import model.DirectFlight;
import org.json.*;

import java.net.*;
import java.io.*;
import java.util.*;

public class FlightAPIService {

    public static List<DirectFlight> getDirectFlights(String origin, String destination)
            throws Exception {

        String url = "https://opensky-network.org/api/routes?dep=" + origin + "&arr=" + destination;

        URL u = new URL(url);
        BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));

        String json = br.readLine();
        JSONArray arr = new JSONArray(json);

        List<DirectFlight> result = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);

            result.add(new DirectFlight(
                    o.getString("callsign"),
                    origin,
                    destination,
                    o.optString("icao24")
            ));
        }

        return result;
    }
}
