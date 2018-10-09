package usp.kanban.client;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Cookie{

    private static JSONObject cookie;

    public static String readCookie(String key) throws Exception{
        if(cookie == null) cookie = getCookie();
        Object obj = cookie.get(key);
        
        if(obj == null) return null;
        else return obj.toString();

    }

    private static JSONObject getCookie() throws Exception{
        JSONParser parser = new JSONParser();
        String path = Cookie.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "usp/kanban/client/cookie.json";
        FileReader file = new FileReader(path);
        try {
            Object obj = parser.parse(file);
            JSONObject json = (JSONObject) obj;
            return json;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally{
            file.close();
        }
    }

    public static void addSessionCookie(String key) throws IOException{
        if(cookie.get("SessionID") == null){
            cookie.put("SessionID", key);
        }
        else{
            cookie.replace("SessionID", key);
        }
        saveCookie();
    }

    private static void saveCookie() throws IOException{
        String path = Cookie.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "usp/kanban/client/cookie.json";
        FileWriter file = new FileWriter(path);
        try{
            file.write(cookie.toJSONString());
        }
        catch(Exception ex){
            
        }
        finally{
            file.flush();
            file.close();
        }
    }
}