package lab4;

public class Group {
    private String realSender;
    private String fakeSender;
    private String[] victims;

    public Group(String realSender, String fakeSender, String[] victims) {
        this.realSender = realSender;
        this.fakeSender = fakeSender;
        this.victims = victims;
    }

    public String getRealSender() {
        return realSender;
    }

    public void setRealSender(String realSender) {
        this.realSender = realSender;
    }

    public String getFakeSender() {
        return fakeSender;
    }

    public void setFakeSender(String fakeSender) {
        this.fakeSender = fakeSender;
    }

    public String[] getVictims() {
        return victims;
    }

    public void setVictims(String[] victims) {
        this.victims = victims;
    }
}
