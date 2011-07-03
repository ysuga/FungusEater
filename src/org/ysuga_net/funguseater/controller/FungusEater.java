package org.ysuga_net.funguseater.controller;
// -*- Java -*-
/*!
 * @file FungusEater.java
 * @date $Date: 2010/02/19 13:39:30 $
 *
 * $Id: FungusEater.java,v 1.1 2010/02/19 13:39:30 ysuga Exp $
 */

import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.RTObject_impl;
import jp.go.aist.rtm.RTC.RtcDeleteFunc;
import jp.go.aist.rtm.RTC.RtcNewFunc;
import jp.go.aist.rtm.RTC.RegisterModuleFunc;
import jp.go.aist.rtm.RTC.util.Properties;

/*!
 * @class FungusEater
 * @brief Fungus Eater Controller
 */
public class FungusEater implements RtcNewFunc, RtcDeleteFunc, RegisterModuleFunc {

//  Module specification
//  <rtc-template block="module_spec">
    public static String component_conf[] = {
    	    "implementation_id", "FungusEater",
    	    "type_name",         "FungusEater",
    	    "description",       "Fungus Eater Controller",
    	    "version",           "1.0.0",
    	    "vendor",            "ysuga.net",
    	    "category",          "Test",
    	    "activity_type",     "STATIC",
    	    "max_instance",      "1",
    	    "language",          "Java",
    	    "lang_type",         "compile",
    	    "exec_cxt.periodic.rate", "100.0",
    	    ""
            };
//  </rtc-template>

    public RTObject_impl createRtc(Manager mgr) {
        return new FungusEaterImpl(mgr);
    }

    public void deleteRtc(RTObject_impl rtcBase) {
        rtcBase = null;
    }
    public void registerModule() {
        Properties prop = new Properties(component_conf);
        final Manager manager = Manager.instance();
        manager.registerFactory(prop, new FungusEater(), new FungusEater());
    }
}
