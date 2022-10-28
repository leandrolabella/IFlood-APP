package br.com.devleo.tcc.model;

public class Warning {

    private String id;
    private String msg;
    private Locale locale;
    private boolean confirm;

    public Warning() {
    }

    public Warning(String id, String msg, Locale locale, boolean confirm) {
        this.id = id;
        this.msg = msg;
        this.locale = locale;
        this.confirm = confirm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    @Override
    public String toString() {
        return "Warning{" +
                "id='" + id + '\'' +
                ", msg='" + msg + '\'' +
                ", locale=" + locale +
                ", confirm=" + confirm +
                '}';
    }
}
