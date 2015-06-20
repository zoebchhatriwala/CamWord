package team7663.project.camword;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class translate
{
	public String trans(String text, String source, String destination)
	{
		String result = null;
        try {
        	text=text.replace("\n"," ");
            text=text.replace(" ","%20");
            String urlStr = "https://www.googleapis.com/language/translate/v2?key=" + "AIzaSyA-AyEsG8goTqRv1g7S9ZQ0e5FCz6ShB4k" + "&source=" + source + "&target=" + destination + "&q=" + text;
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            InputStream stream;
            if (conn.getResponseCode() == 200) //success
            {
                stream = conn.getInputStream();
            }
            else
            {
                stream = conn.getErrorStream();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            int x=0;
            while ((line = reader.readLine()) != null) 
            {
             if(x==4)
             {
            	 result=line;
             }
             x++;
            }
        }
        catch (Exception e)
        {
        	return "Network Error!!! Please Check Your Connection";
        }
        result=result.replace("\"","");
        result=result.replace("translatedText:","");
        result=result.trim();
        return result;
	}
}
