package com.muiboot.rpc.config;

import com.muiboot.rpc.Service;
import org.apache.commons.lang.ArrayUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * Created by 75631 on 2019/1/4.
 */
@Configuration
public class SoapConfig {
    
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean
    public Endpoint[] endpoint(Service[] services) {
        EndpointImpl[] endpoints=null;
        if (ArrayUtils.isNotEmpty(services)){
            endpoints=new EndpointImpl[services.length];
            for (int i=0,l=services.length;i<l;i++){
                endpoints[i]= new EndpointImpl(springBus(),services[i]);
                endpoints[i].publish(services[i].getAddress());
            }
        }
        return endpoints;
    }
/*    @Bean
    public ServletRegistrationBean dispatcherServlet() {
        CXFServlet cxfServlet = new CXFServlet();
        cxfServlet.setBus(springBus());
        ServletRegistrationBean servletBean = new ServletRegistrationBean(cxfServlet, "/soap");
        servletBean.setName("v1");
        return servletBean;
    }*/

}
