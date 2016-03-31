
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package gov.nist.healthcare.hl7ws.messagevalidation;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.0.5
 * 2016-03-29T10:23:11.759-04:00
 * Generated source version: 3.0.5
 * 
 */

@javax.jws.WebService(
                      serviceName = "MessageValidationV2",
                      portName = "MessageValidationV2ServicePort",
                      targetNamespace = "http://gov.nist.healthcare.hl7ws/validation/message",
                      wsdlLocation = "http://localhost:8080/hl7v2ws-base/services/soap/MessageValidationV2?WSDl",
                      endpointInterface = "gov.nist.healthcare.hl7ws.messagevalidation.MessageValidationV2Interface")
                      
public class MessageValidationV2ServicePortImpl implements MessageValidationV2Interface {

    private static final Logger LOG = Logger.getLogger(MessageValidationV2ServicePortImpl.class.getName());

    /* (non-Javadoc)
     * @see gov.nist.healthcare.hl7ws.messagevalidation.MessageValidationV2Interface#loadResource(java.lang.String  arg0 ,)java.lang.String  arg1 ,)java.lang.String  arg2 )*
     */
    public java.lang.String loadResource(java.lang.String arg0,java.lang.String arg1,java.lang.String arg2) { 
        LOG.info("Executing operation loadResource");
        System.out.println(arg0);
        System.out.println(arg1);
        System.out.println(arg2);
        try {
            java.lang.String _return = "_return-2005109629";
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see gov.nist.healthcare.hl7ws.messagevalidation.MessageValidationV2Interface#validateDQA(java.lang.String  arg0 ,)java.lang.String  arg1 ,)java.lang.String  arg2 ,)java.lang.String  arg3 ,)java.lang.String  arg4 )*
     */
    public java.lang.String validateDQA(java.lang.String arg0,java.lang.String arg1,java.lang.String arg2,java.lang.String arg3,java.lang.String arg4) { 
        LOG.info("Executing operation validateDQA");
        System.out.println(arg0);
        System.out.println(arg1);
        System.out.println(arg2);
        System.out.println(arg3);
        System.out.println(arg4);
        try {
            java.lang.String _return = "_return1399050454";
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see gov.nist.healthcare.hl7ws.messagevalidation.MessageValidationV2Interface#validate(java.lang.String  arg0 ,)java.lang.String  arg1 ,)java.lang.String  arg2 ,)java.lang.String  arg3 )*
     */
    public java.lang.String validate(java.lang.String arg0,java.lang.String arg1,java.lang.String arg2,java.lang.String arg3) { 
        LOG.info("Executing operation validate");
        System.out.println(arg0);
        System.out.println(arg1);
        System.out.println(arg2);
        System.out.println(arg3);
        try {
            java.lang.String _return = "_return-986068296";
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see gov.nist.healthcare.hl7ws.messagevalidation.MessageValidationV2Interface#getServiceStatus(*
     */
    public java.lang.String getServiceStatus() { 
        LOG.info("Executing operation getServiceStatus");
        try {
            java.lang.String _return = "_return-2106366610";
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
