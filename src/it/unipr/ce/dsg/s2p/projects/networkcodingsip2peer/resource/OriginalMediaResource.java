package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

import org.hachreak.projects.gfjama.matrix.GFMatrix;
import org.hachreak.projects.gfjama.matrix.GaloisField;
import org.zoolu.tools.Random;

public class OriginalMediaResource {

	private File file;
	private int fragmentSize;
	private byte[] digest;
	private GaloisField galoisField;
//	private List<Fragment> fragments;

	public OriginalMediaResource(File file, int fragmentSize, GaloisField galoisField) {
//		setFile(file);
//		setFragmentSize(fragmentSize);
		this.file = file;
		this.fragmentSize = fragmentSize;
		this.galoisField = galoisField;
	}
	
	public OriginalMediaResource(File file, int fragmentSize) {
//		setFile(file);
//		setFragmentSize(fragmentSize);
		this.file = file;
		this.fragmentSize = fragmentSize;
//		this.galoisField = galoisField;
	}
	
	public void save(List<GFMatrix> matrixs) throws IOException{
		Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		fragmentSize = matrixs.get(0).getColumnDimension();
		for(int i=0; i<fragmentSize; i++){
			char cbuf[] = new char[matrixs.size()];
			for(int j=0; j<matrixs.size(); j++){
				GFMatrix g = matrixs.get(j);
//				System.out.print("("+i+" "+j+") ");
				cbuf[j] = g.get(0, i);
			}
			System.out.println(cbuf);
//			CharSequence csq = new CharArrayString(cbuf);
			w.write(cbuf);
		}
		w.flush();w.close();
	}

	public GaloisField getGaloisField() {
		return galoisField;
	}

	public File getFile() {
		return file;
	}

//	private void setFile(File file) {
//		this.file = file;
//	}

	public int getFragmentSize() {
		return fragmentSize;
	}

//	private void setFragmentSize(int fragmentSize) {
//		this.fragmentSize = fragmentSize;
//	}
	
	public int getNumberOfFragments(){
		return Math.round(file.length() / fragmentSize) + 1;
	}
	
	public OriginalMediaResourcePiece getPiece(int index) throws IOException{
		return new OriginalMediaResourcePiece(file, fragmentSize, index, galoisField);
	}

	public int getResourceKey() throws NoSuchAlgorithmException,
			IOException {
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
			// //convert the byte to hex format method 1
			// StringBuffer sb = new StringBuffer();
			// for (int i = 0; i < digest.length; i++) {
			// sb.append(Integer.toString((digest[i] & 0xff) + 0x100,
			// 16).substring(1));
			// }
			//
			// System.out.println("Hex format : " + sb.toString());
			// //convert the byte to hex format method 2
			// StringBuffer hexString = new StringBuffer();
			// for (int i=0;i<digest.length;i++) {
			// hexString.append(Integer.toHexString(0xFF & digest[i]));
			// }
			//
			// System.out.println("Hex format : " + hexString.toString());
			// dis.close();
			inputStream.close();
		}

		//return Integer.parseInt(digest.toString());
		// TODO usare il digest per identificare la risorsa!
		return Random.nextInt();
	}

}
