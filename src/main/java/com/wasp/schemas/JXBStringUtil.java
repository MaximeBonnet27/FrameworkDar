package com.wasp.schemas;

import org.jvnet.jaxb2_commons.lang.ToString2;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy2;

public class JXBStringUtil {

    public static String pretty(ToString2 obj){
        final ToStringStrategy2 strategy = ToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        obj.append(null, buffer, strategy);
        return buffer.toString();
    }
}
