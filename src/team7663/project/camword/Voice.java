package team7663.project.camword;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;

public class Voice extends Activity {
	
	String resultString;
	protected dictionaryClass dictionary = new dictionaryClass();
	protected translate translate = new translate();
	protected AlertDialog.Builder dlgAlert;
	protected SharedPreferences preferences;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		preferences = this.getSharedPreferences("team7663.project.camword",Context.MODE_PRIVATE);
        dlgAlert = new AlertDialog.Builder(Voice.this);
        dlgAlert.setTitle(R.string.app_name);
		dlgAlert.setCancelable(true);
		dlgAlert.setPositiveButton("Done",new android.content.DialogInterface.OnClickListener()
		{	
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}
		});
		dlgAlert.setOnDismissListener(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog)
			{
				finish();
			}
		});
		startAct();
	}

	protected void startAct()
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Word");
		try
		{
			startActivityForResult(intent,100);
		}
		catch (Exception ex)
		{
		    Toast.makeText(this,"Unavailable",Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
			if (resultCode == RESULT_OK && requestCode == 100)
			{
				try
				{
				ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				resultString = result.toString();
				resultString = resultString.replace("[","");
				resultString = resultString.replace("]","");
				resultString = resultString.replace(","," ");
				int langpos = preferences.getInt("team7663.project.camword.OCR.lanposition",0);
				if((preferences.getBoolean("team7663.project.camword.OCR.translateon",false))==true)
				{
					if(langpos == 1 )
			    	{
			    	    Toast.makeText(Voice.this,String.format("Translating to ARABIC"),Toast.LENGTH_SHORT).show();
					    resultString = translate.trans(dictionary.firstWord(resultString),"en","ar");
			    	}
			    	else if(langpos == 2)
			    	{
			            Toast.makeText(Voice.this,String.format("Translating to FRENCH"), Toast.LENGTH_SHORT).show();
			            resultString = translate.trans(dictionary.firstWord(resultString),"en","fr");
			    	}
			    	else if(langpos == 3)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to GERMAN"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","de");
			    	}
			    	else if(langpos == 4)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to SPANISH"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","es");
			    	}
			    	else if(langpos == 5)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to HINDI"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","hi");
			    	}
			    	else if(langpos == 6)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to FILIPINO"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","tl");
			    	}
			    	else if(langpos == 7)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to CHINESE"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","zh-CN");
			    	}
			    	else if(langpos == 8)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to DUTCH"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","nl");
			    	}
			    	else if(langpos == 9)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to CROATIAN"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","hr");
			    	}
			    	else if(langpos == 10)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to ITALIAN"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","it");
			    	}
			    	else if(langpos == 11)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to JAPANESE"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","ja");
			    	}
			    	else if(langpos == 12)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to LATIN"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","la");
			    	}
			    	else if(langpos == 13)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to RUSSIAN"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","ru");
			    	}
			    	else if(langpos == 14)
			    	{
			    		Toast.makeText(Voice.this,String.format("Translating to GEORGIAN"), Toast.LENGTH_SHORT).show();
				        resultString = translate.trans(dictionary.firstWord(resultString),"en","ka");
			    	}
			    	else
			    	{
			    		resultString = "Change language in settings from default english";
			    	}
				}
				else
				{
				resultString = dictionary.defineWord(dictionary.firstWord(resultString),Voice.this);
				}
				dlgAlert.setMessage(resultString);
				dlgAlert.create().show();
				}
				catch(Exception ex)
				{
					//Do Nothing
				}
			}
			else
			{
				finish();
			}
		}
	
	@Override
	protected void onDestroy()
	{
		Overlay.overlayedButton.setBackgroundResource(R.drawable.speech);
		preferences.edit().putBoolean("team7663.project.camword.OCR.finish",true).apply();
		super.onDestroy();
	}
}
