package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.hachreak.projects.gfjama.matrix.GFMatrix;
import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.zoolu.tools.Random;

public class MediaResource {

	private File file;
	private int fragmentSize;
	private GaloisField galoisField;
	private byte[] digest;

	public MediaResource(File file, int fragmentSize, GaloisField galoisField) {
		this.file = file;
		this.fragmentSize = fragmentSize;
		this.galoisField = galoisField;
	}

	public GaloisField getGaloisField() {
		return galoisField;
	}

	public File getFile() {
		return file;
	}

	public int getFragmentSize() {
		return fragmentSize;
	}

	public int getNumberOfFragments() {
		return Math.round(file.length() / fragmentSize) + 1;
	}

	public int getResourceKey() throws NoSuchAlgorithmException, IOException {
		if (digest == null) {
			InputStream inputStream = new BufferedInputStream(
					new FileInputStream(file));

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// DigestInputStream dis = new DigestInputStream(inputStream, md);
			byte cbuf[] = new byte[fragmentSize];
			int offset = 0;
			byte[] dataBytes = new byte[1024];
			int nread = 0;
			while ((nread = inputStream.read(cbuf, offset, fragmentSize)) != -1) {
				// offset += fragmentSize;
				md.update(dataBytes, 0, nread);
			}
			digest = md.digest();
			inputStream.close();
		}

		// return Integer.parseInt(digest.toString());
		// TODO usare il digest per identificare la risorsa!
		return Random.nextInt();
	}

	
//	public static void saveTransposed(OutputStream ostream, char[][] data)
//			throws IOException {
//		Writer w = new BufferedWriter(new OutputStreamWriter(ostream));// new
//																		// FileOutputStream(file)
//		int fragmentSize = data[0].length;
//		// for each fragment
//		for (int i = 0; i < fragmentSize; i++) {
//			char cbuf[] = new char[data.length];
//
//			for (int j = 0; j < data.length; j++) {
//				// char[] v = ;
//				cbuf[j] = data[j][i];// g.get(0, i);
//			}
//			System.out.println(cbuf);
//			w.write(cbuf);
//		}
//		w.flush();
//		w.close();
//	}

	/**
	 * Save the matrix as a sequence of char[]
	 * 
	 * @param ostream stream where to write
	 * @param data data to write
	 * @throws IOException
	 */
	public static void save(OutputStream ostream, char[][] data)
			throws IOException {
		Writer w = new BufferedWriter(new OutputStreamWriter(ostream));

		for (int i = 0; i < data.length; i++) {
//			System.out.println(data[i]);
			w.write(data[i]);
		}
	
		w.flush();
		w.close();
	}

	
	public GFMatrix loadTransposedPiece(int index) throws FileNotFoundException, IOException{
		return MediaResource.loadTransposedPiece(new FileInputStream(file), (int) file.length(), fragmentSize, index, galoisField);
	}
	
	public static GFMatrix loadTransposedPiece(InputStream istream,
			int totalLength, int fragmentSize, int index,
			GaloisField galoisField) throws IOException {
		assert index > fragmentSize : "index > fragment size";

		// reader of file
		Reader reader = new BufferedReader(new InputStreamReader(istream));// new
																			// FileInputStream(file)));

		// create GFMatrix object
		GFMatrix m = new GFMatrix(1, (int) Math.round(totalLength
				/ fragmentSize) + 1, galoisField);

		// get internal matrix
		char A[][] = m.getArray();

		// load vector of original data
		char cbuf[] = new char[fragmentSize];
		int offset = 0, jndex = 0;
		// List<Character> vect = new ArrayList<Character>();
		int ret = 0;
		while ((ret = reader.read(cbuf, offset, fragmentSize)) != -1) {
			// System.out.println(new String(cbuf)+ " - "+ret);
			if (index < cbuf.length) {
				A[0][jndex] = cbuf[index];
				// System.out.print(cbuf[index]);
			}
			// offset += fragmentSize;
			jndex++;
		}

		reader.close();
		return m;
	}
}
