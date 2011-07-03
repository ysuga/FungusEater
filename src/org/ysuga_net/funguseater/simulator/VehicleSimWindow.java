package org.ysuga_net.funguseater.simulator;
import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class VehicleSimWindow extends JFrame {

	private VehicleSimPanel panel;


	public VehicleSimWindow() throws HeadlessException {
		super("VehicleSim 1.0");
		
		initPanel();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
	}

	public void start() {
		panel.startAutoRepaint();
	}
	
	public void stop() {
		panel.stopAutoRepaint();
	}
	
	
	private void initPanel() {
		getContentPane().setLayout(new BorderLayout());
		panel = new VehicleSimPanel();
		//getContentPane().add(BorderLayout.CENTER, 
		//		new JScrollPane(panel));
		getContentPane().add(BorderLayout.CENTER, panel);
	}
	
	
	public Vehicle getVehicle() {
		return panel.getVehicle();
	}

	
	public static void main (String[] arg) {
		new VehicleSimWindow();
	}
}
