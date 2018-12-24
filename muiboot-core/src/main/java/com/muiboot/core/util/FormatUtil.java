package com.muiboot.core.util;

import org.apache.commons.collections.MapUtils;
import org.springframework.util.Assert;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 75631 on 2018/12/22.
 */
public class FormatUtil {
    public static String toString(String expression,Map<String,Object> param){
        Assert.notNull(expression,"表达式不能为空");
        Map<Integer,Object> keys=new TreeMap();
        if (MapUtils.isNotEmpty(param)){
            for (Map.Entry<String,Object> entry:param.entrySet()){
                String key = entry.getKey();
                Object value=entry.getValue();
                int index=expression.indexOf("{"+key+"}");
                if (index>=0){
                    expression=expression.replace("{"+key+"}","");
                    keys.put(index,value);
                }
            }
        }
        return String.format(expression,keys.values().toArray());
    };
}
