package co.com.ies.pruebas.webservice;

public class Greeting {

    private final long id;
    private final String content;
    private final String ip;

    public Greeting(long id, String content, String ip) {
        this.id = id;
        this.content = content;
        this.ip = ip;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getIp(){
        return ip;
    }
}
