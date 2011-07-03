package org.ysuga_net.funguseater;
// -*- Java -*-
/*!
 * @file FloatLessThan.java
 * @date $Date: 2010/02/19 13:39:30 $
 *
 * $Id: FloatLessThan.java,v 1.1 2010/02/19 13:39:30 ysuga Exp $
 */


import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.RTObject_impl;
import jp.go.aist.rtm.RTC.RtcDeleteFunc;
import jp.go.aist.rtm.RTC.RtcNewFunc;
import jp.go.aist.rtm.RTC.RegisterModuleFunc;
import jp.go.aist.rtm.RTC.util.Properties;

/*!
 * @class FloatLessThan
 * @brief Component goes to Deactivated if data is less than x
 */
public class FloatLessThan implements RtcNewFunc, RtcDeleteFunc, RegisterModuleFunc {

//  Module specification
//  <rtc-template block="module_spec">
    public static String component_conf[] = {
    	    "implementation_id", "FloatLessThan",
    	    "type_name",         "FloatLessThan",
    	    "description",       "Component goes to Deactivated if data is less than x",
    	    "version",           "1.0.0",
    	    "vendor",            "ysuga.net",
    	    "category",          "Test",
    	    "activity_type",     "STATIC",
    	    "max_instance",      "1",
    	    "language",          "Java",
    	    "lang_type",         "compile",
    	    "exec_cxt.periodic.rate", "100.0",
            // Configuration variables
            "conf.default.threshold", "0.6",
    	    ""
            };
//  </rtc-template>

    public RTObject_impl createRtc(Manager mgr) {
        return new FloatLessThanImpl(mgr);
    }

    public void deleteRtc(RTObject_impl rtcBase) {
        rtcBase = null;
    }
    public void registerModule() {
        Properties prop = new Properties(component_conf);
        final Manager manager = Manager.instance();
        manager.registerFactory(prop, new FloatLessThan(), new FloatLessThan());
    }
}
