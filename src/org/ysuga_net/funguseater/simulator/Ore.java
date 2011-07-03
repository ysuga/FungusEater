package org.ysuga_net.funguseater.simulator;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;

public class Ore  {

	private double x;

	private double y;

	private Double ellipse;

	public Ore(double x, double y) {
		this.setX(x);
		this.setY(y);

	}

	public void draw(Graphics2D g) {
		ellipse = new Ellipse2D.Double(getX() - SimParam.getORERADIUS(), getY()
				- SimParam.getORERADIUS(), SimParam.getORERADIUS() * 2, SimParam.getORERADIUS() * 2);
		g.setColor(SimParam.getORECOLOR());
		g.drawString("Ore", (int)getX(), (int)getY()+ SimParam.getFUNGIRADIUS() + 10);
		g.draw(ellipse);
		g.setColor(Color.black);
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
}
