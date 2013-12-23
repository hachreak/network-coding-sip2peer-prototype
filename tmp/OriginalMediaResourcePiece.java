package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.hachreak.projects.gfjama.matrix.GFMatrix;
import org.hachreak.projects.gfjama.matrix.GaloisField;

public class OriginalMediaResourcePiece extends GFMatrix {

	/**
	 * Input stream where read the resource
	 */
//	private Reader reader;

	/**
	 * Input stream (equivalent to the Reader)
	 */
//	private InputStream inputStream;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4722842546235754378L;

	/**
	 * Digest of input stream
	 */
//	private byte[] digest = null;

	/**
	 * Fragment Size
	 */
	private int fragmentSize;

	/**
	 * Total size of the stream
	 */
	//private int totalSize;

	/**
	 * Index of value selected in fragment
	 * 
	 * E.g: index = 2;
	 * Fragment a_i    [a_i0, a_i1, {a_i2}, a_i3, a_i4]
	 */
	private int index;

	/**
	 * Resource
	 */
	private File file;

	/**
	 * TODO remove cast and use long instead int for file length
	 * 
	 * @param file file to encode
	 * @param fragmentSize fragment size
	 * @param index index of piece
	 * @param galoisField galois field useful to encode in Galois Field space
	 * @throws IOException
	 */
	public OriginalMediaResourcePiece(File file,
			int fragmentSize, int index, GaloisField galoisField) throws IOException {
		super(1, (int) Math.round(file.length() / fragmentSize)+1, galoisField);
		this.file = file;
		this.index = index;
//		this.inputStream = new FileInputStream(file);
		this.fragmentSize = fragmentSize;
		//this.totalSize = (int) file.length();
		
		loadMatrix();
	}
	
//	public OriginalMediaResourcePiece(int fragmentSize, int index, GaloisField galoisField) throws IOException {
//		super(1, (int) Math.round(file.length() / fragmentSize)+1, galoisField);
//		this.file = file;
//		this.index = index;
//		this.fragmentSize = fragmentSize;
//	}
	
	/**
	 * Load a vector of elements and put in 1D matrix
	 * @throws IOException
	 */
	private void loadMatrix() throws IOException {
		assert index > fragmentSize : "index > fragment size";

		// reader of file
		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		// get internal matrix
		char A[][] = getArray();
		
		// load vector of original data
		char cbuf[] = new char[fragmentSize];
		int offset = 0, jndex = 0;
//		List<Character> vect = new ArrayList<Character>();
		int ret = 0;
		while ((ret = reader.read(cbuf, offset, fragmentSize)) != -1) {
//			System.out.println(new String(cbuf)+ " - "+ret);
			if(index < cbuf.length){
				A[0][jndex] = cbuf[index];
//				System.out.print(cbuf[index]);
			}
//			offset += fragmentSize;
			jndex++;
		}
		
		reader.close();
//		char c[] = ((Character[]) vect.toArray()).toString().toCharArray();
	}

	
}
