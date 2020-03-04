package environment;
public interface AddressesInterface{
    public static final String http_port = System.getenv("HTTP_PORT");
    public static final String http_address = System.getenv("HTTP_ADDRESS");
    public static final String http_completeAddress= "http://" + http_address +":" +http_port;
}