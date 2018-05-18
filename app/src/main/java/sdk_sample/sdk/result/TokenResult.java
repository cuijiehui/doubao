package sdk_sample.sdk.result;

/**
 * Created by Android on 2017/3/23.
 */

public class TokenResult extends BaseResult {
    private String token;
    private long dead_time;

    public TokenResult() {
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getDead_time() {
        return this.dead_time;
    }

    public void setDead_time(long dead_time) {
        this.dead_time = dead_time;
    }

    public String toString() {
        return "TokenResult [token=" + this.token + ", dead_time=" + this.dead_time + "]";
    }
}
