package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.tmp;

import java.util.ArrayList;
import java.util.Iterator;
//import it.unipr.ce.dsg.deus.example.p2p.storage.utils.MatrixException;


/**
 * This class represent a Matrix.
 * 
 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
 *
 */
public class Matrix {
	
//	the generation's dimension
	private byte h = 0;
	
	protected char[][] codingMatrix;
	
	
	public Matrix(byte generationDimension){
		this.h = generationDimension;
	}
	
	public int size(){
		return codingMatrix.length;
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
			if(codingMatrix != null){
				char[][] m = new char[codingMatrix.length + 1][h];
				for(byte i = 0; i <= codingMatrix.length; ++i){
					if (i == codingMatrix.length){
						m[i] = codingVector;
						continue;
					}
					m[i] = codingMatrix[i];
				}
				codingMatrix = new char[codingMatrix.length + 1][h];
				codingMatrix = m;
			}else{
				codingMatrix = new char[1][h];
//				for(byte j = 0; j < h; ++j)
//					codingMatrix[0][j] = codingVector[j];
				codingMatrix[0] = codingVector;
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
	public char[] get(byte index){
		return codingMatrix[index];
	}
	
	/**
	 * This method remove a vector which index is passed ad argument from the Matrix.
	 *
	 * @param index: the vector's index into the Matrix
	 * @return true if the deletion goes fine
	 * 
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	public boolean remove(byte index){
		
		try{
			
			if((index >= 0) && (index < codingMatrix.length)){
				
				char[][] m = new char[codingMatrix.length - 1][h];
				
				for(byte i = 0; i < codingMatrix.length; ++i){
					if(i < index){
						m[i] = codingMatrix[i];
					}else if(i > index){
						m[i-1] = codingMatrix[i];
					}
				}
				
				codingMatrix = new char[codingMatrix.length - 1][h];
				
				codingMatrix = m;
				
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
		
		char[][] m = new char[codingMatrix.length - indexes.size()][h];
		
		//verify of indexes' bounds
		try{
			Iterator<Byte>it = indexes.iterator();
			byte index = 0;
			while(it.hasNext()){
				index = it.next();
				if((index < 0) || (index >= codingMatrix.length))
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
					m[i - removed] = codingMatrix[i];
				}
				row = (byte)(vectorIndexToRemove + 1);
				++removed;
			}
			
			
			for (byte i = row; i < m.length; ++i){
				m[i - removed] = codingMatrix[i];
			}
			
			codingMatrix = new char[codingMatrix.length - indexes.size()][h];
			
			codingMatrix = m;
			
			return true;
			
		}catch(MatrixException ex){
			System.err.println("Index out of range.");
			return false;
		}
	}

	/**
	 * <p>
	 * This method perform the Gauss' reduction algorithm.
	 * </p>
	 * It is used to get the vectors' indexes that are linear dependent.
	 * 
	 * @return a list of vectors' indexes that are linear dependent.
	 * 
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	public ArrayList<Byte> gaussAlgorithm(){
		
		double[] [] m = this.castToDoubleMatrix(codingMatrix);
		
		m = this.translate(m);
		
		ArrayList<Byte> linearDipendentIndexVector = new ArrayList<Byte>();
				
		byte i = 0, j = 0;
		
		while((i < m.length) && (j < m[0].length)){
			if(m[i][j] == 0){
				
				//exchanges the row with any other with first digit different by zero
				double[] tempArray;
				double maxPivot = 0;
				byte indexMaxPivot = i;
				for(byte l = (byte) (i+1); l < m.length; ++l){
					if (m[l][j] != 0){
						if (m[l][j] > maxPivot){
								maxPivot = m[l][j];
								indexMaxPivot = l;
						}
					}
				}
				tempArray = m[i];
				m[i] = m[indexMaxPivot];
				m[indexMaxPivot] = tempArray;
			}
			
			double coeff = 0;
			
			for(byte l = (byte) (i+1); l < m.length; ++l){
				if((m[l][j] != 0) && (m[i][j] != 0)){
					coeff = - m[l][j] / m[i][j];
					for(byte k = 0; k < m[0].length; ++k){
						m[i][k] = m[i][k] * coeff;
						m[l][k] += m[i][k];
					}
				}
			}
			
			++i;
			++j;

		}
		for(i = 0; i < m.length; ++i ){
			for(j = 0; j < m[0].length; ++j){
				if(i == j){
					if(m[i][j] == 0)
						linearDipendentIndexVector.add(i);
				}
			}
		}
				
		return linearDipendentIndexVector;
	}

	/**
	 * This method perform a matrix translation.
	 * 
	 * @param matrix
	 * 
	 * @return the matrix translated
	 * 
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	private double[][] translate(double[][] matrix){
		
		double[][] m = new double[matrix[0].length][matrix.length];
		
		for(byte i = 0; i < matrix.length; ++i){
			for(byte j = 0; j < matrix[0].length; ++j){
				m[j][i] = matrix[i][j];
			}
		}
		return m;
	}
	
	private double[][] castToDoubleMatrix(char[][] matrix){
		double[][] doubleMatrix = new double[matrix[0].length][matrix.length];; 
		for(byte i = 0; i < matrix.length; ++i){
			for(byte j = 0; j < matrix[0].length; ++j){
				doubleMatrix[i][j] = (double) matrix[i][j];
			}
		}
		
		return doubleMatrix;
	}
}

