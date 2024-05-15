package gui;

import javax.swing.SwingUtilities;

import backend.WeatherApp;

public class AppLauncher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//new WeatherAppGUI().setVisible(true);
				
				//TEST API CALL
				System.out.println(WeatherApp.getLocationData("Rio de Janeiro"));
			}
		});
	}
}
