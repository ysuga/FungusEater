package org.ysuga_net.funguseater.controller;
// -*- Java -*-
/*!
 * @file  FungusEaterImpl.java
 * @brief Fungus Eater Controller
 * @date  $Date: 2010/02/19 13:39:30 $
 *
 * $Id: FungusEaterImpl.java,v 1.1 2010/02/19 13:39:30 ysuga Exp $
 */

import jp.go.aist.rtm.RTC.DataFlowComponentBase;
import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.port.InPort;
import jp.go.aist.rtm.RTC.port.OutPort;
import jp.go.aist.rtm.RTC.util.DataRef;
import RTC.ReturnCode_t;
import RTC.Time;
import RTC.TimedFloat;
import RTC.TimedFloatSeq;

/*!
 * @class FungusEaterImpl
 * @brief Fungus Eater Controller
 *
 */
public class FungusEaterImpl extends DataFlowComponentBase {

  /*!
   * @brief constructor
   * @param manager Maneger Object
   */
	public FungusEaterImpl(Manager manager) {  
        super(manager);
        // <rtc-template block="initializer">
        m_in_val = new TimedFloatSeq(new Time(0, 0), new float[2]);
        m_in = new DataRef<TimedFloatSeq>(m_in_val);
        m_inIn = new InPort<TimedFloatSeq>("in", m_in);
        m_vr_val = new TimedFloat(new Time(0, 0), 0);
        m_vr = new DataRef<TimedFloat>(m_vr_val);
        m_vrOut = new OutPort<TimedFloat>("vr", m_vr);
        m_vl_val = new TimedFloat(new Time(0, 0), 0);
        m_vl = new DataRef<TimedFloat>(m_vl_val);
        m_vlOut = new OutPort<TimedFloat>("vl", m_vl);
        
        // </rtc-template>

        // Registration: InPort/OutPort/Service
        // <rtc-template block="registration">
        // Set InPort buffers
        try {
			registerInPort(TimedFloatSeq.class, "in", m_inIn);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // Set OutPort buffer
        try {
			registerOutPort(TimedFloat.class, "vr", m_vrOut);
			registerOutPort(TimedFloat.class, "vl", m_vlOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // Set service provider to Ports
        
        // Set service consumers to Ports
        
        // Set CORBA Service Ports
        
        // </rtc-template>
    }

    /**
     *
     * The initialize action (on CREATED->ALIVE transition)
     * formaer rtc_init_entry() 
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onInitialize() {
//        return super.onInitialize();
//    }

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
    	this.m_vr_val.data = 0;
    	m_vl_val.data = 0;
    	m_vrOut.write();
    	m_vlOut.write();
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
        return super.onDeactivated(ec_id);
    }

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
    	
    	if(this.m_inIn.isNew()) {
    		m_inIn.read();
    		float direction = m_in.v.data[0];
    		float distance  = m_in.v.data[1];
    		
    		//System.out.println("direction = " + direction);
    		
    		double K = 0.1;
    		double vr = K * distance * Math.pow((2 * Math.PI - (direction + Math.PI)),2);
    		double vl = K * distance * Math.pow((direction + Math.PI), 2);
    		
    		
    		if(direction > Math.PI/2) {
    			vl = K*distance*Math.PI*2;
    			vr = -K*distance*Math.PI*2;
    		} else if(direction < -Math.PI/2) {
    			vl = -K*distance*Math.PI*2;
    			vr = K*distance*Math.PI*2;
    		}
    		
    		
    		
    		//System.out.println("vr, vl = " + vr + ", " + vl);
    		
    		m_vr_val.data = (float)vr;
    		m_vl_val.data = (float)vl;
    		
    		m_vrOut.write();
    		m_vlOut.write();
    		
    	}
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
    // DataInPort declaration
    // <rtc-template block="inport_declare">
    protected TimedFloatSeq m_in_val;
    protected DataRef<TimedFloatSeq> m_in;
    /*!
     */
    protected InPort<TimedFloatSeq> m_inIn;

    
    // </rtc-template>

    // DataOutPort declaration
    // <rtc-template block="outport_declare">
    protected TimedFloat m_vr_val;
    protected DataRef<TimedFloat> m_vr;
    /*!
     */
    protected OutPort<TimedFloat> m_vrOut;

    protected TimedFloat m_vl_val;
    protected DataRef<TimedFloat> m_vl;
    /*!
     */
    protected OutPort<TimedFloat> m_vlOut;

    
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
