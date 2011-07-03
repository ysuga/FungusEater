package org.ysuga_net.funguseater.simulator;
import java.awt.Color;


public class SimParam {

	private static int BOUND =  400;
	//private static int INIT_X = getBOUND()/2;
	//private static int INIT_Y = getBOUND()/2;
	
	
	//private static int RADIUS = (int)(getBOUND()*0.07);
	//private static int ARROW = (int)(getRADIUS()*1.5);
	
	//private static int ORERADIUS = getRADIUS()/2;
	private static int ORENUM = 3;

	//private static int FUNGIRADIUS = getRADIUS()/2;
	private static int FUNGINUM = 3;
	
	private static int INIT_THETA = 0;
		
	private static float BATTERY_CONSUMPTION = 0.002f;
	private static float BATTERY_MAX = 1.0f;
	private static Color ORECOLOR = Color.red;
	private static Color FUNGICOLOR = Color.blue;


	static public float FUNGIPOWER = 0.1f;
	static public float VEHICLE_SPEED = 1.0f;
	public static void setBOUND(int bOUND) {
		BOUND = bOUND;
	}
	public static int getBOUND() {
		return BOUND;
	}

	public static int getRADIUS() {
		return (int)(getBOUND()*0.07);
	}

	public static int getARROW() {
		return (int)(getRADIUS()*1.5);
	}

	public static int getORERADIUS() {
		return getRADIUS()/2;
	}
	public static void setORENUM(int oRENUM) {
		ORENUM = oRENUM;
	}
	public static int getORENUM() {
		return ORENUM;
	}

	public static int getFUNGIRADIUS() {
		return getRADIUS()/2;
	}
	
	public static void setFUNGINUM(int fUNGINUM) {
		FUNGINUM = fUNGINUM;
	}
	public static int getFUNGINUM() {
		return FUNGINUM;
	}
	public static void setINIT_THETA(int iNIT_THETA) {
		INIT_THETA = iNIT_THETA;
	}
	public static int getINIT_THETA() {
		return INIT_THETA;
	}
	public static void setBATTERY_CONSUMPTION(float bATTERY_CONSUMPTION) {
		BATTERY_CONSUMPTION = bATTERY_CONSUMPTION;
	}
	public static float getBATTERY_CONSUMPTION() {
		return BATTERY_CONSUMPTION;
	}

	
	public static float getBATTERY_MAX() {
		return BATTERY_MAX;
	}

	
	
	public static Color getORECOLOR() {
		return ORECOLOR;
	}

	
	public static Color getFUNGICOLOR() {
		return FUNGICOLOR;
	}
	
	
	public static int getINIT_X() {
		return getBOUND()/2;
	}
	
	
	public static int getINIT_Y() {
		return getBOUND()/2;
	}
}
