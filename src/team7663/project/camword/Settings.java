package team7663.project.camword;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends Activity{
	
	private Spinner spinner1;
    private Button save,about;
    private CheckBox transselect;
	protected SharedPreferences preferences;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
		preferences = this.getSharedPreferences("team7663.project.camword",Context.MODE_PRIVATE);
		preferences.edit().putBoolean("team7663.project.camword.OCR.mainoff",true).apply();
        spinner1 = (Spinner) findViewById(R.id.spin);
        transselect = (CheckBox) findViewById(R.id.checkBox1);
        if((preferences.getBoolean("team7663.project.camword.OCR.translateon",false))==true)
        {
        	transselect.setChecked(true);
        }
        else
        {
        	transselect.setChecked(false);
        }
        List<String> list = new ArrayList<String>();
        list.add("english");
        list.add("arabic");
        list.add("french");
        list.add("german");
        list.add("spanish");
        list.add("hindi");
        list.add("filipino");
        list.add("chinese");
        list.add("dutch");
        list.add("croatian");
        list.add("italian");
        list.add("japanese");
        list.add("latin");
        list.add("russain");
        list.add("georgian");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
        save = (Button) findViewById(R.id.save);
        about = (Button) findViewById(R.id.about);
        int position=preferences.getInt("team7663.project.camword.OCR.lanposition",0);
        spinner1.setSelection(position);
        save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				preferences.edit().putString("team7663.project.camword.OCR.language",spinner1.getSelectedItem().toString()).apply();
				preferences.edit().putInt("team7663.project.camword.OCR.lanposition",spinner1.getSelectedItemPosition()).apply();
				if(transselect.isChecked()==true)
				{
					preferences.edit().putBoolean("team7663.project.camword.OCR.translateon",true).apply();
				}
				else
				{
					preferences.edit().putBoolean("team7663.project.camword.OCR.translateon",false).apply();
				}
				Toast.makeText(Settings.this,"Saved", Toast.LENGTH_SHORT).show();
			}
		});
        about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dlgAlert;
				dlgAlert = new AlertDialog.Builder(Settings.this);
		        dlgAlert.setTitle(R.string.app_name);
				dlgAlert.setCancelable(true);
				dlgAlert.setIcon(R.drawable.ic_launcher);
				dlgAlert.setPositiveButton("Done",new android.content.DialogInterface.OnClickListener()
				{	
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						
					}
				});
				dlgAlert.setMessage("CamWord is an ultimate application providing dictionary and translating functionalities taking inputs by various means.\n\nApplication developed by : \nZoeb Chhatriwala\n\nContribution by :\n1. Hardik Patel\n2. Kishan Patel");
				dlgAlert.create().show();
			}
		});
        
        Button share = (Button) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener()
        {
			
			@Override
			public void onClick(View v)
			{
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
			    sharingIntent.setType("text/plain");
			    String shareBody = "CamWord is an ultimate application providing dictionary and translating functionalities taking inputs by various means.\nDownload now : \nhttp://camword.cf";
			    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CamWord");
			    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			    startActivity(Intent.createChooser(sharingIntent, "Share via"));
				
			}
		});
	}
}
