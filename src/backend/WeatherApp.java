package backend;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {

	public static JSONObject getWeatherData(String locationName) {
		JSONArray locationData = getLocationData(locationName);
		return null;
	}
	
	//CHANGE LATER TO PRIVATE
	public static JSONArray getLocationData(String locationName) {
		locationName = locationName.replaceAll(" ", "+");
		String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";
		
		try {
			HttpURLConnection conn = fetchAPIResponse(urlString);
			if(conn.getResponseCode() != 200) {
				System.out.println("ERROR: could not connect to API");
				System.out.println("RESPONSE CODE " + conn.getResponseCode() + conn.getResponseMessage());
				return null;
			}
			
			StringBuilder resultJSON = new StringBuilder();
			Scanner scanner = new Scanner(conn.getInputStream());
			
			while(scanner.hasNext()) {
				resultJSON.append(scanner.nextLine());
			}
			
			//These will not be used anymore, close to avoid wasting resources
			scanner.close();
			conn.disconnect();
			
			JSONParser parser = new JSONParser();
			JSONObject resultsJSONObj = (JSONObject) parser.parse(String.valueOf(resultJSON));
			
			JSONArray locationData = (JSONArray) resultsJSONObj.get("results");
			return locationData;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static HttpURLConnection fetchAPIResponse(String urlString) {
		try {
			URL url = new URL(urlString);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			return conn;
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}
}
