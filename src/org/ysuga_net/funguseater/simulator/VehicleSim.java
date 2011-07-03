package org.ysuga_net.funguseater.simulator;
// -*- Java -*-
/*!
 * @file VehicleSim.java
 * @date $Date: 2010/02/19 13:39:29 $
 *
 * $Id: VehicleSim.java,v 1.1 2010/02/19 13:39:29 ysuga Exp $
 */

import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.RTObject_impl;
import jp.go.aist.rtm.RTC.RtcDeleteFunc;
import jp.go.aist.rtm.RTC.RtcNewFunc;
import jp.go.aist.rtm.RTC.RegisterModuleFunc;
import jp.go.aist.rtm.RTC.util.Properties;

/*!
 * @class VehicleSim
 * @brief Robotic Vehicle Simulator
 */
public class VehicleSim implements RtcNewFunc, RtcDeleteFunc, RegisterModuleFunc {

//  Module specification
//  <rtc-template block="module_spec">
    public static String component_conf[] = {
    	    "implementation_id", "VehicleSim",
    	    "type_name",         "VehicleSim",
    	    "description",       "Robotic Vehicle Simulator",
    	    "version",           "1.0.0",
    	    "vendor",            "ysuga.net",
    	    "category",          "Category",
    	    "activity_type",     "STATIC",
    	    "max_instance",      "1",
    	    "language",          "Java",
    	    "lang_type",         "compile",
    	    "exec_cxt.periodic.rate", "100.0",
            // Configuration variables
            "conf.default.speed", "1.0",
            "conf.default.consumption", "0.005",
            "conf.default.fungi_energy", "0.1",
            "conf.default.error_rate", "0.001",
    	    ""
            };
//  </rtc-template>

    public RTObject_impl createRtc(Manager mgr) {
        return new VehicleSimImpl(mgr);
    }

    public void deleteRtc(RTObject_impl rtcBase) {
        rtcBase = null;
    }
    public void registerModule() {
        Properties prop = new Properties(component_conf);
        final Manager manager = Manager.instance();
        manager.registerFactory(prop, new VehicleSim(), new VehicleSim());
    }
}
