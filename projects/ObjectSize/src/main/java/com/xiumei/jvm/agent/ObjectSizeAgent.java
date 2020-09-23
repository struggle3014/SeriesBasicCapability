package com.xiumei.jvm.agent;

import java.lang.instrument.Instrumentation;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 20:43 2020/6/2
 * @Version: 1.0
 * @Description: 对象大小 agent
 **/
public class ObjectSizeAgent {

    private static Instrumentation inst;

    public static void premain(String agentArgs, Instrumentation _inst) {
        inst = _inst;
    }

    public static long sizeOf(Object o) {
        return inst.getObjectSize(o);
    }

}
