package group1.com.casper_android_client;

/**
 * Created by Andreas Fransson
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();
    private String userName;
    private String passWord;

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public static Singleton getInstance() {
        return ourInstance;
    }

    public static void setOurInstance(Singleton ourInstance) {
        Singleton.ourInstance = ourInstance;
    }


    private Singleton() {

    }
}
