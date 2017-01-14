package Parser;


/* A context represents the entire scope of an application, i.e.
 * we can assign "global variables" to a context.
 *
 * This is a "bean class", containing a constructor and accessor
 * methods only.
 */
public class Context {
    public static final String CONF_FILE="conf.xml";
    private String host;
    private int port;
    private String schema;
    private Driveable driver;

    public Context() {
        super();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Driveable getDriver() {
        return driver;
    }

    public void setDriver(Driveable driver) {
        this.driver = driver;
    }

    @Override
    public String toString() {
        return "Context [host=" + host + ", port=" + port + ", schema=" + schema + ", driver=" + driver + "]";
    }
}
