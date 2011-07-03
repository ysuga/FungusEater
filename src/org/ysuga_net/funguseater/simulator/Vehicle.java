package org.ysuga_net.funguseater.simulator;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class Vehicle {

	static VehicleSimTime time = VehicleSimTime.getSimTime();

	private double vr = 0;

	private double vl = 0;

	public void setVelocity(double vr, double vl) {
		// ごめんなさい．600は見た感じでチューニングしました．
		// 大きくすると早くなります．
		this.vr = speed * vr / SimParam.getBOUND() * 600;
		this.vl = speed * vl / SimParam.getBOUND() * 600;
	}

	public void chargeFull() {
		batteryPercent = 1.0f;
	}
	double timeBuf = -1;;
	
	
	float batteryConsumption = SimParam.getBATTERY_CONSUMPTION();
	public void setBatterConsumption(float cons) {
		batteryConsumption = cons;
	}
	
	float speed = SimParam.VEHICLE_SPEED;
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void refreshPosition() {
		if (timeBuf < 0) {
			timeBuf = time.getCurrentSimTime();
		}
		double currentTime = time.getCurrentSimTime() - timeBuf;
		double deltaTime = currentTime - timeBuf;
		timeBuf = currentTime;
		time.tic(0.1);
		// まずはロボット座標系で考える．

		double dr = vr * deltaTime;
		double drtheta = dr / (getRadius() * 2);
		double drF = getRadius() * Math.sin(drtheta);
		double drS = getRadius() * Math.cos(drtheta) - getRadius();

		double dl = vl * deltaTime;
		double dltheta = dl / (getRadius() * 2);
		double dlF = getRadius() * Math.sin(dltheta);
		double dlS = getRadius() - getRadius() * Math.cos(dltheta);

		double dFront = drF + dlF;
		double dSide = drS + dlS;
		double dTheta = -drtheta + dltheta;

		double dx = dFront * Math.cos(theta) - dSide * Math.sin(theta);
		double dy = -dFront * Math.sin(theta) + dSide * Math.cos(theta);

		setX(getX() + dx);
		if (getX() < SimParam.getRADIUS())
			setX(SimParam.getRADIUS());
		else if (getX() > SimParam.getBOUND()-SimParam.getRADIUS())
			setX(SimParam.getBOUND()-SimParam.getRADIUS());
		setY(getY() - dy);
		if (getY() < SimParam.getRADIUS())
			setY(SimParam.getRADIUS());
		else if (getY() > SimParam.getBOUND()-SimParam.getRADIUS())
			setY(SimParam.getBOUND()-SimParam.getRADIUS());
		theta += dTheta;
		double thetabuf = theta % (Math.PI*2);
		if(thetabuf <= Math.PI) {
			theta = thetabuf;
		}else {
			theta = thetabuf - (Math.PI*2);
		}

		ellipse = new Ellipse2D.Double(getX() - getRadius(), getY() - getRadius(), getRadius() * 2,
				getRadius() * 2);
		line = new Line2D.Double(getX(), getY(), getX() + getArrow() * Math.cos(theta), getY() + getArrow()
				* Math.sin(theta));
		
		
		Fungi fungi = owner.getNearestFungi(this);
		eatFungiIfPossible(fungi);
		
		Ore ore = owner.getNearestOre(this);
		eatOreIfPossible(ore);
		
		batteryPercent -= batteryConsumption;
		if(batteryPercent < 0) batteryPercent = 0;
		
	}

	private void eatFungiIfPossible(Fungi fungi) {
		double dx = getX() - fungi.getX();
		double dy = getY() - fungi.getY();
		double distance = Math.sqrt(dx*dx + dy*dy);
		if(distance <= SimParam.getRADIUS() + SimParam.getFUNGIRADIUS()) {
			owner.removeFungi(fungi);
			batteryPercent += this.fungi_energy;
			if(batteryPercent > 1.0f) batteryPercent = 1.0f;
		}
	}
	
	private void eatOreIfPossible(Ore ore) {
		double dx = getX() - ore.getX();
		double dy = getY() - ore.getY();
		double distance = Math.sqrt(dx*dx + dy*dy);
		if(distance <= SimParam.getRADIUS() + SimParam.getORERADIUS()) {
			owner.removeOre(ore);
		}
	}
	
	/**
	 * 
	 */
	private double x;

	private double y;

	public double theta;

	private float batteryPercent;

	final public float getBatteryByPercent() {
		return batteryPercent;
	}
	
	VehicleSimPanel owner;

	private int radius = SimParam.getRADIUS();

	/**
	 * 
	 */
	private int arrow = SimParam.getARROW();

	public Vehicle(VehicleSimPanel owner, double x, double y, double theta) {
		this.owner = owner;
		this.setX(x);
		this.setY(y);
		this.theta = theta;

		batteryPercent = 1.0f;
	}

	public void startAutoRefresh() {
		refreshPositionThread = new RefreshPositionThread();
		refreshPositionThread.start();
	}

	public void stopAutoRefresh() {
		refreshPositionThread.stopRefresh();
	}

	class RefreshPositionThread extends Thread {

		private boolean endflag;

		public void run() {
			endflag = false;

			while (!endflag) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshPosition();
			}

		}

		public void stopRefresh() {
			endflag = true;
		}
	};

	RefreshPositionThread refreshPositionThread;

	Ellipse2D ellipse;

	Line2D line;

	private float fungi_energy = SimParam.FUNGIPOWER;

	public void draw(Graphics2D g2d) {
		if (ellipse != null) {
			g2d.draw(ellipse);
		}
		if (line != null) {
			g2d.draw(line);
		}
	}

	public float[] getNearestFungiDirectionAndDistance() {
		return owner.getNearestFungiDirectionAndDistance(this);
	}

	public float getBatteryVoltage() {
		return batteryPercent * SimParam.getBATTERY_MAX();
	}

	public float[] getNearestOreDirectionAndDistance() {
		return owner.getNearestOreDirectionAndDistance(this);
	}

	public void setFungiEnergy(Float value) {
		this.fungi_energy  = value;
	}

	public void setX(double x) {
		x /= SimParam.getBOUND();
		this.x = x;
	}

	public double getX() {
		return x * SimParam.getBOUND();
	}

	public void setY(double y) {
		y /= SimParam.getBOUND();

		this.y = y;
	}

	public double getY() {
		return y * SimParam.getBOUND();
	}


	private int getRadius() {
		return SimParam.getRADIUS();
	}


	private int getArrow() {
		return SimParam.getARROW();
	}

}
