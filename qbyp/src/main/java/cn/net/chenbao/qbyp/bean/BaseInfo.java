package cn.net.chenbao.qbyp.bean;


public class BaseInfo {
    protected long VenderId;
    protected String VenderName;
    protected boolean isChoosed;

    public BaseInfo() {
        super();
    }

    public BaseInfo(long id, String name) {
        super();
        VenderId = id;
        VenderName = name;

    }

    public long getVenderId() {
        return VenderId;
    }

    public String getVenderName() {
        return VenderName;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
    }

}
