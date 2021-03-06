package com.qianzhui.enode.jmx;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Created by xujunbo on 17-9-15.
 */
public class ENodeJMXAgent {
    public static void startAgent() {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        try {
            ObjectName forkJoinName = new ObjectName("com.qianzhui.enode:name=ForkJoinPool");
            server.registerMBean(new ForkJoinPool(), forkJoinName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
