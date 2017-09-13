package okmvc.service;

import okmvc.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class HelloService {

    private static final Logger LOG = LoggerFactory.getLogger(HelloService.class);

    public void doService() {
        LOG.info(".");
        LOG.info("..");
        LOG.info("...");
        LOG.info("....");
    }

}
