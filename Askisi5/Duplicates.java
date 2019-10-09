package documentSimilarity;

import java.io.IOException;
import java.util.List;

/**
 * Kipourou Marina 1859
 */


public class Duplicates {
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		if(args.length < 4) throw new IllegalArgumentException(
				"Enter <folder> <num permutations> <num bands> <similarity threshold> ");
		
		System.out.println("Number of Permutations: "+args[1]+"  Number of Bands: "+args[2]);
		MinHash minHash = new MinHash(args[0], Integer.parseInt(args[1]));
		int[][] hashMatrix = minHash.minHashMatrix();
		String[] documents = minHash.allDocs();
		LSH lsh = new LSH(hashMatrix, documents, Integer.parseInt(args[2]));
		double similarityThreshold = Double.parseDouble(args[3]);
		int totalFalsePositives =0;
		
		for(int doc= 0; doc < documents.length; doc++){
			List<String> nearDuplicates = lsh.nearDuplicatesOf(documents[doc]);			
			int falsePositives = 0;
					for(String s : nearDuplicates) {
						double similarity = minHash.exactJaccard(documents[doc], s);
						falsePositives = countFalsePositives(similarityThreshold, falsePositives, s, similarity);
					}
			totalFalsePositives += falsePositives;
			System.out.println("Number of false positives: " + falsePositives);
			System.out.println("------------------------------");
		}
		System.out.println("Total Number of false positives for all documents is: "+totalFalsePositives);
		System.out.println("==================================");
		
		System.out.println("Executing LSH + MinHash 5 times...");
		long startTime = System.currentTimeMillis();
		executeLSHandMinHashFiveTimes(minHash, documents, lsh);
		long endTime = System.currentTimeMillis() - startTime;
		System.out.println("Total time for 5 executions of LSH + MinHash: "+ endTime + " ms");
	}
	
	public static int countFalsePositives(double similarityThreshold, int falsePositives, String s, double similarity) {
		if(similarity > similarityThreshold ) {
				System.out.println(s);
		}else {
			falsePositives++;
		}
		return falsePositives;
	}

	public static void executeLSHandMinHashFiveTimes(MinHash minHash, String[] documents, LSH lsh) throws IOException {
		for(int times=0; times<5; times++){	
			executeLSH(minHash, documents, lsh);
		}
	}

	public static void executeLSH(MinHash minHash, String[] documents, LSH lsh)	throws IOException {
		for(int doc= 0; doc < documents.length; doc++){
			List<String> nearDuplicates = lsh.nearDuplicatesOf(documents[doc]);			
					executeMinHash(minHash, documents, doc, nearDuplicates);
		}
	}

	public static void executeMinHash(MinHash minHash, String[] documents, int doc, List<String> nearDuplicates) throws IOException {
		for(String s : nearDuplicates) {
			minHash.exactJaccard(documents[doc], s);
		}
	}

}
