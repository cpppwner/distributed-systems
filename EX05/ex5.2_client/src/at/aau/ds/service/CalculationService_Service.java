
package at.aau.ds.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.7-b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "CalculationService", targetNamespace = "http://ds.aau.at", wsdlLocation = "file:/C:/Users/cpppw/projects/distributed-systems/EX05/ex5.2_server/src/at/aau/ds/CalculationService.wsdl")
public class CalculationService_Service
    extends Service
{

    private final static URL CALCULATIONSERVICE_WSDL_LOCATION;
    private final static WebServiceException CALCULATIONSERVICE_EXCEPTION;
    private final static QName CALCULATIONSERVICE_QNAME = new QName("http://ds.aau.at", "CalculationService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/C:/Users/cpppw/projects/distributed-systems/EX05/ex5.2_server/src/at/aau/ds/CalculationService.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CALCULATIONSERVICE_WSDL_LOCATION = url;
        CALCULATIONSERVICE_EXCEPTION = e;
    }

    public CalculationService_Service() {
        super(__getWsdlLocation(), CALCULATIONSERVICE_QNAME);
    }

    public CalculationService_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), CALCULATIONSERVICE_QNAME, features);
    }

    public CalculationService_Service(URL wsdlLocation) {
        super(wsdlLocation, CALCULATIONSERVICE_QNAME);
    }

    public CalculationService_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CALCULATIONSERVICE_QNAME, features);
    }

    public CalculationService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CalculationService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns CalculationService
     */
    @WebEndpoint(name = "CalculationService")
    public CalculationService getCalculationService() {
        return super.getPort(new QName("http://ds.aau.at", "CalculationService"), CalculationService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CalculationService
     */
    @WebEndpoint(name = "CalculationService")
    public CalculationService getCalculationService(WebServiceFeature... features) {
        return super.getPort(new QName("http://ds.aau.at", "CalculationService"), CalculationService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CALCULATIONSERVICE_EXCEPTION!= null) {
            throw CALCULATIONSERVICE_EXCEPTION;
        }
        return CALCULATIONSERVICE_WSDL_LOCATION;
    }

}
