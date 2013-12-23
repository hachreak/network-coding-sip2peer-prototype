package it.unipr.ce.dsg.s2p.projects.networkcodingsip2peer.resource;

import java.util.ArrayList;

/**
 * <p>
 * This class represent a generic resource in the storage p2p system. First a resource is associated to
 * a ClientNode, next it is published in the system and associated to a responsible SuperNode
 * and a set of StorageNodes.
 * </p>
 * 
 * @author  Stefano Bonelli (stefano.bonelli@studenti.unipr.it)
 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
 * TODO da rivedere la sua implementazione!!!!
 */
public class MediaResourceOLD{
	
	private int totalNumberOfFragments = 0;
	private int thresholdFragments = 0;
	private int minFragments = 0;
	
	private int resourceKey = 0;
	
	private int resourceSize = 0;
	
	private byte generationsAmount = 0;


	public ArrayList< char[] > fragments = new ArrayList< char[] > ();
	
	/**
	 * TODO da convertire per accettare una risorsa (file) in ingresso!!!!! magari creare una classe figlia FileMediaResource
	 * @param resourceKey
	 * @param resourceSize
	 */
	public MediaResourceOLD(int resourceKey, ArrayList< char[] > fragments){
		
		this.resourceKey = resourceKey;
		this.fragments = fragments;
		if(fragments != null && fragments.size() > 0){
			this.resourceSize = fragments.get(0).length * fragments.size();
		}else{
			this.resourceSize = 0;
		}
	}

	@Override
	public boolean equals(Object o) {
		int rKey = ((MediaResourceOLD) o).getResourceKey();
		int rSize = ((MediaResourceOLD) o).getSize();
		if((rKey == this.resourceKey) && (rSize == this.resourceSize))
			return true;
		else
			return false;
	}

	/**
	 * <p>
	 * Returns the resource file size in Mega Bytes. 
	 * </p>
	 * 
	 * @author  Stefano Bonelli (stefano.bonelli@studenti.unipr.it)
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	public int getSize() {
		return resourceSize;
	}
	
	/**
	 * <p>
	 * Returns the resource key. 
	 * </p>
	 * 
	 * @author  Stefano Bonelli (stefano.bonelli@studenti.unipr.it)
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	public int getResourceKey() {
		return resourceKey;
	}

	/**
	 * <p>
	 * Sets the resource key. 
	 * </p>
	 * 
	 * @author  Stefano Bonelli (stefano.bonelli@studenti.unipr.it)
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	public void setResourceKey(int resourceKey) {
		this.resourceKey = resourceKey;
	}
	
	/**
	 * <p>
	 * Returns the total number of original fragments for the resource. 
	 * </p>
	 * 
	 * @author  Stefano Bonelli (stefano.bonelli@studenti.unipr.it)
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	public int getTotalNumberOfFragments() {
		return fragments.size();
	}

	/**
	 * <p>
	 * Returns the minimum number of fragments needed for reconstruct the resource. 
	 * </p>
	 * 
	 * @author  Stefano Bonelli (stefano.bonelli@studenti.unipr.it)
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	public int getMinFragments() {
		return fragments.size();//minFragments;
	}

	/**
	 * <p>
	 * Returns the threshold number of fragments to activate the resource maintenance process. 
	 * </p>
	 * 
	 * @author  Stefano Bonelli (stefano.bonelli@studenti.unipr.it)
	 * @author  Riccardo Bussandri (riccardo.bussandri@studenti.unipr.it)
	 */
	public int getThresholdFragments(){
		return thresholdFragments;
	}
	
	public void setTotalNumberOfFragments(int totalNumberOfFragments) {
		this.totalNumberOfFragments = totalNumberOfFragments;
	}

	public void setThresholdFragments(int thresholdFragments) {
		this.thresholdFragments = thresholdFragments;
	}

	public void setMinFragments(int minFragments) {
		this.minFragments = minFragments;
	}

	public void setResourceSize(int resourceSize) {
		this.resourceSize = resourceSize;
	}

	public void setFragments(ArrayList< char[] > fragments) {
		this.fragments = fragments;
	}

	public byte getGenerationsAmount() {
		return (byte) fragments.size();//generationsAmount;
	}
	
	/**
	 * TODO temporaneamente consideriamo i frammenti come liste di vettori char[] con un carattere solo
	 * @return
	 */
	public char[] getFragments(){
		char a[] = new char[fragments.size()];
		for(int i=0; i<fragments.size(); i++){
			char x = fragments.get(i)[0];
			a[i] = x;
			
		}
		return a;
	}

	public void setGenerationsAmount(byte generationSize) {
		this.generationsAmount = this.computeGenerationsAmount(resourceSize, generationSize);
		
	}
	
	private byte computeGenerationsAmount(int resourceSize, byte generationSize){
		
		byte generationsAmount = 1;
		
		if(resourceSize > 15)
			generationsAmount = (byte)(resourceSize / 15);
		/*
		System.out.println("generationsAmount = " + generationsAmount);
		System.out.println("generationSize = " + generationSize);
		System.out.println("GenerationSize.BIG.getValue() = " + GenerationSize.BIG.getValue());
		*/
		if (generationSize == GenerationSize.BIG.getValue() 
				&& generationsAmount > 1)
			return (byte) (generationsAmount / 2);
		else
			return generationsAmount;
	}
}

