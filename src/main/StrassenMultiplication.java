

package main;
import java.util.Random;


public class StrassenMultiplication {
	
	/**
	 * a counter for naive multiplications
	 */
	private static int naiveCounter = 0;
	
	/**
	 * a counter for strassen multiplications
	 */
	private static int strassenCounter = 0;
	
	/**
	 * Driver for the Strassen Multiplication assignment. Runs the program.
	 * 
	 * @param theArgs Command line parameter... to be ignored
	 */
	public static void main(String... theArgs) {
		
		//the size of the array
		int size = 2;
		
		//create the matrices
		int[][] matrix1 = generateMatrix(size);
		int[][] matrix2 = generateMatrix(size);
		
		//print the matrix
		printMatrix(matrix1, size);
		printMatrix(matrix2, size);
		
		//naively multiply the matrix an print
		System.out.println("Naive Multiplication result:");
		int[][] naiveMatrix = naiveMultiply(matrix1, matrix2, size);
		printMatrix(naiveMatrix, size);
		System.out.println("Naive Multiplication required " + naiveCounter + " mulitplications\n");
		
		//Strassen multiply the matrix an print
		System.out.println("Strassen Multiplication result:");
		int[][] strassenMatrix = strassenMultiply(matrix1, matrix2, size);
		printMatrix(strassenMatrix, size);
		System.out.println("Naive Multiplication required " + strassenCounter + " mulitplications\n");
	}
	
	/**
	 * Generates a random matrix 
	 * @param the size of the matrix desired
	 * @return a matrix of size n of int, values 0-9
	 */
	private static int[][] generateMatrix(int n){
		Random rand = new Random();
		int[][] arr = new int[n][n];
		for (int i = 0; i < n; i++) {     
	        for (int j = 0; j < n; j++) {
	            int r = rand.nextInt()%10; 
	            arr[i][j] = Math.abs(r);
	        }
		}
		return arr;
	}
	
	/**
	 * Prints a matrix
	 * @param the matrix to be printed
	 */
	private static void printMatrix(int[][] mat, int size){
		for (int i = 0; i < size; i++) {
		    for (int j = 0; j < size; j++) {
		        System.out.print(mat[i][j] + " ");
		    }
		    System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	/**
	 * Multiplies two matrices together using a naive method
	 * @param two matrices (A[][],B[][]), the size of the matrices (assumes 2 
	 * 		  matrices of the same size)
	 * @return the matrix that is the product of A*B 
	 */
	private static int[][] naiveMultiply(int[][] A, int[][] B, int size){
		int[][] result = new int[size][size];
		//find the dot product of the matrixes
		//for each row
		for(int r = 0; r < size; r++){
			//for each column
			for (int c = 0; c < size; c++){
				//for each cell
				for(int k = 0; k < size; k++){
					//calculate the dot product of a whole row * whole column
					result[r][c] += A[r][k] * B[k][c];
					naiveCounter++;
				}			
			}
		}	
		return result;
	}
	
	/**
	 * Multiplies two matrices together using Strassen's algorithm
	 * @param two matrices (A[][], B[][]), the same size matrices (assumes 2 
	 * 	      matrices of the same size)
	 * @return the matrix that is the product of A*B
	 */
	private static int[][] strassenMultiply(int[][] A, int[][] B, int size){
		int[][] result = new int[size][size];
		//if the size is 2 (base case)
		if (size == 2){
			int m1 = (A[0][0] + A[1][1]) * (B[0][0] + B[1][1]);
			int m2 = (A[1][0] + A[1][1]) * B[0][0];
			int m3 = A[0][0] * (B[0][1] - B[1][1]);
			int m4 = A[1][1] * (B[1][0] - B[0][0]);
			int m5 = (A[0][0] + A[0][1]) * B[1][1];
			int m6 = (A[1][0] - A[0][0]) * (B[0][0] + B[0][1]);
			int m7 = (A[0][1] - A[1][1]) * (B[1][0] + B[1][1]);
			
			result[0][0] = m1 + m4 - m5 + m7;
			result[0][1] = m3 + m5;
			result[1][0] = m2 + m4;
			result[1][1] = m1 + m3 - m2 + m6;
		}
		
		//if the size is > 2
		//@TODO: FINISH THE OTHER CASES
		
		
		
		return result;
	}

}

