package walkingschoolbus.cmpt276.ca.dataObjects;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;


/**
 * Created by Jonathan Chen on 2018/3/11.
 */

public class ServerManager {
    private static ApiInterface proxy;
    private static User userManager = User.getInstance();
//    private static final String APIKEY ="E14DEF58-61CB-4425-B6FB-BDBD807E44CF ";
    private static boolean login = false;
    private static Context currentContext;


    //setLogin
    public static void setDoLogin(boolean login){
        ServerManager.login = login;
    }

    public static void connectToServerWithoutToken(Context context){
        currentContext = context;
        proxy = ProxyBuilder.getProxy(String.valueOf(R.string.apiKey),null);
    }
    public static void connectToServerWithToken(Context context){
        currentContext = context;
        proxy = ProxyBuilder.getProxy(String.valueOf(R.string.apiKey),userManager.getToken());
    }

    //register
    public static void createNewUser(User user)
    {
        Call<User> caller = proxy.createNewUser(user);
        ProxyBuilder.callProxy(currentContext,caller,returnedUser->responseRegister(returnedUser));
    }
    private static void responseRegister(User user){
        String TAG = "Server";
        Log.w(TAG, "Server replied with user: " + user.toString());
    }
    //refresh token
    public static void refreshToken(){
        ProxyBuilder.setOnTokenReceiveCallback(token->onReceiveToken(token));
    }

    private static  void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        String TAG = "Proxy";
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        userManager.setToken(token);
        proxy = ProxyBuilder.getProxy(String.valueOf(R.string.apiKey),userManager.getToken());
    }
    //login


    public static boolean doLogin(){
        return login;
    }

    public static void Login(ProxyBuilder.SimpleCallback<Void> callback){
        Call<Void> caller = proxy.login(userManager.getUser());
        ProxyBuilder.callProxy(currentContext,caller,callback);
    }

    public static void getUserByEmail(ProxyBuilder.SimpleCallback<User> callback){

        Call<User> caller = proxy.getUserByEmail(userManager.getEmail());
        ProxyBuilder.callProxy(currentContext,caller,callback);

    }

    //part one for parentlist
    public static void LoginInitilizePartOne(ProxyBuilder.SimpleCallback<List<User>> callback){
        Call<List<User>> callerForResetParent = proxy.getMonitoredByUser(userManager.getId());
        ProxyBuilder.callProxy(currentContext,callerForResetParent,callback);
    }
    //part two for child list
    public static void LoginInitilizePartTwo(ProxyBuilder.SimpleCallback<List<User>> callback){
        Call<List<User>> callerForResetChild = proxy.getMonitorUser(userManager.getId());
        ProxyBuilder.callProxy(currentContext,callerForResetChild,callback);
    }

    //for add child
    public static void addMonitorUser (String email,ProxyBuilder.SimpleCallback<User> callback){
        Call<User> callerForEmail = proxy.getUserByEmail(email);
        ProxyBuilder.callProxy(currentContext, callerForEmail,callback);
    }

    public static void addChild(User user,ProxyBuilder.SimpleCallback<List<User>> callback) {
        long userId = user.getId();

        Map<String,Long> body = new HashMap<String, Long>();
        body.put("id", userId);

        Call<List<User>> callerForAdd = proxy.addMonitorUsers(userManager.getId(), body);
        ProxyBuilder.callProxy(currentContext, callerForAdd, callback);
    }



    //for add parent
    public static void addMonitedByUser(String email,ProxyBuilder.SimpleCallback<User> callback){
        Call<User> callerForEmail = proxy.getUserByEmail(email);
        ProxyBuilder.callProxy(currentContext, callerForEmail, callback);
    }
    public static void addParent(User user,ProxyBuilder.SimpleCallback<List<User>> callback ){
        Long userId = user.getId();
        Map<String,Long> body = new HashMap<String, Long>();
        body.put("id", userId);

        Call<List<User>> callerForAdd = proxy.addMonitoredByUsers(userManager.getId(), body);
        ProxyBuilder.callProxy(currentContext, callerForAdd, callback);
    }


    //for delete child

    public static void deleteMoniterUser(Long userId,ProxyBuilder.SimpleCallback<Void> callback){
        Call<Void> callerForDelete = proxy.deleteMonitorUser(userManager.getId(),userId);
        ProxyBuilder.callProxy(currentContext, callerForDelete,callback);
    }

    public static void deleteChild(ProxyBuilder.SimpleCallback<List<User>> callback){
        Call<List<User>> callerForReset =proxy.getMonitorUser(userManager.getId());
        ProxyBuilder.callProxy(currentContext, callerForReset,callback);
    }


    // for delete parent
    public static void deleteMonitoredByUser(Long userId,ProxyBuilder.SimpleCallback<Void> callback){
        Call<Void> callerForDelete = proxy.deleteMonitoredByUser(userManager.getId(),userId);
        ProxyBuilder.callProxy(currentContext,callerForDelete,callback);

    }
    public static void  deleteParent(ProxyBuilder.SimpleCallback<List<User>> callback){
        Call<List<User>> callerForReset =proxy.getMonitoredByUser(userManager.getId());
        ProxyBuilder.callProxy(currentContext,callerForReset,callback);
    }




}
