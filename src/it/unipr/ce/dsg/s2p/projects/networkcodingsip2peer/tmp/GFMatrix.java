package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tmp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

/***
 * This class represent a Matrix of elements in a finite field.
 * It is used to store codingVector by ClientNodePeers.
 * 
 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
 *
 */
public class GFMatrix {

	//	the row's dimension
	private byte h = 0;
	
	public GaloisField field = null;
	
	protected char[][] matrix;
	
	private static int counter = 0;
	
	
	/**
	 * 
	 * @param generationDimension
	 * @param primePower: is the power of the base. It can assume value 8 or 16.
	 */
	public GFMatrix(byte rowSize, GaloisField field) {
		this.h = rowSize;
		this.field = field;
	}
	
	public int size(){
		return matrix.length;
	}
	
	/**
	 * This method adds a vector to the Matrix object.
	 * 
	 * @param codingVector: the vector to be added to the Matrix
	 * 
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	public void add(char[] codingVector){
		try{
			if(codingVector.length != h)
				throw new MatrixException();
			if(matrix != null){
				char[][] m = new char[matrix.length + 1][h];
				for(char i = 0; i <= matrix.length; ++i){
					if (i == matrix.length){
						m[i] = codingVector;
						continue;
					}
					m[i] = matrix[i];
				}
				matrix = new char[matrix.length + 1][h];
				matrix = m;
			}else{
				matrix = new char[1][h];
//				for(byte j = 0; j < h; ++j)
//					codingMatrix[0][j] = codingVector[j];
				matrix[0] = codingVector;
			}
		}catch(MatrixException ex){
			System.err.println("CodingVector passed hasn't h elements.");
		}
	}
	
	/**
	 * Returns a Matrix's vector.
	 * 
	 * @param index: the vector's index into the Matrix
	 * @return a vector
	 * 
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	public char[] get(char index){
		return matrix[index];
	}
	
	/**
	 * Return a column of matrix
	 * 
	 * @param index which column
	 * @return column of values
	 */
	public char[] getColumn(char index){
		char col[] = new char[(int)getRowsNumber()];
		//System.out.println((int)getColumnsNumber() +" - "+(int)getRowsNumber()+ " index: "+(int)index);
		for(char i=0; i<getRowsNumber(); i++){
			//System.out.println("get["+(int)i+"]["+(int)index+"] = "+(int)matrix[i][index]);
			col[i] = matrix[i][index];
		}
		
		return col;
	}
	
	public boolean remove(byte index) {
		
		try{
			
			if((index >= 0) && (index < matrix.length)){
				
				char[][] m = new char[matrix.length - 1][h];
				
				for(byte i = 0; i < matrix.length; ++i){
					if(i < index){
						m[i] = matrix[i];
					}else if(i > index){
						m[i-1] = matrix[i];
					}
				}
				
				matrix = new char[matrix.length - 1][h];
				
				matrix = m;
				
				return true;
				
			}else
				throw new MatrixException();
		}catch(MatrixException ex){
			System.err.println("Index out of range.");
			return false;
		}
	}
	
