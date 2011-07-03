package org.ysuga_net.funguseater.simulator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class VehicleSimPanel extends JPanel {

	static Random random = new Random();

	private Vehicle vehicle;

	public VehicleSimPanel() {
		super();
		vehicle = new Vehicle(this, SimParam.getINIT_X(), SimParam.getINIT_Y(),
				SimParam.getINIT_THETA());
		vehicle.setVelocity(0, 0);
		setPreferredSize(new Dimension(SimParam.getBOUND() + 20, SimParam.getBOUND() + 20));

		oreList = new ArrayList<Ore>();
		for (int i = 0; i < SimParam.getORENUM(); i++) {
			addNewOre();
		}
		fungiList = new ArrayList<Fungi>();
		for (int i = 0; i < SimParam.getFUNGINUM(); i++) {
			addNewFungi();
			
		}
	}

	public void addNewFungi() {
		synchronized(fungiList) {
		fungiList.add(new Fungi(20 + random.nextDouble() * (SimParam.getBOUND()-40),
				20 + random.nextDouble() * (SimParam.getBOUND()-40)));
		}
		
	}
	
	public void addNewOre() {
		synchronized(oreList) {
			oreList.add(new Ore(20 + random.nextDouble() * (SimParam.getBOUND()-40), 20 + random
				.nextDouble()
				* (SimParam.getBOUND()-40)));
		}
	}
	
	
	public void removeFungi(Fungi fungi) {
		synchronized(fungiList) {
			fungiList.remove(fungi);
		addNewFungi();
		
		}
	}
	
	public void removeOre(Ore ore) {
		synchronized(oreList) {
			oreList.remove(ore);
		addNewOre();
		}
	}
	
	
	
	
	public Vehicle getVehicle() {
		return vehicle;
	}

	private void drawBackGround(Graphics2D g2d) {
		Color color = g2d.getColor();
		
		Dimension d = getSize();
		Rectangle2D back = new Rectangle2D.Double(0, 0, d.getWidth(), d.getHeight());
		g2d.setColor(this.getBackground());
		g2d.draw(back);
		
		int bound;// = SimParam.getBOUND();
		if(d.getHeight() < d.getWidth()) {
			bound = (int)d.getHeight();
		}else {
			bound = (int)d.getWidth();
		}
		
		SimParam.setBOUND(bound);
		Rectangle2D rect = new Rectangle2D.Double(0, 0, SimParam.getBOUND(),
				SimParam.getBOUND());

		g2d.setColor(Color.white);
		g2d.fill(rect);
		g2d.setColor(color);
		// g2d.draw(rect);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		drawBackGround(g2d);

		vehicle.draw(g2d);

		g.drawString("Battery:" + Float.toString(vehicle.getBatteryByPercent()), 0, 20);
		
		//TODO: Here should be some error!!
		synchronized(oreList) {
		for (Ore ore : oreList) {
			ore.draw(g2d);
		}
		}

		synchronized(fungiList) {
		for (Fungi fungi : fungiList) {
			fungi.draw(g2d);
		}
		}
	}

	RepaintThread repaintThread;

	public void startAutoRepaint() {
		repaintThread = new RepaintThread();
		repaintThread.start();
		vehicle.startAutoRefresh();
	}

	public void stopAutoRepaint() {
		vehicle.stopAutoRefresh();
		repaintThread.stopRepaint();
	}

	public class RepaintThread extends Thread {
		private boolean endflag;

		public RepaintThread() {

		}

		public void run() {
			endflag = false;
			while (!endflag) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Ž©“®¶¬‚³‚ê‚½ catch ƒuƒƒbƒN
					e.printStackTrace();
				}
				repaint();
			}
		}

		public void stopRepaint() {
			endflag = true;
		}
	}

	
	ArrayList<Fungi> fungiList;

	ArrayList<Ore> oreList;



	
	
	public Fungi getNearestFungi(Vehicle vehicle) {
		double nearestDistance = Double.MAX_VALUE;
		Fungi nearestFungi = null;
		for (Fungi fungi : fungiList) {
			double dx = vehicle.getX() - fungi.getX();
			double dy = vehicle.getY() - fungi.getY();
			double dist = Math.sqrt(dx * dx + dy * dy);
			if (nearestDistance > dist) {
				nearestDistance = dist;
				nearestFungi = fungi;
			}
		}
		return nearestFungi;
	}

	public float[] getNearestFungiDirectionAndDistance(Vehicle vehicle2) {
		float[] ret = { 0, 0 };
		double nearestDistance = Double.MAX_VALUE;
		Fungi nearestFungi = null;
		for (Fungi fungi : fungiList) {
			double dx = vehicle.getX() - fungi.getX();
			double dy = vehicle.getY() - fungi.getY();
			double dist = Math.sqrt(dx * dx + dy * dy);
			if (nearestDistance > dist) {
				nearestDistance = dist;
				nearestFungi = fungi;
			}
		}
		double dx = -vehicle.getX() + nearestFungi.getX();
		double dy = vehicle.getY() - nearestFungi.getY();
		double theta = -Math.atan2(dy, dx) - vehicle.theta;

		
		double thetabuf = theta % (Math.PI * 2);
		if (-Math.PI <= thetabuf && thetabuf <= Math.PI) {
			theta = thetabuf;
		} else {
			theta = thetabuf - (Math.PI * 2);
		}

		ret[0] = (float) theta;
		ret[1] = (float) nearestDistance;
		return ret;
	}
	
	public float[] getNearestOreDirectionAndDistance(Vehicle vehicle2) {
		float[] ret = { 0, 0 };
		double nearestDistance = Double.MAX_VALUE;
		Ore nearestOre = null;
		for (Ore ore : oreList) {
			double dx = vehicle.getX() - ore.getX();
			double dy = vehicle.getY() - ore.getY();
			double dist = Math.sqrt(dx * dx + dy * dy);
			if (nearestDistance > dist) {
				nearestDistance = dist;
				nearestOre = ore;
			}
		}
		double dx = -vehicle.getX() + nearestOre.getX();
		double dy = vehicle.getY() - nearestOre.getY();
		
		double theta = -Math.atan2(dy, dx) - vehicle.theta;

		double thetabuf = theta % (Math.PI * 2);
		if (-Math.PI <= thetabuf && thetabuf <= Math.PI) {
			theta = thetabuf;
		} else {
			theta = thetabuf - (Math.PI * 2);
		}
		ret[0] = (float) theta;
		ret[1] = (float) nearestDistance;
		return ret;
	}
	
	public Ore getNearestOre(Vehicle vehicle) {
		double nearestDistance = Double.MAX_VALUE;
		Ore nearestOre = null;
		for (Ore ore : oreList) {
			double dx = vehicle.getX() - ore.getX();
			double dy = vehicle.getY() - ore.getY();
			double dist = Math.sqrt(dx * dx + dy * dy);
			if (nearestDistance > dist) {
				nearestDistance = dist;
				nearestOre = ore;
			}
		}
		return nearestOre;
	}
	
}
