package com.wasp.schemas;

import org.jvnet.jaxb2_commons.lang.ToString2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JXBStringUtil {

    public static String pretty(ToString2 obj){
        String result=obj.toString();
        Pattern pattern = Pattern.compile(".*\\.([^\\.]+)@\\w*(\\[.*]).*");
        Matcher matcher = pattern.matcher(result);

        if(matcher.matches()){
            result=matcher.group(1)+matcher.group(2);
        }
        return result;
    }
}