	/**
	 * This method removes a List of vectors which indexes are passed as argument, from
	 * the Matrix.
	 * 
	 * @param indexes
	 * @return true if the deletion goes fine
	 * 
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	public boolean removeAll(ArrayList<Byte> indexes){
		
		char[][] m = new char[matrix.length - indexes.size()][h];
		
		//verify of indexes' bounds
		try{
			Iterator<Byte>it = indexes.iterator();
			byte index = 0;
			while(it.hasNext()){
				index = it.next();
				if((index < 0) || (index >= matrix.length))
					throw new MatrixException();
			}
			
			//removing vectors
			it = indexes.iterator();
			byte vectorIndexToRemove = 0;
			byte row = 0;
			byte removed = 0;
			
			
			while(it.hasNext()){
				vectorIndexToRemove = it.next();
				for(byte i = row; i < vectorIndexToRemove; ++i){
					m[i - removed] = matrix[i];
				}
				row = (byte)(vectorIndexToRemove + 1);
				++removed;
			}
			
			
			for (byte i = row; i < m.length; ++i){
				m[i - removed] = matrix[i];
			}
			
			matrix = new char[matrix.length - indexes.size()][h];
			
			matrix = m;
			
			return true;
			
		}catch(MatrixException ex){
			System.err.println("Index out of range.");
			return false;
		}
	}
	
	/**
	 * Elimination algorithm performed over finite field.
	 * 
	 *
	 */
	public ArrayList<Byte> gaussAlgorithm(){
		
		char[] [] m = this.translate(matrix);
		
		ArrayList<Byte> linearDipendentIndexVector = new ArrayList<Byte>();
				
		byte i = 0, j = 0;
		
		while((i < m.length) && (j < (m[0].length - 1))){
			if(field.isZero(m[i][j])){
				
				//exchanges the row with any other with first digit different by zero
				char[] tempArray;
				byte rowIndex = i;
				for(byte l = (byte) (i+1); l < m.length; ++l){
					if (!field.isZero(m[l][j])){
						rowIndex = l;
						break;
					}
				}
				tempArray = m[i];
				m[i] = m[rowIndex];
				m[rowIndex] = tempArray;
			}
			
			char coeff = 0;
			
			for(byte l = (byte) (i+1); l < m.length; ++l){
				if((!field.isZero(m[l][j])) && (!field.isZero(m[i][j]))){
//					coeff = field.divide(m[i][j], m[l][j]);
					coeff = field.divide(m[l][j],m[i][j]);
					for(byte k = i; k < m[0].length; ++k){
//						char product = field.product(m[l][k], coeff);
//						m[l][k] = field.sum(product, m[i][k]);
						char product = field.product(m[i][k], coeff);
						m[l][k] = field.sum(product, m[l][k]);
					}
				}
			}
			
			++i;
			++j;

		}
		for(i = 0; i < m[0].length; ++i ){
			if(field.isZero(m[i][i]))
				linearDipendentIndexVector.add(i);
		}
//		this.printMatrix(m);
		if(m.length != m[0].length)
			return this.verifyClog(m, linearDipendentIndexVector);
		else
			return linearDipendentIndexVector;
	}
	
	

	private ArrayList<Byte> verifyClog(char[][] m,
			ArrayList<Byte> indexes) {
		ArrayList<Byte> linearDipendentIndexVector = new ArrayList<Byte>();
		Iterator<Byte> itIndexes = indexes.iterator();
		while(itIndexes.hasNext())
		{
			byte idx = itIndexes.next();
			boolean digitDifferentFromZero = false;
			for(byte i = idx; i < m.length; ++i){
				if(!field.isZero(m[i][idx])){
					digitDifferentFromZero = true;
					break;
				}
			}
			if(!digitDifferentFromZero)
				linearDipendentIndexVector.add(idx);
		}
		return linearDipendentIndexVector;
		
	}

	private char[][] translate(char[][] matrix) {
		char[][] m = new char[matrix[0].length][matrix.length];
		
		for(byte i = 0; i < matrix.length; ++i){
			for(byte j = 0; j < matrix[0].length; ++j){
				m[j][i] = matrix[i][j];
			}
		}
		return m;
	}
	
	/**
	 * This method fills a matrix with ZERO elements.
	 * 
	 * 
	 */
	public void initializeMatrix(byte rows, byte columns){
		matrix = new char[rows][columns];
		for(byte i = 0; i < matrix.length; ++i){
			for(byte j = 0; j < matrix[0].length; ++j)
				matrix[i][j] = GaloisField.getZero();
		}
	}

	public void printMatrix(char[][] mat){
		for(char i = 0; i < mat.length; ++i){
			for(char j = 0; j < mat[0].length; ++j){
				System.out.print(((int)mat[i][j]) + "\t");
			}
			System.out.println();
		}
	}
	
	public void printMatrix(){
		for(char i = 0; i < matrix.length; ++i){
			for(char j = 0; j < matrix[0].length; ++j){
				System.out.print(((int)matrix[i][j]) + "\t");
			}
			System.out.println();
		}
	}
	
	public void printMatrix(ArrayList<Byte> indexes, int resourceKey){
		try {
			++GFMatrix.counter;
		    FileOutputStream file = new FileOutputStream("./matrixFile/matrix" + GFMatrix.counter +".txt");
		    PrintStream output = new PrintStream(file);

		    output.println("Resource:" + resourceKey);
		    Iterator<Byte> it = indexes.iterator();
		    while(it.hasNext())
		    	output.print(it.next() + " ");
		    output.println();
			char[][] m = this.translate(matrix);
			for(char i = 0; i < m.length; ++i){
				for(char j = 0; j < m[0].length; ++j){
					output.print(((int)m[i][j]) + "\t");
				}
				output.println();
			}
		} catch (IOException e) {}

	}

	public char getRowsNumber() {
		return (char) matrix.length;
	}
	
	public char getColumnsNumber() {
//		System.out.println((char) matrix[0].length);
		return (char) matrix[0].length;
	}
}
