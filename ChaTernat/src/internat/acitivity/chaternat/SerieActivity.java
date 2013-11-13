package internat.acitivity.chaternat;

import internat.adapter.chaternat.SerieListViewAdapter;
import internat.chaternat.R;
import internat.db.chaternat.Answer;
import internat.db.chaternat.DaoMaster;
import internat.db.chaternat.DaoMaster.DevOpenHelper;
import internat.db.chaternat.DaoSession;
import internat.db.chaternat.Question;
import internat.db.chaternat.QuestionDao;
import internat.db.chaternat.QuestionDao.Properties;
import internat.models.Notation;
import internat.models.SerieModel;

import java.util.List;
import java.util.Random;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.greenrobot.dao.query.QueryBuilder;

public class SerieActivity extends Activity {

	private SQLiteDatabase db;

	private DaoMaster daoMaster;
	private DaoSession daoSession;

	private QuestionDao questionDao;

	private SerieModel serie;

	private final int QUESTION_IN_SERIE = 2;

	private int indexSerie = 0;

	public ImageButton imgBtnPrevious;
	public ImageButton imgBtnNext;

	public Button btnValidation;
	public Button btnOkScore;

	public TextView txtQuestion;
	public TextView txtQuestionCount;
	public TextView txtScoreRecap;

	public CheckBox chkA;
	public CheckBox chkB;
	public CheckBox chkC;
	public CheckBox chkD;
	public CheckBox chkE;

	public LinearLayout questionLayout;
	public LinearLayout correctionLayout;

