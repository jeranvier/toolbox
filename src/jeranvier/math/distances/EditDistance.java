package jeranvier.math.distances;

import java.util.Arrays;
import java.util.List;

import jeranvier.math.util.MathExtension;

/**
 * @author ranvier
 *
 */
public class EditDistance {

public static <T> int computeDistance(List<T> A, List<T> B){
		
		// degenerated cases
		if(A.equals(B)){
			return 0;
		}
		
		if(A.size() == 0){
			return B.size();
		}
		
		if(B.size() == 0){
			return A.size();
		}
		
		// Computation of the distance matrix
		int lengthA = A.size();
		int lengthB = B.size();
		
		int[][] matrix = new int[lengthA+1][lengthB+1];
		
		for (int a=0; a<lengthA+1; a++){
			matrix[a][0]=a;
		}
		
		for (int b=0; b<lengthB+1; b++){
			matrix[0][b]=b;
		}
		
		
		for (int b=1; b<lengthB+1; b++){
			for (int a=1; a<lengthA+1; a++){
				matrix[a][b] = MathExtension.min(
									(matrix[a-1][b]+1), //deletion
									(matrix[a][b-1]+1),//insertion
									(matrix[a-1][b-1]+(A.get(a-1).equals(B.get(b-1))?0:1)) ); //substitution
			}
		}
		return matrix[lengthA][lengthB];
	}
	
	public static int computeDistance(String A, String B){
		return computeDistance(Arrays.asList(Arrays.copyOfRange(A.split(""), 1, A.length()+1)), Arrays.asList(Arrays.copyOfRange(B.split(""), 1, B.length()+1)));
	}
	
	public static void main(String[] args){
		System.out.println(EditDistance.computeDistance("bcgef", "abcd"));
	}
}