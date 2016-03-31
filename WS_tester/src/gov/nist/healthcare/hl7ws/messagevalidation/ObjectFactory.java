
package gov.nist.healthcare.hl7ws.messagevalidation;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.nist.healthcare.hl7ws.messagevalidation package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetServiceStatus_QNAME = new QName("http://messagevalidation.hl7ws.healthcare.nist.gov/", "getServiceStatus");
    private final static QName _GetServiceStatusResponse_QNAME = new QName("http://messagevalidation.hl7ws.healthcare.nist.gov/", "getServiceStatusResponse");
    private final static QName _LoadResource_QNAME = new QName("http://messagevalidation.hl7ws.healthcare.nist.gov/", "loadResource");
    private final static QName _LoadResourceResponse_QNAME = new QName("http://messagevalidation.hl7ws.healthcare.nist.gov/", "loadResourceResponse");
    private final static QName _Validate_QNAME = new QName("http://messagevalidation.hl7ws.healthcare.nist.gov/", "validate");
    private final static QName _ValidateDQA_QNAME = new QName("http://messagevalidation.hl7ws.healthcare.nist.gov/", "validateDQA");
    private final static QName _ValidateDQAResponse_QNAME = new QName("http://messagevalidation.hl7ws.healthcare.nist.gov/", "validateDQAResponse");
    private final static QName _ValidateResponse_QNAME = new QName("http://messagevalidation.hl7ws.healthcare.nist.gov/", "validateResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.nist.healthcare.hl7ws.messagevalidation
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetServiceStatus }
     * 
     */
    public GetServiceStatus createGetServiceStatus() {
        return new GetServiceStatus();
    }

    /**
     * Create an instance of {@link GetServiceStatusResponse }
     * 
     */
    public GetServiceStatusResponse createGetServiceStatusResponse() {
        return new GetServiceStatusResponse();
    }

    /**
     * Create an instance of {@link LoadResource }
     * 
     */
    public LoadResource createLoadResource() {
        return new LoadResource();
    }

    /**
     * Create an instance of {@link LoadResourceResponse }
     * 
     */
    public LoadResourceResponse createLoadResourceResponse() {
        return new LoadResourceResponse();
    }

    /**
     * Create an instance of {@link Validate }
     * 
     */
    public Validate createValidate() {
        return new Validate();
    }

    /**
     * Create an instance of {@link ValidateDQA }
     * 
     */
    public ValidateDQA createValidateDQA() {
        return new ValidateDQA();
    }

    /**
     * Create an instance of {@link ValidateDQAResponse }
     * 
     */
    public ValidateDQAResponse createValidateDQAResponse() {
        return new ValidateDQAResponse();
    }

    /**
     * Create an instance of {@link ValidateResponse }
     * 
     */
    public ValidateResponse createValidateResponse() {
        return new ValidateResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetServiceStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", name = "getServiceStatus")
    public JAXBElement<GetServiceStatus> createGetServiceStatus(GetServiceStatus value) {
        return new JAXBElement<GetServiceStatus>(_GetServiceStatus_QNAME, GetServiceStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetServiceStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", name = "getServiceStatusResponse")
    public JAXBElement<GetServiceStatusResponse> createGetServiceStatusResponse(GetServiceStatusResponse value) {
        return new JAXBElement<GetServiceStatusResponse>(_GetServiceStatusResponse_QNAME, GetServiceStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadResource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", name = "loadResource")
    public JAXBElement<LoadResource> createLoadResource(LoadResource value) {
        return new JAXBElement<LoadResource>(_LoadResource_QNAME, LoadResource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadResourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", name = "loadResourceResponse")
    public JAXBElement<LoadResourceResponse> createLoadResourceResponse(LoadResourceResponse value) {
        return new JAXBElement<LoadResourceResponse>(_LoadResourceResponse_QNAME, LoadResourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Validate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", name = "validate")
    public JAXBElement<Validate> createValidate(Validate value) {
        return new JAXBElement<Validate>(_Validate_QNAME, Validate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateDQA }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", name = "validateDQA")
    public JAXBElement<ValidateDQA> createValidateDQA(ValidateDQA value) {
        return new JAXBElement<ValidateDQA>(_ValidateDQA_QNAME, ValidateDQA.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateDQAResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", name = "validateDQAResponse")
    public JAXBElement<ValidateDQAResponse> createValidateDQAResponse(ValidateDQAResponse value) {
        return new JAXBElement<ValidateDQAResponse>(_ValidateDQAResponse_QNAME, ValidateDQAResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://messagevalidation.hl7ws.healthcare.nist.gov/", name = "validateResponse")
    public JAXBElement<ValidateResponse> createValidateResponse(ValidateResponse value) {
        return new JAXBElement<ValidateResponse>(_ValidateResponse_QNAME, ValidateResponse.class, null, value);
    }

}
