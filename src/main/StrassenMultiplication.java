/**
 * implementation of Strassen's algorithm for matrix multiplication
 * Matthew Seto
 * TCSS 343 
 * 26 Feb, 2016
 * 
 * NB, code is mine, but I did use the following as guidelines for help. 
 * https://martin-thoma.com/strassen-algorithm-in-python-java-cpp/
 * https://github.com/srinivasgumdelli/Algorithms/blob/master/Strassen.java
 * 
 */

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
		
		//the size of the array MUST BE POW 2
		int size = 64;
		
		//create the matrices
		int[][] matrix1 = generateMatrix(size);
		int[][] matrix2 = generateMatrix(size);
		
		//print the matrix
		System.out.println("A");
		printMatrix(matrix1, size);
		System.out.println("B");
		printMatrix(matrix2, size);
		
		//naively multiply the matrix an print
		System.out.println("Naive Multiplication result:");
		int[][] naiveMatrix = naiveMultiply(matrix1, matrix2);
		printMatrix(naiveMatrix, size);
		System.out.println("\n");
		
		//Strassen multiply the matrix an print
		System.out.println("Strassen Multiplication result:");
		int[][] strassenMatrix = strassenMultiply(matrix1, matrix2);
		printMatrix(strassenMatrix, size);
		System.out.println("Compare: \n");
		System.out.println("Naive Multiplication required " + naiveCounter + " mulitplications");
		System.out.println("Runtime of naive: O(n^3)\n");
		System.out.println("Strassen Multiplication required " + strassenCounter + " mulitplications");
		System.out.println("Runtime of strassen: O(n^(log(b2)7))\n");
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
	private static int[][] naiveMultiply(int[][] A, int[][] B){
		int size = A.length;
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
	private static int[][] strassenMultiply(int[][] A, int[][] B){
		int size = A.length;
		int[][] result = new int[size][size];

		//Check if the matrix is not even and not 1
		if((size%2 != 0) && (size!= 1)){
			//generate smaller sub arrays to hold intermediate multiplications
			int[][] subA = new int[size+1][size+1];
			int[][] subB = new int[size+1][size+1];
			
			//copy the arrays into the intermediates
			for(int i=0; i<size; i++){
				for(int j=0; j<size; j++){
					subA[i][j] = A[i][j];
					subB[i][j] = B[i][j];
				}
			}
			
			//recursively call strassen multiply 
			result = strassenMultiply(subA, subB);

			//return the result;
			return result;
			
		}
		
		//base case (size == 1)
		if (size == 1){
			result[0][0] = A[0][0] * B[0][0];
		}
		//else, it is greater than 1
		else {
			//generate new matrices of half the size
			//submatrices of A
			int [][] A11 = new int[size/2][size/2];
			int [][] A12 = new int[size/2][size/2];
			int [][] A21 = new int[size/2][size/2];
			int [][] A22 = new int[size/2][size/2];
			//submatrices of B
			int [][] B11 = new int[size/2][size/2];
			int [][] B12 = new int[size/2][size/2];
			int [][] B21 = new int[size/2][size/2];
			int [][] B22 = new int[size/2][size/2];
			
			
			//divide the original matrices into the smaller matrices created above
			//A11 is the top left quarter
			matrixDivide(A, A11, 0,0);
			//A12 is the top right quarter
			matrixDivide(A, A12, 0, size/2);
			//A21 is the bottom left quarter
			matrixDivide(A, A21, size/2, 0);
			//A22 is the bottom right quarter
			matrixDivide(A, A22, size/2, size/2);
			
			//do the same for matrix B
			matrixDivide(B, B11, 0, 0);
			matrixDivide(B, B12, 0, size/2);
			matrixDivide(B, B21, size/2, 0);
			matrixDivide(B, B22, size/2, size/2);
			
			//now set up the M values and recursively call strassenMultiply
			//m1 = (A[1][1] + A[2][2]) * (B[1][1] + B[2][2]);
			int[][] m1 = strassenMultiply(addMatrices(A11, A22), addMatrices(B11, B22));
			//m2 = (A[2][1] + A[2][2]) * B[1][1];
			int[][] m2 = strassenMultiply(addMatrices(A21, A22), B11);
			//m3 = A[1][1] * (B[1][2] - B[2][2]);
			int[][] m3 = strassenMultiply(A11, subtractMatrices(B12, B22));
			//m4 = A[2][2] * (B[2][1] - B[1][1]);
			int[][] m4 = strassenMultiply(A22, subtractMatrices(B21, B11));
			//m5 = (A[1][1] + A[1][2]) * B[2][2];
			int[][] m5 = strassenMultiply(addMatrices(A11, A12), B22);
			//m6 = (A[2][1] - A[1][1]) * (B[1][1] + B[1][2]);
			int[][] m6 = strassenMultiply(subtractMatrices(A21, A11), addMatrices(B11, B12));
			//m7 = (A[1][2] - A[2][2]) * (B[2][1] + B[2][2]);
			int[][] m7 = strassenMultiply(subtractMatrices(A12, A22), addMatrices(B21, B22));
			//increment the strassen counter
			strassenCounter +=7;
			
			//now get the C values from the M values
			//C11 = m1 + m4 - m5 + m7;
			int[][] C11 = addMatrices(subtractMatrices(addMatrices(m1, m4), m5), m7);
			//C12 = m3 + m5;
			int[][] C12 = addMatrices(m3, m5);
			//C21 = m2 + m4;
			int[][] C21 = addMatrices(m2, m4);
			//C22 = m1 + m3 - m2 + m6
			int[][] C22 = addMatrices(subtractMatrices(addMatrices(m1, m3), m2), m6);
			
			//rebuild the result matrix from the C matrices we made
			matrixCopy(C11, result, 0, 0);
			matrixCopy(C12, result, 0, size/2);
			matrixCopy(C21, result, size/2, 0);
			matrixCopy(C22, result, size/2, size/2);
			
		}
		return result;
	}

	
	/**
	 * Takes a section of an input matrix and divides it to the output matrix 
	 * takes the number of rows and columns specified
	 * @param in input matrix
	 * @param out output matrix
	 * @param rows number of rows to take from the input
	 * @param cols number of columns to take from the input
	 */
	private static void matrixDivide(int[][] in, int[][] out, int rows, int cols){
		//for each row (out rows start at zero, in rows start at the rows param)
		for (int outrow=0, inrow = rows; outrow < out.length; outrow++, inrow++){
			//for each col (out cols start at zero, in cols start at cols param)
			for (int outcol=0, incol=cols; outcol<out.length; outcol++, incol++){
				out[outrow][outcol] = in[inrow][incol];
			}
		}
	}
	
	/**
	 * Takes a section of an input matrix and copies it to the output matrix 
	 * takes the number of rows and columns specified
	 * @param in input matrix
	 * @param out output matrix
	 * @param rows number of rows to take from the input
	 * @param cols number of columns to take from the input
	 */
	public static void matrixCopy(int[][] in, int[][] out, int rows, int cols)
	{
		for(int inrows = 0, outrows=rows; inrows<in.length; inrows++, outrows++)
			for(int incols = 0, outcols=cols; incols<in.length; incols++, outcols++)
			{
				out[outrows][outcols] = in[inrows][incols];
			}
	}
	
	/**
	 * Adds to matrices together
	 * @param A
	 * @param B
	 * @return the sum of the two matrices
	 */
	private static int[][] addMatrices(int[][] A, int[][] B){
		int size = A.length;
		
		int[][] result = new int[size][size];
		//for each row
		for(int i = 0; i < size; i++){
			//for each col
			for(int j = 0; j < size; j++){
				result[i][j] = A[i][j] + B[i][j];
			}
		}
		return result;
	}
	
	/**
	 * Subtracts two matrices
	 * @param A
	 * @param B
	 * @return the difference of the two matrices
	 */
	private static int[][] subtractMatrices(int[][] A, int[][] B){
		int size = A.length;
		
		int[][] result = new int[size][size];
		//for each row
		for(int i = 0; i < size; i++){
			//for each col
			for(int j = 0; j < size; j++){
				result[i][j] = A[i][j] - B[i][j];
			}
		}
		return result;
	}

}

