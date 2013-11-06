package jeranvier.math.distances;

import java.util.Set;
import java.util.TreeSet;

public class JaccardIndex {

	public static <T> double computeDistance(Set<T> A, Set<T> B){
		Set<T> C = new TreeSet<T>(A);
		
		C.retainAll(B); //intersection
		double numerator = C.size();
		C = null;
		
		A.addAll(B); //union
		double denominator = A.size();
		
		return numerator/denominator;
	}
}
