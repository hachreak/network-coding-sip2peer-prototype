package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource;

import org.hachreak.projects.gfjama.matrix.GaloisField;

/**
 * This class represent a fragment's header that contains info about the fragment's key,
 * the generation's tag, the resource's key and the codingVector.
 * 
 *
 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
 *
 */
public class EncodedFragmentHeader {
	
	private int fragmentKey;
	
//	private byte generationTag;
	
	private char[] codingVector;
	
	private int resourceKey;

	private int fragmentSize;

	private GaloisField galoisField;
	
	public EncodedFragmentHeader(int fragmentKey, int fileId, char[] codingVector, int fragmentSize, GaloisField galoisField){//, byte generationTag) {
		this.fragmentKey = fragmentKey;
		this.resourceKey = fileId;
		this.codingVector = codingVector;
		this.fragmentSize = fragmentSize;
		this.galoisField = galoisField;
		//this.generationTag = generationTag;
	}

	public int getFragmentKey() {
		return fragmentKey;
	}

//	public void setFragmentKey(int fragmentKey) {
//		this.fragmentKey = fragmentKey;
//	}

//	public byte getGenerationTag() {
//		return generationTag;
//	}
//
//	public void setGenerationTag(byte generationTag) {
//		this.generationTag = generationTag;
//	}

	public char[] getCodingVector() {
		return codingVector;
	}

//	public void setCodingVector(char[] codingVector) {
//		this.codingVector = codingVector;
//	}

	public int getResourceKey() {
		return resourceKey;
	}

//	public void setResourceKey(int fileId) {
//		this.resourceKey = fileId;
//	}
	
	/**
	 * TODO Temporary function for generation of key
	 * @return
	 */
	public static int generateKey(){
		return 1 + (int)(Math.random()*100000);
	}

	public int getFragmentSize() {
		return fragmentSize;
	}

	public GaloisField getGaloisField() {
		return galoisField;
	}
	
	
}
