package org.jboss.resteasy.plugins.providers.jaxb.fastinfoset;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.jaxb.JAXBElementProvider;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Provider
@Consumes({ "application/fastinfoset", "application/*+fastinfoset" })
@Produces({ "application/fastinfoset", "application/*+fastinfoset" })
public class FastinfoSetElementProvider extends JAXBElementProvider {
    @Override
    protected boolean needsSecurity() {
        return false;
    }
}
