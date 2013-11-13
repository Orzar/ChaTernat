package internat.acitivity.chaternat;

import internat.chaternat.R;
import internat.db.chaternat.Answer;
import internat.db.chaternat.DaoMaster;
import internat.db.chaternat.DaoMaster.DevOpenHelper;
import internat.db.chaternat.DaoSession;
import internat.db.chaternat.Question;
import internat.db.chaternat.QuestionDao;
import internat.db.chaternat.QuestionDao.Properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import de.greenrobot.dao.query.QueryBuilder;

public class QuestionActivity extends Activity {

	
	private SQLiteDatabase db;
	
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	
	private QuestionDao questionDao;
	private Question chosenQuestion;
	private List<Answer> answers;
	
	private List<String> rightAnswers = new ArrayList<String>();
	private List<String> answerChecked = new ArrayList<String>();
	
	public TextView txtQuestion;
	public CheckBox chkA;
	public CheckBox chkB;
	public CheckBox chkC;
	public CheckBox chkD;
	public CheckBox chkE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "internat-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        
        questionDao = daoSession.getQuestionDao();
		txtQuestion = (TextView) findViewById(R.id.txt_question);
		chkA = (CheckBox) findViewById(R.id.chk_answer_a);
		chkB = (CheckBox) findViewById(R.id.chk_answer_b);
		chkC = (CheckBox) findViewById(R.id.chk_answer_c);
		chkD = (CheckBox) findViewById(R.id.chk_answer_d);
		chkE = (CheckBox) findViewById(R.id.chk_answer_e);
		
		//Sometimes, question are too long, we have some scroll in case it's needed.
		txtQuestion.setMovementMethod(new ScrollingMovementMethod());
		
		long countQuestion = questionDao.queryBuilder().count();
		
		// Generate a random number between 1 and max question to select one randomly
		Random rand = new Random();
		int nombreAleatoire = rand.nextInt((int)countQuestion) + 1;
		
		QueryBuilder<Question> qb = questionDao.queryBuilder();
		qb.where(Properties.Id.eq(nombreAleatoire));
		
		List<Question> questions = qb.list();	
		chosenQuestion = (Question) questions.get(0);
		
		answers = chosenQuestion.getAnswerList();
		
		for (Answer ans : answers){
			if (ans.getSolution())
				rightAnswers.add(ans.getLetter());
		}
		
		txtQuestion.setText(chosenQuestion.getQuestionNumber() + ". " +chosenQuestion.getText());
		for (Answer answer : answers) {
			if (answer.getLetter().equalsIgnoreCase(Answer.ANSWER_A))
				chkA.setText(answer.getLetter() + ") " + answer.getText());
			if (answer.getLetter().equalsIgnoreCase(Answer.ANSWER_B))
				chkB.setText(answer.getLetter() + ") " + answer.getText());
			if (answer.getLetter().equalsIgnoreCase(Answer.ANSWER_C))
				chkC.setText(answer.getLetter() + ") " + answer.getText());
			if (answer.getLetter().equalsIgnoreCase(Answer.ANSWER_D))
				chkD.setText(answer.getLetter() + ") " + answer.getText());
			if (answer.getLetter().equalsIgnoreCase(Answer.ANSWER_E))
				chkE.setText(answer.getLetter() + ") " + answer.getText());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

	
	public void onSubmitClick(View view) {
		Collections.sort(answerChecked);
        Collections.sort(rightAnswers);
        
        if (answerChecked.equals(rightAnswers)){
        	ImageView imgStatus = (ImageView) findViewById(R.id.img_results);
        	imgStatus.setImageResource(R.drawable.ic_success);
        	Button btnNext = (Button) findViewById(R.id.btn_skip);
        	btnNext.setText(R.string.lbl_next);
        }
        else{
        	ImageView imgStatus = (ImageView) findViewById(R.id.img_results);
        	imgStatus.setImageResource(R.drawable.ic_fail);
        }
        
    }
	
	public void onCheckBoxClick(View view){
		CheckBox cb = (CheckBox) view;
		switch (cb.getId()) {
		case R.id.chk_answer_a:
			if (cb.isChecked())
				answerChecked.add(Answer.ANSWER_A);
			if (!cb.isChecked())
				answerChecked.remove(Answer.ANSWER_A);
			break;
		case R.id.chk_answer_b:
			if (cb.isChecked())
				answerChecked.add(Answer.ANSWER_B);
			if (!cb.isChecked())
				answerChecked.remove(Answer.ANSWER_B);
			break;
		case R.id.chk_answer_c:
			if (cb.isChecked())
				answerChecked.add(Answer.ANSWER_C);
			if (!cb.isChecked())
				answerChecked.remove(Answer.ANSWER_C);
			break;
		case R.id.chk_answer_d:
			if (cb.isChecked())
				answerChecked.add(Answer.ANSWER_D);
			if (!cb.isChecked())
				answerChecked.remove(Answer.ANSWER_D);
			break;
		case R.id.chk_answer_e:
			if (cb.isChecked())
				answerChecked.add(Answer.ANSWER_E);
			if (!cb.isChecked())
				answerChecked.remove(Answer.ANSWER_E);
			break;
		default:
			break;
		}
	}
	
	public void onSkipClick(View view){
		finish();
		startActivity(getIntent());
	}
	
	public void onCorrectionClick(View view){
		chkA.setChecked(false);
		chkB.setChecked(false);
		chkC.setChecked(false);
		chkD.setChecked(false);
		chkE.setChecked(false);
		for (String answer : rightAnswers){
			if (answer.equals(Answer.ANSWER_A))
				chkA.setChecked(true);
			else if (answer.equals(Answer.ANSWER_B))
				chkB.setChecked(true);
			else if (answer.equals(Answer.ANSWER_C))
				chkC.setChecked(true);
			else if (answer.equals(Answer.ANSWER_D))
				chkD.setChecked(true);
			else if (answer.equals(Answer.ANSWER_E))
				chkE.setChecked(true);
		}
		Button btnSkip = (Button) findViewById(R.id.btn_skip);
		btnSkip.setText(R.string.lbl_next);
		Button btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setEnabled(false);
		
	}
	
}
