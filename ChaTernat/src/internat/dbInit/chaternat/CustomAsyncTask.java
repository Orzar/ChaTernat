package internat.dbInit.chaternat;

import internat.chaternat.R;
import internat.db.chaternat.Answer;
import internat.db.chaternat.AnswerDao;
import internat.db.chaternat.DaoMaster;
import internat.db.chaternat.DaoMaster.DevOpenHelper;
import internat.db.chaternat.DaoSession;
import internat.db.chaternat.Question;
import internat.db.chaternat.QuestionDao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CustomAsyncTask extends AsyncTask<String, Integer, Integer> {
	private Activity mContext;
	private ProgressBar pbAsyncBackgroundTraitement;

	private SQLiteDatabase db;

	private DaoMaster daoMaster;
	private DaoSession daoSession;

	private QuestionDao questionDao;
	private AnswerDao answerDao;

	public CustomAsyncTask(Activity mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	protected void onPreExecute() {
		this.pbAsyncBackgroundTraitement = (ProgressBar) mContext
				.findViewById(R.id.pgr_initiate_db);
		this.pbAsyncBackgroundTraitement.setVisibility(View.VISIBLE);
		super.onPreExecute();
		
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, "internat-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        
        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, true);
        
        daoSession = daoMaster.newSession();
        
        questionDao = daoSession.getQuestionDao();
        answerDao = daoSession.getAnswerDao();
		
	}

	@Override
	protected Integer doInBackground(String... params) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			AssetManager asset = mContext.getAssets();
			InputStream xml = asset.open("xml/questionnaire.xml");
			Document doc = builder.parse(xml);
			
			NodeList questions = doc.getElementsByTagName("question");
			
			int progress = 0;
			
			for(int i=0; i<questions.getLength(); i++){
				if (i % 19 == 0)
					progress++;
				Element questionElement = (Element)questions.item(i);
				
				// Deal with text of the question
				NodeList ennonceNode = questionElement.getElementsByTagName("ennonce");
				Element ennonceElement = (Element) ennonceNode.item(0);
				Question question = new Question();
				question.setText(ennonceElement.getTextContent());
				question.setQuestionNumber(Integer.parseInt(questionElement.getAttribute("id")));
				question.setLastAsked(null);
				questionDao.insert(question);
				
				//Deal with answer of the question
				NodeList answerNode = questionElement.getElementsByTagName("reponse");
				NodeList solutionNode = questionElement.getElementsByTagName("solution");
				List<String> solutionList = new ArrayList<String>();
				for (int s=0; s<solutionNode.getLength(); s++){
					Element solutionElement = (Element) solutionNode.item(s);
					solutionList.add(solutionElement.getTextContent());
				}
				
				for (int a=0; a<answerNode.getLength();a++){
					Element answerElement = (Element) answerNode.item(a);
					Answer answer = new Answer();
					answer.setLetter(answerElement.getAttribute("id"));
					answer.setQuestionId(question.getId());
					answer.setText(answerElement.getTextContent());
					if (solutionList.contains(answerElement.getAttribute("id")))
						answer.setSolution(true);
					else
						answer.setSolution(false);
					
					answerDao.insert(answer);
				}
				
				publishProgress(progress);
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
	}

	/* fonction qui met à jour la progress bar */
	/* function which set progress update */
	@Override
	protected void onProgressUpdate(Integer... values) {
		// On met à jour la progress bar
		// We update the progress bar
		this.pbAsyncBackgroundTraitement.setProgress(values[0]);
		super.onProgressUpdate(values);
	}

	/*
	 * fonction qui est appelé après que le traitement effectué en tache de fond
	 * est terminé
	 */
	/* function which is called after work on background function is over */
	@Override
	protected void onPostExecute(Integer result) {
		// Le traitement est terminé, vous pouvez faire une action comme ouvrir
		// une nouvelle activity ou cacher des composant de l'UI
		// The work is done, you can do an action like create an activity or set
		// invisible some UI components
		this.pbAsyncBackgroundTraitement.setVisibility(View.INVISIBLE);
		Toast.makeText(this.mContext, "Insertion complete, you can work now <3", Toast.LENGTH_SHORT).show();
		super.onPostExecute(result);
	}

}
