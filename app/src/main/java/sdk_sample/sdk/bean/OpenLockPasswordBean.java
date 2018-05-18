package sdk_sample.sdk.bean;

/**
 * Created by Android on 2017/3/23.
 */


public class OpenLockPasswordBean {
    private String random_pw;
    private long randomkey_dead_time;

    public OpenLockPasswordBean() {
    }

    public String getRandom_pw() {
        return this.random_pw;
    }

    public void setRandom_pw(String random_pw) {
        this.random_pw = random_pw;
    }

    public long getRandomkey_dead_time() {
        return this.randomkey_dead_time;
    }

    public void setRandomkey_dead_time(long randomkey_dead_time) {
        this.randomkey_dead_time = randomkey_dead_time;
    }

    public String toString() {
        return "OpenLockBean [random_pw=" + this.random_pw + ", randomkey_dead_time=" + this.randomkey_dead_time + "]";
    }
}
