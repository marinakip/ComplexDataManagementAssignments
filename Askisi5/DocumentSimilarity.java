package documentSimilarity;

import java.io.IOException;

/**
 * Kipourou Marina 1859
 */

public class DocumentSimilarity {

	public static void main(String[] args) throws NumberFormatException, IOException {
		if(args.length != 3) throw new IllegalArgumentException("Enter <folder> <num permutations> <error parameter>");
		
		System.out.println("Results for Permutations: "+ args[1]);
		MinHash minHash = new MinHash(args[0], Integer.parseInt(args[1]));
		double epsilon = Double.parseDouble(args[2]);
		String[] files = minHash.allDocs();
		int[][] minHashMatrix = minHash.minHashMatrix();
		
		int numberOfDifferentDocs = computeNumberOfDifferentDocs(minHash, epsilon, files, minHashMatrix);
		System.out.println("Number of different documents: " + numberOfDifferentDocs);
		
		System.out.println("Executing Exact Jaccard 5 times...");
		long endTimeExactJaccard = timingExactJaccard(minHash, files);
		System.out.println("Total time for Exact Jaccard after 5 executions is: " + endTimeExactJaccard + " ms");
		System.out.println("------------------------------");
		
		System.out.println("Executing Approximate Jaccard 5 times...");
		long endTimeApproximateJaccard = timingApproximateJaccard(minHash, minHashMatrix);
		System.out.println("Total time for Approximate Jaccard after 5 executions is: " + endTimeApproximateJaccard + " ms");
		System.out.println("================================");

	}
	
	public static int computeNumberOfDifferentDocs(MinHash minHash,	double epsilon, String[] files, int[][] minHashMatrix) throws IOException {
		int numberOfDifferentDocs = 0;
		double exactJaccard;
		double approximateJaccard;	
		for(int i = 0; i < files.length; i++) {
			for(int j = i + 1; j < files.length; j++) {
				exactJaccard = minHash.exactJaccard(files[i], files[j]);
				approximateJaccard = minHash.approximateJaccard(minHashMatrix[i], minHashMatrix[j]);
				
				numberOfDifferentDocs = countDifferentDocuments(epsilon, numberOfDifferentDocs, exactJaccard, approximateJaccard);
			}
		}
		return numberOfDifferentDocs;
	}
	
	public static int countDifferentDocuments(double epsilon, int differentDocs, double exactJaccard, double approximateJaccard) {
		if(approximateJaccard < exactJaccard && approximateJaccard + epsilon < exactJaccard) {
			differentDocs++;
		} else if(approximateJaccard > exactJaccard && approximateJaccard - epsilon > exactJaccard) {
			differentDocs++;
		}
		return differentDocs;
	}

	public static long timingExactJaccard(MinHash minHash, String[] files) throws IOException {
		long startTimeExactJaccard = System.currentTimeMillis();
		executeExactJaccardFiveTimes(minHash, files);
		long endTimeExactJaccard = System.currentTimeMillis() - startTimeExactJaccard;
		return endTimeExactJaccard;
	}

	public static void executeExactJaccardFiveTimes(MinHash minHash, String[] files) throws IOException {
		for(int times=0; times<5; times++){
			computeExactJaccard(minHash, files);
		}
	}
	
	public static void computeExactJaccard(MinHash minHash, String[] files)	throws IOException {
		for(int i = 0; i < files.length; i++) {
			for(int j = i + 1; j < files.length; j++) {
				minHash.exactJaccard(files[i], files[j]);
			}
		}
	}
	
	public static long timingApproximateJaccard(MinHash minHash, int[][] minHashMatrix) {
		long startTimeApproximateJaccard = System.currentTimeMillis();
		executeApproximateJaccardFiveTimes(minHash, minHashMatrix);
		long endTimeApproximateJaccard = System.currentTimeMillis() - startTimeApproximateJaccard;
		return endTimeApproximateJaccard;
	}

	public static void executeApproximateJaccardFiveTimes(MinHash minHash, int[][] minHashMatrix) {
		for(int times=0; times<5; times++){
			computeApproximateJaccard(minHash, minHashMatrix);
		}
	}

	public static void computeApproximateJaccard(MinHash minHash, int[][] minHashMatrix) {
		for(int i = 0; i < minHashMatrix.length; i++) {
			for(int j = i + 1; j < minHashMatrix.length; j++) {
				minHash.approximateJaccard(minHashMatrix[i], minHashMatrix[j]);
			}
		}
	}	

}
