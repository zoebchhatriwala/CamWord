package team7663.project.camword;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	
	@Override
	protected void onDestroy()
	{
		preferences.edit().putBoolean("team7663.project.camword.OCR.mainoff",true).apply();
		super.onDestroy();
	}

	@Override
	protected void onPause() 
	{
		preferences.edit().putBoolean("team7663.project.camword.OCR.mainoff",false).apply();
		super.onPause();
	}

	static ImageButton ocr;
	ImageButton speech;
	static ImageButton spell;
	ImageButton settings;
	boolean speechclickcheck=false;
	protected SharedPreferences preferences;
	protected Intent svc;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        ocr = (ImageButton) findViewById(R.id.ocr);
		speech = (ImageButton) findViewById(R.id.speech);
		spell = (ImageButton) findViewById(R.id.spell);
		settings = (ImageButton) findViewById(R.id.settings);
		preferences = this.getSharedPreferences("team7663.project.camword",Context.MODE_PRIVATE);
		ocr.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				ocr.setBackgroundResource(R.drawable.ocr_clicked);
				Intent newactivity = new Intent("team7663.project.camword.OCR");
				startActivity(newactivity);
			}
		});
		boolean temp = preferences.getBoolean("team7663.project.camword.OCR.key",false);
		if(temp==true)
		{
			speech.setBackgroundResource(R.drawable.speech_clicked);
			speechclickcheck=true;
			svc = new Intent(this,Overlay.class);
	        startService(svc);
		}
        speech.setOnClickListener(new View.OnClickListener()
        {
			public void onClick(View v){
				if(speechclickcheck==false)
				{
				speech.setBackgroundResource(R.drawable.speech_clicked);
				speechclickcheck=true;
				preferences.edit().putBoolean("team7663.project.camword.OCR.key",true).apply();
				notifycall(true);
				}
				else
				{
					speech.setBackgroundResource(R.drawable.speech);
					speechclickcheck=false;
					preferences.edit().putBoolean("team7663.project.camword.OCR.key",false).apply();
					notifycall(false);
				}
			}
		});
        spell.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v){
				spell.setBackgroundResource(R.drawable.spell_clicked);
				Intent newactivity = new Intent("team7663.project.camword.SPELLCHECKER");
				startActivity(newactivity);
			}
		});
        settings.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent newactivity = new Intent("team7663.project.camword.SETTINGS");
				startActivity(newactivity);
			}
		});
         
	}
	
	
	@Override
	protected void onResume()
	{
		boolean finish=preferences.getBoolean("team7663.project.camword.OCR.finish",false);
		boolean mainoff=preferences.getBoolean("team7663.project.camword.OCR.mainoff",false);
		if(finish==true && mainoff==false)
		{
			preferences.edit().putBoolean("team7663.project.camword.OCR.finish",false).apply();
			finish();
		}
		super.onResume();
	}


	public void notifycall(boolean fix)
	{
	if(fix==true)
	{
		svc = new Intent(this,Overlay.class);
        startService(svc);
	}
	else
	{
       stopService(svc);
	}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
