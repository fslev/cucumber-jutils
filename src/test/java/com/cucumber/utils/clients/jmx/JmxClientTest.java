package com.cucumber.utils.clients.jmx;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class JmxClientTest {

    @Test
    public void testJmxConnection() {
        JmxClient jmxClient = new JmxClient("service:jmx:rmi:///jndi/rmi://domnexfrontmwqa01.mw.server.lan:11193/server",
                "control", "shu8cooS");
        PollerMBean proxy = jmxClient.getMBeanProxy("MigraeneDomain:name=MigraeneDomainPollerMBean", PollerMBean.class);
        proxy.switchPollerActive();
    }
}
