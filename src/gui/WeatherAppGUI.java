package gui;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

import backend.WeatherApp;

public class WeatherAppGUI extends JFrame {
	public WeatherAppGUI() {
		//Creating window with title Weather
		super("Weather");
		
		//Setting properties of the window
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(450, 550);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setResizable(false);
		
		addGUIComponents();
	}
	
	private void addGUIComponents() {
		//Search text field
		JTextField txtSearch = new JTextField();
		txtSearch.setBounds(15, 15, 351, 45);
		txtSearch.setFont(new Font("Dialog", Font.PLAIN, 24));
		this.add(txtSearch);
		
		//Search button
		JButton btnSearch = new JButton(loadImg("assets/search.png"));
		btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSearch.setBounds(375, 13, 47, 45);
		this.add(btnSearch);
		
		//Weather condition image
		JLabel imgWeatherCondition = new JLabel(loadImg("assets/clear.png"));
		imgWeatherCondition.setBounds(0, 125, 450, 217);
		this.add(imgWeatherCondition);
		
		//Temperature text
		JLabel txtTemperature = new JLabel("0 ºC");
		txtTemperature.setBounds(0, 350, 450, 217);
		txtTemperature.setFont(new Font("Dialog", Font.BOLD, 48));
		txtTemperature.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(txtTemperature);
		
		//Weather condition label
		JLabel txtWeatherCondition = new JLabel("Sunny");
		txtWeatherCondition.setBounds(0, 405, 450, 36);
		txtWeatherCondition.setFont(new Font("Dialog", Font.PLAIN, 32));
		txtWeatherCondition.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(txtWeatherCondition);
		
		//Humidity image
		//JLabel imgHumidity = new JLabel(loadImg("assets/humidity.png"));
		//imgHumidity.setBounds(15, 500, 74, 66);
		//this.add(imgHumidity);
		
		//Humidity text
		//JLabel txtHumidity = new JLabel("Humidity 100%");
		//txtHumidity.setBounds(90, 500, 120, 55);
		//txtHumidity.setFont(new Font("Dialog", Font.PLAIN, 16));
		//this.add(txtHumidity);
		
		//JLabel imgWindSpeed = new JLabel(loadImg("assets/windspeed.png"));
		//imgWindSpeed.setBounds(220, 500, 74, 66);
		//this.add(imgWindSpeed);
		
		//JLabel txtWindSpeed = new JLabel("Wind Speed");
		//txtWindSpeed.setBounds(310, 500, 120, 55);
		//txtWindSpeed.setFont(new Font("Dialog", Font.PLAIN, 16));
		//this.add(txtWindSpeed);
		
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String input = txtSearch.getText();
				if(input.replace("\\s", "").length() <= 0) {
					return;
				}
				
				JSONObject weatherData = WeatherApp.getWeatherData(input);
				
				double temperature = (double) weatherData.get("temperature");
				txtTemperature.setText(temperature + "ºC");
				
				String weatherCondition = (String) weatherData.get("weather_condition");
				txtWeatherCondition.setText(weatherCondition);
				
				switch(weatherCondition) {
					case "Clear":
					case "Mostly Clear":
						imgWeatherCondition.setIcon(loadImg("assets/clear.png"));
						break;
					case "Partly Cloudy":
					case "Overcast":
					case "Fog":
						imgWeatherCondition.setIcon(loadImg("assets/cloudy.png"));
						break;
					case "Rain":
					case "Showers":
					case "Thunderstorms":
						imgWeatherCondition.setIcon(loadImg("assets/rain.png"));
						break;
					case "Snow":
						imgWeatherCondition.setIcon(loadImg("assets/snow.png"));
						break;
				}
			}
		});
	}
	
	private ImageIcon loadImg(String path) {
		try {
			BufferedImage img = ImageIO.read(new File(path));
			return new ImageIcon(img);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Could not load resource: " + path);
			return null;
		}
	}
}
