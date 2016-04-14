package group1.com.casper_android_client;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pontuspohl on 07/12/15.
 */
public class User {

    private JSONObject userToken;
    private JSONObject userinfo;
    private String name;

//    public User(JSONObject jsonObject) throws JSONException {
//        this.userToken = jsonObject;
//        this.userinfo = userToken.getJSONObject("user");
//        System.out.println(userinfo.getString("username"));
//    }

    public User(JSONObject jsonObject,String name) throws JSONException{
        this.userToken = jsonObject;
    }

    public JSONObject getUserToken() {
        return userToken;
    }

    public JSONObject getUserinfo() {
        return userinfo;
    }

    public void setUserToken(JSONObject userToken) {
        this.userToken = userToken;
    }

    public void setUserinfo(JSONObject userinfo) {
        this.userinfo = userinfo;
    }

    public String getToken() throws JSONException {
        String token = this.userToken.getString("token");
        return token;
    }

    @Override
    public String toString(){
        String stringRepresentation = "{" + this.name + "} , {" + this.userToken + "}";
        return stringRepresentation;
    }

}
