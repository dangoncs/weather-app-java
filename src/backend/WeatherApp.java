package backend;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {

	public static JSONObject getWeatherData(String locationName) {
		JSONArray locationData = getLocationData(locationName);
		
		//Get the first location that is returned by the API
		JSONObject location = (JSONObject) locationData.get(0);
		
		//Extract coordinates from the location
		double latitude = (double) location.get("latitude");
		double longitude = (double) location.get("longitude");
		
		//API request with coordinates
		String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,weather_code";
		
		try {
			HttpURLConnection conn = fetchAPIResponse(urlString);
			if(conn.getResponseCode() != 200) {
				System.out.println("ERROR: could not connect to API");
				System.out.println("RESPONSE CODE: " + conn.getResponseCode() + " " + conn.getResponseMessage());
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
			
			JSONObject hourly = (JSONObject) resultsJSONObj.get("hourly");
			
			JSONArray time = (JSONArray) hourly.get("time");
			int index = findIndexOfCurrentTime(time);
			
			JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
			double currentTemperature = (double) temperatureData.get(index);
			
			JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
			long currentWeatherCode = (long) weatherCode.get(index);
			String currentWeatherCondition = convertWeatherCode(currentWeatherCode);
			
			JSONObject weatherData = new JSONObject();
			weatherData.put("temperature", currentTemperature);
			weatherData.put("weather_condition", currentWeatherCondition);
			//weatherData.put("humidity", currentHumidity);
			//weatherData.put("windspeed", windspeed);
			
			return weatherData;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static JSONArray getLocationData(String locationName) {
		locationName = locationName.replaceAll(" ", "+");
		String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";
		
		try {
			HttpURLConnection conn = fetchAPIResponse(urlString);
			if(conn.getResponseCode() != 200) {
				System.out.println("ERROR: could not connect to API");
				System.out.println("RESPONSE CODE: " + conn.getResponseCode() + " " + conn.getResponseMessage());
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
	
	private static int findIndexOfCurrentTime(JSONArray timeList) {
		String currentTime = getCurrentTime();
		
		for(int i = 0; i < timeList.size(); i++) {
			String time = (String) timeList.get(i);
			if(time.equals(currentTime)) {
				return i;
			}
		}
		return -1;
	}

	private static String getCurrentTime() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
		String formattedDateTime = currentDateTime.format(formatter);
		return formattedDateTime;
	}
	
	private static String convertWeatherCode(long weatherCode) {
		//switch(weatherCode)
		
		
		if(weatherCode == 0L) {
			return "Clear";
		}
		else if(weatherCode == 1L) {
			return "Mostly Clear";
		}
		else if(weatherCode == 2L) {
			return "Partly Cloudy";
		}
		else if(weatherCode == 3L) {
			return "Overcast";
		}
		else if(weatherCode >= 45L && weatherCode <= 48L) {
			return "Fog";
		}
		else if(weatherCode >= 51L && weatherCode <= 67L) {
			return "Rain";
		}
		else if(weatherCode >= 71L && weatherCode <= 77L) {
			return "Snow";
		}
		else if(weatherCode >= 80L && weatherCode <= 86L) {
			return "Showers";
		}
		else if(weatherCode >= 95L && weatherCode <= 99L) {
			return "Thunderstorms";
		}
		
		return null;
	}
}
