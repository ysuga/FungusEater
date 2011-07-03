package org.ysuga_net.funguseater.simulator;
// -*- Java -*-
/*!
 * @file  VehicleSimImpl.java
 * @brief Robotic Vehicle Simulator
 * @date  $Date: 2010/02/19 13:39:30 $
 *
 * $Id: VehicleSimImpl.java,v 1.1 2010/02/19 13:39:30 ysuga Exp $
 */

import java.util.Random;

import jp.go.aist.rtm.RTC.DataFlowComponentBase;
import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.port.InPort;
import jp.go.aist.rtm.RTC.port.OutPort;
import jp.go.aist.rtm.RTC.util.DataRef;
import jp.go.aist.rtm.RTC.util.FloatHolder;
import RTC.ReturnCode_t;
import RTC.TimedFloat;
import RTC.TimedFloatSeq;

/*!
 * @class VehicleSimImpl
 * @brief Robotic Vehicle Simulator
 *
 */
public class VehicleSimImpl extends DataFlowComponentBase {

  /*!
   * @brief constructor
   * @param manager Maneger Object
   */
	public VehicleSimImpl(Manager manager) {  
        super(manager);
        // <rtc-template block="initializer">
        m_vr_val = new TimedFloat(new RTC.Time(0,0),0);
        m_vr = new DataRef<TimedFloat>(m_vr_val);
        m_vrIn = new InPort<TimedFloat>("vr", m_vr);
       
        m_vl_val = new TimedFloat(new RTC.Time(0,0),0);
        m_vl = new DataRef<TimedFloat>(m_vl_val);
        m_vlIn = new InPort<TimedFloat>("vl", m_vl);
        m_fungi_val = new TimedFloatSeq(new RTC.Time(0,0), new float[2]);
        m_fungi = new DataRef<TimedFloatSeq>(m_fungi_val);
        m_fungiOut = new OutPort<TimedFloatSeq>("fungi", m_fungi);
        
        m_ore_val = new TimedFloatSeq(new RTC.Time(0,0), new float[2]);
        m_ore = new DataRef<TimedFloatSeq>(m_ore_val);
        m_oreOut = new OutPort<TimedFloatSeq>("ore", m_ore);
        
        m_battery_val = new TimedFloat(new RTC.Time(0,0),0);
        m_battery = new DataRef<TimedFloat>(m_battery_val);
        m_batteryOut = new OutPort<TimedFloat>("battery", m_battery);
        
        // </rtc-template>

        // Registration: InPort/OutPort/Service
        // <rtc-template block="registration">
        // Set InPort buffers
        try {
			registerInPort(TimedFloat.class, "vr", m_vrIn);
			registerInPort(TimedFloat.class, "vl", m_vlIn);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // Set OutPort buffer
        try {
			registerOutPort(TimedFloatSeq.class, "fungi", m_fungiOut);
			registerOutPort(TimedFloatSeq.class, "ore", m_oreOut);
			registerOutPort(TimedFloat.class, "battery", m_batteryOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // Set service provider to Ports
        
        // Set service consumers to Ports
        
        // Set CORBA Service Ports
        
        // </rtc-template>
    }

    /*!
     *
     * The initialize action (on CREATED->ALIVE transition)
     * formaer rtc_init_entry() 
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onInitialize() {
        bindParameter("speed", m_speed, "1.0f");
        bindParameter("consumption", m_consumption, "0.005f");
        bindParameter("fungi_energy", m_fungi_energy, "0.1f");

        bindParameter("error_rate", m_error_rate, "0.001f");
        frame = new VehicleSimWindow();
    	vehicle = frame.getVehicle();
        
        return ReturnCode_t.RTC_OK;
    }

    /***
     *
     * The finalize action (on ALIVE->END transition)
     * formaer rtc_exiting_entry()
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onFinalize() {
//        return super.onFinalize();
//    }

    /***
     *
     * The startup action when ExecutionContext startup
     * former rtc_starting_entry()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onStartup(int ec_id) {
//        return super.onStartup(ec_id);
//    }

    /***
     *
     * The shutdown action when ExecutionContext stop
     * former rtc_stopping_entry()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onShutdown(int ec_id) {
//        return super.onShutdown(ec_id);
//    }

    
    VehicleSimWindow frame;
    Vehicle vehicle;
    /***
     *
     * The activated action (Active state entry action)
     * former rtc_active_entry()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onActivated(int ec_id) {
    	frame.start();
    	//vehicle.chargeFull();
    	return super.onActivated(ec_id);
    }

    /***
     *
     * The deactivated action (Active state exit action)
     * former rtc_active_exit()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onDeactivated(int ec_id) {
    	frame.stop();
    	//vehicle = null;
        return super.onDeactivated(ec_id);
    }

    float vr, vl;
    /***
     *
     * The execution action that is invoked periodically
     * former rtc_active_do()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onExecute(int ec_id) {
    	vehicle.setSpeed(this.m_speed.value);
		vehicle.setBatterConsumption(this.m_consumption.value);
		vehicle.setFungiEnergy(m_fungi_energy.value);
    	if(m_vrIn.isNew()) {
    		m_vrIn.read();
    		vr = m_vr.v.data;
    		//vr = m_vr_val.data; 
    		vehicle.setVelocity(vr, vl);
    	//	System.out.println("Vr, Vl = " + vr + ", " + vl);
    	}

    	Random rand = new Random();
    	double d = rand.nextDouble();
    	if(d < m_error_rate.value) {
    		return ReturnCode_t.RTC_ERROR;
    	}
    	
    	if(m_vlIn.isNew()) {
    		m_vlIn.read();
    		vl = m_vl.v.data;
    		//vl = m_vl_val.data; 
    		vehicle.setVelocity(vr, vl);
    	//	System.out.println("Vr, Vl = " + vr + ", " + vl);
    	}

    	float[] fungiData = vehicle.getNearestFungiDirectionAndDistance();
    	m_fungi_val.data = fungiData;
    	m_fungiOut.write();
    	float[] oreData = vehicle.getNearestOreDirectionAndDistance();
    	m_ore_val.data = oreData;
    	m_oreOut.write();
    	
		//m_battery_val.tm = new Time();
		m_battery_val.data = vehicle.getBatteryVoltage();
		m_batteryOut.write();
	
        return super.onExecute(ec_id);
    }

    /***
     *
     * The aborting action when main logic error occurred.
     * former rtc_aborting_entry()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//  @Override
//  public ReturnCode_t onAborting(int ec_id) {
//      return super.onAborting(ec_id);
//  }

    /***
     *
     * The error action in ERROR state
     * former rtc_error_do()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    public ReturnCode_t onError(int ec_id) {
//        return super.onError(ec_id);
//    }

    /***
     *
     * The reset action that is invoked resetting
     * This is same but different the former rtc_init_entry()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onReset(int ec_id) {
//        return super.onReset(ec_id);
//    }

    /***
     *
     * The state update action that is invoked after onExecute() action
     * no corresponding operation exists in OpenRTm-aist-0.2.0
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onStateUpdate(int ec_id) {
//        return super.onStateUpdate(ec_id);
//    }

    /***
     *
     * The action that is invoked when execution context's rate is changed
     * no corresponding operation exists in OpenRTm-aist-0.2.0
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onRateChanged(int ec_id) {
//        return super.onRateChanged(ec_id);
//    }
//
	// Configuration variable declaration
	// <rtc-template block="config_declare">
    /*!
     * 
     * - Name:  conf_varname
     * - DefaultValue: 100
     */
    protected FloatHolder m_speed = new FloatHolder();

    protected FloatHolder m_consumption = new FloatHolder();
    
    protected FloatHolder m_fungi_energy = new FloatHolder();

    

    protected FloatHolder m_error_rate = new FloatHolder();
    
    
    // </rtc-template>

    // DataInPort declaration
    // <rtc-template block="inport_declare">
    protected TimedFloat m_vr_val;
    protected DataRef<TimedFloat> m_vr;
    /*!
     */
    protected InPort<TimedFloat> m_vrIn;

    protected TimedFloat m_vl_val;
    protected DataRef<TimedFloat> m_vl;
    /*!
     */
    protected InPort<TimedFloat> m_vlIn;

    
    // </rtc-template>

    // DataOutPort declaration
    // <rtc-template block="outport_declare">
    protected TimedFloatSeq m_fungi_val;
    protected DataRef<TimedFloatSeq> m_fungi;
    /*!
     */
    protected OutPort<TimedFloatSeq> m_fungiOut;

    
    protected TimedFloatSeq m_ore_val;
    protected DataRef<TimedFloatSeq> m_ore;
    /*!
     */
    protected OutPort<TimedFloatSeq> m_oreOut;
    
    protected TimedFloat m_battery_val;
    protected DataRef<TimedFloat> m_battery;
    /*!
     */
    protected OutPort<TimedFloat> m_batteryOut;
    
    // </rtc-template>

    // CORBA Port declaration
    // <rtc-template block="corbaport_declare">
    
    // </rtc-template>

    // Service declaration
    // <rtc-template block="service_declare">
    
    // </rtc-template>

    // Consumer declaration
    // <rtc-template block="consumer_declare">
    
    // </rtc-template>


}
