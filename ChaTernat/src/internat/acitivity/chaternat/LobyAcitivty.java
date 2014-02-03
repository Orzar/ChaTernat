package internat.acitivity.chaternat;


import internat.chaternat.R;
import internat.dbInit.chaternat.CustomAsyncTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class LobyAcitivty extends Activity {
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loby);       
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loby_acitivty, menu);
        return true;
    }
    
    public void initiateOnClick(View view){
		CustomAsyncTask myCustomTask = new CustomAsyncTask(this);
		myCustomTask.execute();
    }
    
    public void randomOnClick (View view){
    	Intent it_random = new Intent(LobyAcitivty.this, QuestionActivity.class);
		startActivity(it_random);
    }
    
    public void serieOnClick(View view){
    	Intent it_serie = new Intent(LobyAcitivty.this, SerieActivity.class);
    	startActivity(it_serie);
    }
}
