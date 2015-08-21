package hl7ws.healthcare.nist.gov.validation.message;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import gov.nist.healthcare.hl7ws.messagevalidation.MessageValidationV2Interface;

import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.0.5
 * 2015-05-18T13:45:55.851-04:00
 * Generated source version: 3.0.5
 * 
 */
@WebServiceClient(name = "MessageValidationV2", 
                  wsdlLocation = "http://hl7v2.ws.nist.gov/hl7v2ws//services/soap/MessageValidationV2?wsdl",
                  targetNamespace = "http://gov.nist.healthcare.hl7ws/validation/message") 
public class MessageValidationV2 extends Service {

    public final static URL WSDL_LOCATION;
    public final static String DEFAULT_ENDPOINT = "http://hl7v2.ws.nist.gov/hl7v2ws//services/soap/MessageValidationV2";
    public String endpoint;
    public final static QName SERVICE = new QName("http://gov.nist.healthcare.hl7ws/validation/message", "MessageValidationV2");
    public final static QName MessageValidationV2ServicePort = new QName("http://gov.nist.healthcare.hl7ws/validation/message", "MessageValidationV2ServicePort");
    static {
        URL url = null;
        try {
            url = new URL("http://hl7v2.ws.nist.gov/hl7v2ws/services/soap/MessageValidationV2?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(MessageValidationV2.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://hl7v2.ws.nist.gov/hl7v2ws//services/soap/MessageValidationV2?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public MessageValidationV2(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public MessageValidationV2(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
        this.endpoint = MessageValidationV2.DEFAULT_ENDPOINT;
    }
    
    public MessageValidationV2(URL wsdlLocation, QName serviceName,String endpoint) {
        super(wsdlLocation, serviceName);
        if(endpoint.equals(""))
        	this.endpoint = MessageValidationV2.DEFAULT_ENDPOINT;
        else
        	this.endpoint = endpoint;
    }

    public MessageValidationV2() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MessageValidationV2(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MessageValidationV2(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public MessageValidationV2(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns MessageValidationV2Interface
     */
    @WebEndpoint(name = "MessageValidationV2ServicePort")
    public MessageValidationV2Interface getMessageValidationV2ServicePort() {
    	MessageValidationV2Interface proxy = super.getPort( MessageValidationV2ServicePort, MessageValidationV2Interface.class);
        ( (BindingProvider) proxy ).getRequestContext().put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.endpoint );
        return proxy;
        //return super.getPort(MessageValidationV2ServicePort, MessageValidationV2Interface.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns MessageValidationV2Interface
     */
    @WebEndpoint(name = "MessageValidationV2ServicePort")
    public MessageValidationV2Interface getMessageValidationV2ServicePort(WebServiceFeature... features) {
    	MessageValidationV2Interface proxy = super.getPort( MessageValidationV2ServicePort, MessageValidationV2Interface.class,features);
        ( (BindingProvider) proxy ).getRequestContext().put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.endpoint );
        return proxy;
        //return super.getPort(MessageValidationV2ServicePort, MessageValidationV2Interface.class, features);
    }

}