	public ExpandableListView correctionList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serie_questions);
		// Show the Up button in the action bar.
		setupActionBar();

		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "internat-db",
				null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		questionDao = daoSession.getQuestionDao();

		imgBtnPrevious = (ImageButton) findViewById(R.id.imgbtn_previous);
		imgBtnNext = (ImageButton) findViewById(R.id.imgbtn_next);

		btnValidation = (Button) findViewById(R.id.btn_submit);

		txtQuestion = (TextView) findViewById(R.id.txt_question);
		txtQuestionCount = (TextView) findViewById(R.id.txt_questionCount);

		chkA = (CheckBox) findViewById(R.id.chk_answer_a);
		chkB = (CheckBox) findViewById(R.id.chk_answer_b);
		chkC = (CheckBox) findViewById(R.id.chk_answer_c);
		chkD = (CheckBox) findViewById(R.id.chk_answer_d);
		chkE = (CheckBox) findViewById(R.id.chk_answer_e);

		// Sometimes, question are too long, we have some scroll in case it's
		// needed.
		txtQuestion.setMovementMethod(new ScrollingMovementMethod());

		correctionList = (ExpandableListView) findViewById(R.id.lstview_resultats);

		initiateSerieModel();

		displayQuestion(indexSerie);

	}

	private void initiateSerieModel() {

		long countQuestion = questionDao.queryBuilder().count();
		Question questionToAdd;

		serie = new SerieModel(QUESTION_IN_SERIE);

		for (int i = 0; i <= QUESTION_IN_SERIE; i++) {
			Random rand = new Random();
			int nombreAleatoire = rand.nextInt((int) countQuestion) + 1;
			QueryBuilder<Question> qb = questionDao.queryBuilder();
			qb.where(Properties.Id.eq(nombreAleatoire));

			List<Question> questions = qb.list();
			questionToAdd = (Question) questions.get(0);

			serie.addQuestion(i, questionToAdd);
		}
	}

	private void displayQuestion(int index) {
		organiseView();
		// check si question not null
		// TODO check content of answer at the index when feeling the answers
		txtQuestion.setText(serie.getQuestion(indexSerie).getQuestionNumber()
				+ ". " + serie.getQuestion(indexSerie).getText());
		for (Answer answer : serie.getAnswer(indexSerie)) {
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
		int currentQuestion = indexSerie + 1;
		int totalQuestion = QUESTION_IN_SERIE + 1;
		txtQuestionCount.setText(currentQuestion + "/" + totalQuestion);
		checkChosenQuestion(index);
	}

	private void checkChosenQuestion(int index) {
		if (serie.getChosenAnswer(index) == null) {
			chkA.setChecked(false);
			chkB.setChecked(false);
			chkC.setChecked(false);
			chkD.setChecked(false);
			chkE.setChecked(false);
		} else {
			if (serie.getChosenAnswer(index).contains(Answer.ANSWER_A))
				chkA.setChecked(true);
			else
				chkA.setChecked(false);
			if (serie.getChosenAnswer(index).contains(Answer.ANSWER_B))
				chkB.setChecked(true);
			else
				chkB.setChecked(false);
			if (serie.getChosenAnswer(index).contains(Answer.ANSWER_C))
				chkC.setChecked(true);
			else
				chkC.setChecked(false);
			if (serie.getChosenAnswer(index).contains(Answer.ANSWER_D))
				chkD.setChecked(true);
			else
				chkD.setChecked(false);
			if (serie.getChosenAnswer(index).contains(Answer.ANSWER_E))
				chkE.setChecked(true);
			else
				chkE.setChecked(false);
		}
	}

	private void organiseView() {
		if (indexSerie == 0) {
			imgBtnPrevious.setVisibility(View.GONE);
			imgBtnNext.setVisibility(View.VISIBLE);
			btnValidation.setVisibility(View.GONE);
			txtQuestionCount.setVisibility(View.VISIBLE);
		} else if (indexSerie == QUESTION_IN_SERIE) {
			imgBtnNext.setVisibility(View.GONE);
			imgBtnPrevious.setVisibility(View.VISIBLE);
			txtQuestionCount.setVisibility(View.GONE);
			btnValidation.setVisibility(View.VISIBLE);
		} else {
			imgBtnNext.setVisibility(View.VISIBLE);
			imgBtnPrevious.setVisibility(View.VISIBLE);
			txtQuestionCount.setVisibility(View.VISIBLE);
			btnValidation.setVisibility(View.GONE);
		}

	}

	public void onCheckBoxClick(View view) {
		CheckBox cb = (CheckBox) view;
		switch (cb.getId()) {
		case R.id.chk_answer_a:
			if (cb.isChecked())
				serie.addChosenAnswer(indexSerie, Answer.ANSWER_A);
			if (!cb.isChecked())
				serie.removeChosenAnswer(indexSerie, Answer.ANSWER_A);
			break;
		case R.id.chk_answer_b:
			if (cb.isChecked())
				serie.addChosenAnswer(indexSerie, Answer.ANSWER_B);
			if (!cb.isChecked())
				serie.removeChosenAnswer(indexSerie, Answer.ANSWER_B);
			break;
		case R.id.chk_answer_c:
			if (cb.isChecked())
				serie.addChosenAnswer(indexSerie, Answer.ANSWER_C);
			if (!cb.isChecked())
				serie.removeChosenAnswer(indexSerie, Answer.ANSWER_C);
			break;
		case R.id.chk_answer_d:
			if (cb.isChecked())
				serie.addChosenAnswer(indexSerie, Answer.ANSWER_D);
			if (!cb.isChecked())
				serie.removeChosenAnswer(indexSerie, Answer.ANSWER_D);
			break;
		case R.id.chk_answer_e:
			if (cb.isChecked())
				serie.addChosenAnswer(indexSerie, Answer.ANSWER_E);
			if (!cb.isChecked())
				serie.removeChosenAnswer(indexSerie, Answer.ANSWER_E);
			break;
		default:
			break;
		}
	}

	public void onCorrectionClick(View view) {
		questionLayout = (LinearLayout) findViewById(R.id.lyt_questions);
		correctionLayout = (LinearLayout) findViewById(R.id.lyt_correction);
		questionLayout.setVisibility(View.GONE);
		correctionLayout.setVisibility(View.VISIBLE);

		correctSerie();

	}

	public void onNextClick(View view) {
		indexSerie++;
		displayQuestion(indexSerie);
	}

	public void onPreviousClick(View view) {
		indexSerie--;
		displayQuestion(indexSerie);
	}

	public void onFirstClick(View view) {
		indexSerie = 0;
		displayQuestion(indexSerie);
	}

	public void onLastClick(View view) {
		indexSerie = QUESTION_IN_SERIE;
		displayQuestion(indexSerie);
	}

	public void scoreOkClick(View view) {

	}

	private void correctSerie() {
		serie.setCoherenceSerie();

		SerieListViewAdapter adpater = new SerieListViewAdapter(this, serie);
		correctionList.setAdapter(adpater);

		LayoutInflater factory = LayoutInflater.from(this);
		View scoreDialogView = factory.inflate(R.layout.dialog_score, null);
		AlertDialog.Builder scoreDialog = new AlertDialog.Builder(this);
		scoreDialog.setView(scoreDialogView);
		scoreDialog.setTitle(R.string.title_dialog_score);
		scoreDialog.setIcon(this.getResources().getDrawable(
				R.drawable.resultat_small));

		double overallScore = (QUESTION_IN_SERIE + 1) * Notation.getHigherMark();
		double serieScore = serie.getTotalMark();
		String rawString = getResources().getString(R.string.descr_final_score);
		String formatedString = String.format(rawString, serieScore, overallScore);
		scoreDialog.setMessage(formatedString);

		scoreDialog.setPositiveButton(R.string.lbl_scoreOk,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		scoreDialog.show();
		
		//TODO revoir système de point, ça marche pas...
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.serie, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
