package com.wasp.schemas;

import org.jvnet.jaxb2_commons.lang.ToString2;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy2;

public class JXBStringUtil {

    /**
     *
     * @param obj object to apply toString
     * @return a String of obj with a other strategy than default jaxb strategy
     */
    public static String pretty(ToString2 obj){
        final ToStringStrategy2 strategy = ToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        obj.append(null, buffer, strategy);
        return buffer.toString();
    }
}
