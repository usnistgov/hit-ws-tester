package gov.nist.healthcare.hl7ws.messagevalidation;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.0.5
 * 2015-05-18T13:45:55.841-04:00
 * Generated source version: 3.0.5
 * 
 */
@WebService(targetNamespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", name = "MessageValidationV2Interface")
@XmlSeeAlso({ObjectFactory.class})
public interface MessageValidationV2Interface {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "loadResource", targetNamespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", className = "gov.nist.healthcare.hl7ws.messagevalidation.LoadResource")
    @WebMethod
    @ResponseWrapper(localName = "loadResourceResponse", targetNamespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", className = "gov.nist.healthcare.hl7ws.messagevalidation.LoadResourceResponse")
    public java.lang.String loadResource(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        java.lang.String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        java.lang.String arg2
    );

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "validate", targetNamespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", className = "gov.nist.healthcare.hl7ws.messagevalidation.Validate")
    @WebMethod
    @ResponseWrapper(localName = "validateResponse", targetNamespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", className = "gov.nist.healthcare.hl7ws.messagevalidation.ValidateResponse")
    public java.lang.String validate(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        java.lang.String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        java.lang.String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        java.lang.String arg3
    );

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getServiceStatus", targetNamespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", className = "gov.nist.healthcare.hl7ws.messagevalidation.GetServiceStatus")
    @WebMethod
    @ResponseWrapper(localName = "getServiceStatusResponse", targetNamespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", className = "gov.nist.healthcare.hl7ws.messagevalidation.GetServiceStatusResponse")
    public java.lang.String getServiceStatus();
}