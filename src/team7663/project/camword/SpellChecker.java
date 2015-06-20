package team7663.project.camword;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SpellChecker extends Activity implements SpellCheckerSessionListener {

	Button check;
	EditText checktext;
	TextView suggestbox;
	TextView rightbox;
	String value;
	protected SpellCheckerSession scs;
	protected SharedPreferences preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spellchecker);
		check = (Button) findViewById(R.id.checkerbuttoncheck);
		checktext = (EditText) findViewById(R.id.checkeredittext);
		suggestbox = (TextView) findViewById(R.id.checkersuggestview);
		rightbox = (TextView) findViewById(R.id.righttext);
		preferences = this.getSharedPreferences("team7663.project.camword",Context.MODE_PRIVATE);
		preferences.edit().putBoolean("team7663.project.camword.OCR.mainoff",true).apply();
		checktext.setText(preferences.getString("team7663.project.camword.OCR.savespell",""));
		TextServicesManager tsm = (TextServicesManager) getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
		scs = tsm.newSpellCheckerSession(null,null, SpellChecker.this,true);
		MainActivity.spell.setBackgroundResource(R.drawable.spell);
		check.setOnClickListener(new View.OnClickListener() {
			
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v){
				value = checktext.getText().toString();
				rightbox.setText("");
				if(value.isEmpty()==false)
				{
				scs.getSuggestions(new TextInfo(value), 5);
				}
				}
		});
    }
	@Override
	public void onGetSuggestions(SuggestionsInfo[] results) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<results.length ;++i)
		{
			int len = results[i].getSuggestionsCount();
			sb.append('\n');
			for (int j=0; j<len; j++)
			{
				if(value.contentEquals(results[i].getSuggestionAt(j))==true)
				{
				rightbox.setText("Correct");
				}
				else
				{
				sb.append(results[i].getSuggestionAt(j)+"\n");
				}
			}
		}
		suggestbox.setText(sb.toString());
	}
	
	@Override
	protected void onDestroy()
	{
		preferences.edit().putString("team7663.project.camword.OCR.savespell",checktext.getText().toString()).apply();
		super.onDestroy();
	}
	
	@Override
	public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
		
		
	}

}
