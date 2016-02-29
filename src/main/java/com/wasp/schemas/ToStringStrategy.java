package com.wasp.schemas;

import org.jvnet.jaxb2_commons.lang.DefaultToStringStrategy;

public class ToStringStrategy extends DefaultToStringStrategy {
    public static final ToStringStrategy INSTANCE =new ToStringStrategy();

    public ToStringStrategy() {
    }

    @Override
    public boolean isUseIdentityHashCode() {
        return false;
    }

   @Override
    protected void appendClassName(StringBuilder buffer, Object object) {
        buffer.append(super.getShortClassName(object.getClass()));
    }
}
