package team7663.project.camword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import android.content.Context;
import android.content.res.AssetManager;


public class dictionaryClass {

	private Context mcon;
	public String firstWord(String Word)
	{
	    Word=Word.replace("\n"," ");
		String arr[] = Word.split(" ", 2);
		return arr[0].toString();
	}
	
	public String defineWord(String Word,Context mcon)
	{
		 this.mcon = mcon;
	     Word=Word.replace(" ","");
	     Word=Word.replace("\n","");
	     Word=Word.substring(0, 1).toUpperCase(new Locale("en")) + Word.substring(1);
	     Word=String.format("%s%s","#",Word);
	     return searchInDictionary(Word);
	}
	
	private String searchInDictionary(String Word)
	{
		AssetManager assetManager = mcon.getAssets();
		BufferedReader reader;
		String Text="";
		try
		{
			InputStream in = assetManager.open("Dictionary.txt");
			reader = new BufferedReader(new InputStreamReader(in));
			while ((Text = reader.readLine()) != null)
			{
				if(Text.contains(Word)==true)
				{
					Text=Text.replace(Word,"");
					Word=Word.replace("#","");
					Text=String.format("%s: %s",Word,Text);
					Text=Text.trim();
					break;
				}
			}
			if (Text==null)
			{
				Word=Word.replace("#","");
				Word=Word.toLowerCase(new Locale("en"));
			    return String.format("%s not found!!!",Word);
			}
			else
			{
			return Text;
			}
		} 
		catch(IOException e) 
		{
			return "error in dictinary!!!";
		}
	}
}
