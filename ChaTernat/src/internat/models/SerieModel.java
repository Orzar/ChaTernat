package internat.models;

import internat.db.chaternat.Answer;
import internat.db.chaternat.Question;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

public class SerieModel {

	private SparseArray<Question> questionList;
	private SparseArray<List<String>> chosenAnswer;
	private SparseArray<Integer> coherenceNumber;

	public SerieModel(int capacity) {
		super();
		this.questionList = new SparseArray<Question>(capacity);
		this.chosenAnswer = new SparseArray<List<String>>(capacity);
		this.coherenceNumber = new SparseArray<Integer>(capacity);
	}

	/**
	 * 
	 * @return The question Sparse Array
	 */
	public SparseArray<Question> getQuestionsList() {
		return questionList;
	}

	/**
	 * Insert a question into the questions SparseArray
	 * 
	 * @param index
	 *            The index to place the question at
	 * @param question
	 *            The question to store
	 */
	public void addQuestion(int index, Question question) {
		questionList.append(index, question);
	}

	public List<Answer> getAnswer(int index) {
		return questionList.get(index).getAnswerList();
	}

	/**
	 * Insert a chosen answer list into the chosen answer SparseArray
	 * 
	 * @param index
	 *            The index to place the chosen answer list at
	 * @param answer
	 *            The chosen answer list to store
	 */
	public void addChosenAnswers(int index, List<String> answer) {
		chosenAnswer.append(index, answer);
	}

	/**
	 * Insert a chosen answer into the chosen answer list
	 * 
	 * @param index
	 *            The index to place the chosen answer list at
	 * @param answer
	 *            the answer to insert into the list
	 */
	public void addChosenAnswer(int index, String answer) {
		List<String> chosen = chosenAnswer.get(index);
		if (chosen == null) {
			chosen = new ArrayList<String>();
		}
		chosen.add(answer);
		chosenAnswer.put(index, chosen);
	}

	/**
	 * Remove a chosen answer from the chosen answer list
	 * 
	 * @param index
	 *            The index at which chosen answer list is stored
	 * @param answer
	 *            the answer to remove
	 */
	public void removeChosenAnswer(int index, String answer) {
		List<String> chosen = chosenAnswer.get(index);
		chosen.remove(answer);
		chosenAnswer.put(index, chosen);
	}

	/**
	 * Get a question from the SparseArray
	 * 
	 * @param index
	 *            The index to look for
	 * @return The question at the index index
	 */
	public Question getQuestion(int index) {
		return questionList.get(index);
	}

	/**
	 * Get a chosen answer list from the SparseArray
	 * 
	 * @param index
	 *            The index to look for
	 * @return The chosen answer list at the index index
	 */
	public List<String> getChosenAnswer(int index) {
		return chosenAnswer.get(index);
	}

	/**
	 * This function return the overall number of errors in a serie.
	 * 
	 * @return Number of errors in the serie
	 */
	public int getNumberOfErrors() {
		int numberOfError = 0;

		for (int i = 0; i < questionList.size(); i++) {
			if (this.getResultsForQuestion(i)) {
				numberOfError++;
			}
		}
		return numberOfError;
	}

	/**
	 * This function allow to test if a question have been answered correctly
	 * 
	 * @param index
	 *            the question to check
	 * @return true if the chosen answer match the right answer of the question
	 */
	public boolean getResultsForQuestion(int index) {
		// return (chosenAnswer.get(index).containsAll(rightAnswer.get(index))
		// && rightAnswer.get(index).containsAll(chosenAnswer.get(index)));
		boolean questionRight = true;
		int totalRight = 0;
		for (Answer ans : questionList.get(index).getAnswerList()) {
			if (ans.getSolution())
				totalRight++;
			if (!(chosenAnswer.get(index).contains(ans.getLetter()))
					&& (ans.getSolution())) {
				questionRight = false;
			}
		}
		if (!(totalRight == chosenAnswer.get(index).size()))
			questionRight = false;
		return questionRight;
	}

	/**
	 * This method calculate and return the number of coherence for a serie
	 * 
	 * @return The total mark for the serie
	 */
	public void setCoherenceSerie() {
		for (int i = 0; i < questionList.size(); i++) {
			coherenceNumber.put(i, getCoherenceQuestion(i));
		}
	}

	/**
	 * Will return the mark for a question
	 * 
	 * @param index
	 *            The index of the question in the serie
	 * @return the coherence of the question
	 */
	public int getCoherenceQuestion(int index) {
		int nbCoherence = 0;

		for (Answer ans : questionList.get(index).getAnswerList()) {
			if (chosenAnswer.get(index).contains(ans.getLetter())
					&& (ans.getSolution()))
				nbCoherence++;
			if (!chosenAnswer.get(index).contains(ans.getLetter())
					&& (!ans.getSolution()))
				nbCoherence++;
			if (chosenAnswer.get(index).contains(ans.getLetter())
					&& (!ans.getSolution()))
				nbCoherence--;
			if (!chosenAnswer.get(index).contains(ans.getLetter())
					&& (ans.getSolution()))
				nbCoherence--;
		}
		
		if (nbCoherence  <= 2)
			nbCoherence = 2;

		return nbCoherence;
	}

	/**
	 * Get the total mark for a serie, the amount is on serie.count * 2.
	 * @return the amount of mark for a serie.
	 */
	public int getTotalMark() {
		int totalMark = 0;
		for (int i = 0; i < coherenceNumber.size(); i++) {
			totalMark += Notation.getMarkForCoherence(coherenceNumber.get(i));
		}
		return totalMark;
	}
}
