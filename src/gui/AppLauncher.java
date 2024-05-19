package gui;

import javax.swing.SwingUtilities;

public class AppLauncher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new WeatherAppGUI().setVisible(true);
				
				//TEST DATE AND TIME FORMATTER
				//System.out.println(WeatherApp.getCurrentTime());
			}
		});
	}
}
