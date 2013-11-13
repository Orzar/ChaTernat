package internat.models;

public class Notation {
	
	private static final int COHERENCE_5 = 2;
	private static final int COHERENCE_4 = 1;
	private static final double COHERENCE_3 = 0.4;
	private static final int COHERENCE_2 = 0;
	
	public static double getMarkForCoherence(int coherence){
		double coherenceValue;
		switch (coherence) {
		case 5:
			coherenceValue = COHERENCE_5;
			break;
		case 4:
			coherenceValue = COHERENCE_4;
			break;
		case 3:
			coherenceValue = COHERENCE_3;
			break;
		case 2:
			coherenceValue = COHERENCE_2;
			break;
		default:
			coherenceValue = 0;
			break;
		}
		
		return coherenceValue;
	}
	
	public static double getHigherMark(){
		return COHERENCE_5;
	}
	
}
