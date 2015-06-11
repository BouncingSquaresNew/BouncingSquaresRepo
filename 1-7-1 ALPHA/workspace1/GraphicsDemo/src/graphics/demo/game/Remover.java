package graphics.demo.game;

/* Name: Remocer.java
 * Author: Ben Winchester
 * Date: 13/12/2014
 * Purpose: A class, which contains objects that store the information for
 * removing other objects from arraylists
 */

public class Remover {

	public int id = 0; //A paramater for specifying the id of an object to be removed
	public int type = 0; //A paramater for specifyg the type (NOTE, OBSOLETE)
	
	public Remover(int id)
	{
		this.id = id;
		this.type = 0;
	}

}
