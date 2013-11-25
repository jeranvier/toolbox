package jeranvier.math.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class ConfusionMatrix <T>{
	
	protected Map<Coordinates<T>, Integer> matrix;

	public ConfusionMatrix(){
		matrix = new HashMap<Coordinates<T>, Integer>();
	}
	
	public void add(T actualClass, T predictedClass){
		Coordinates<T> coordinates = new Coordinates<T>(actualClass, predictedClass);
		if(matrix.containsKey(coordinates)){
			matrix.put(coordinates, matrix.get(coordinates)+1);
		}else{
			matrix.put(coordinates, 1);
		}
	}
	
	public void display(){

		Set<T> classes = getClasses();
		
		System.out.format("%17s","predicted as -> |");
		for(T classe: classes){
			System.out.format("%15s",classe+" |");
		}
		System.out.print("\n");
		
		for(T actualClasse: classes){
			System.out.format("%17s",actualClasse+" |");
			for(T predictedClasse: classes){
				Coordinates<T> currentCoordinates = new Coordinates<T>(actualClasse, predictedClasse);
				if(matrix.containsKey(currentCoordinates)){
    				System.out.format("%15s",""+matrix.get(currentCoordinates)+" |");
    			}
    			else{
    				System.out.format("%15s","0 |");
    			}
			}
			System.out.print("\n");
		}
		
	}
	
	// =tp/(tp+fn)
		public Map<T, Double> computeClassesRecall(){

			Set<T> classes = getClasses();
			
			Map<T, Double> recalls = new HashMap<T, Double>();
			
			for(T actualClasse: classes){
				int tp = 0; //true positive
				int fn = 0; //false negative
				for(T predictedClasse: classes){
					Coordinates<T> currentCoordinates = new Coordinates<T>(actualClasse, predictedClasse);
					if(matrix.containsKey(currentCoordinates)){
						if(actualClasse.equals(predictedClasse)){
							tp+=matrix.get(currentCoordinates);
						}else{
							fn+=matrix.get(currentCoordinates);
						}
					}
				}
				
				recalls.put(actualClasse,((double)tp)/(tp+fn));
			}
			return recalls;
		}
		
		// =tp/(tp+fp)
		public Map<T, Double> computeClassesPrecision(){

			Set<T> classes = getClasses();
			
			Map<T, Double> precisions = new HashMap<T, Double>();
			
			for(T predictedClasse: classes){
				int tp = 0; //true positive
				int fp = 0; //false positive
				for(T actualClasse: classes){
					Coordinates<T> currentCoordinates = new Coordinates<T>(actualClasse, predictedClasse);
					if(matrix.containsKey(currentCoordinates)){
						if(actualClasse.equals(predictedClasse)){
							tp+=matrix.get(currentCoordinates);
						}else{
							fp+=matrix.get(currentCoordinates);
						}
					}
				}
				

				precisions.put(predictedClasse,((double)tp)/(tp+fp));
			}
			return precisions;
		}
		
		public double computePrecision(){
			Map<T, Double> precisions = computeClassesPrecision();
			Map<T, Integer> weights = computeClassesWeights();
			Set<T> classes = getClasses();
			double precision=0;
			int totalWeight = 0;
			for(T classe : classes){
				precision += precisions.get(classe)*weights.get(classe);
				totalWeight += weights.get(classe);
			}
			return precision/totalWeight;
		}
		
		public double computeRecall(){
			Map<T, Double> recalls = computeClassesRecall();
			Map<T, Integer> weights = computeClassesWeights();
			Set<T> classes = getClasses();
			double recall=0;
			int totalWeight = 0;
			for(T classe : classes){
				recall += recalls.get(classe)*weights.get(classe);
				totalWeight += weights.get(classe);
			}
			return recall/totalWeight;
		}

	public Map<T, Integer> computeClassesWeights(){
		
		Map<T, Integer> weights = new HashMap<T, Integer>();

		Set<T> classes = getClasses();
		
		for(T actualClasse: classes){
			int weight = 0;
			for(T predictedClasse: classes){
				Coordinates<T> currentCoordinates = new Coordinates<T>(actualClasse, predictedClasse);
				if(matrix.containsKey(currentCoordinates)){
					weight+=matrix.get(currentCoordinates);
				}
			}
			
			weights.put(actualClasse,weight);
		}
		return weights;
	}
	
	public Set<T> getClasses(){ 
		Set<Coordinates<T>> coordinates = matrix.keySet();
		Set<T> classes = new TreeSet<T>();
		for(Coordinates<T> coordinate: coordinates){
			classes.add(coordinate.actualClass);
			classes.add(coordinate.predictedClass);
		}
		return classes;
	}
	
	//Represents the coordinates of an element of the matrix
	class Coordinates<S>{
		
		private S actualClass;
		private S predictedClass;

		public Coordinates(S actualClass, S predictedClass){
			this.actualClass = actualClass;
			this.predictedClass= predictedClass;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((actualClass == null) ? 0 : actualClass.hashCode());
			result = prime
					* result
					+ ((predictedClass == null) ? 0 : predictedClass.hashCode());
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Coordinates<S> other = (Coordinates<S>) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (actualClass == null) {
				if (other.actualClass != null)
					return false;
			} else if (!actualClass.equals(other.actualClass))
				return false;
			if (predictedClass == null) {
				if (other.predictedClass != null)
					return false;
			} else if (!predictedClass.equals(other.predictedClass))
				return false;
			return true;
		}

		private ConfusionMatrix<T> getOuterType() {
			return ConfusionMatrix.this;
		}
	}
	
	public static void main(String[] arg){
		ConfusionMatrix<Integer> cm = new ConfusionMatrix<Integer>();
		cm.add(1, 1);
		cm.add(1, 1);
		cm.add(1, 1);
		cm.add(2, 1);
		cm.add(2, 2);
		cm.add(2, 2);
		cm.add(2, 2);
		cm.add(2, 3);
		cm.add(3, 2);
		cm.add(3, 3);
		cm.add(3, 3);
		cm.add(1, 3);
		cm.display();
		
		System.out.println("\nprecisions");
		for(Entry<Integer, Double> precision : cm.computeClassesPrecision().entrySet()){
			System.out.println(precision.getKey()+" : "+precision.getValue());
		}
		
		System.out.println("\nrecalls");
		for(Entry<Integer, Double> recall : cm.computeClassesRecall().entrySet()){
			System.out.println(recall.getKey()+" : "+recall.getValue());
		}
		
		System.out.println("\nweights");
		for(Entry<Integer, Integer> weight : cm.computeClassesWeights().entrySet()){
			System.out.println(weight.getKey()+" : "+weight.getValue());
		}
		
		System.out.println("\nPrecision: "+cm.computePrecision());
		System.out.println("Recall: "+cm.computeRecall());
	}
}
