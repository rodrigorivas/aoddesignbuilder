package util;

import java.util.Random;
import java.util.Vector;

public class UniqueID
{
    private static Vector IDlist = new Vector();

    public static String generateUniqueID()
    {
    	Random rnd = new Random();
    	
    	int i_rnd;
        i_rnd = rnd.nextInt(Integer.MAX_VALUE);
        String new_id = null;
        
        new_id = String.valueOf(i_rnd);
        
        while(IDlist.lastIndexOf(new_id) != -1)
        {
            new_id = String.valueOf(i_rnd);
        }
        
        IDlist.addElement(new_id);
        
        return new_id;
    }
}
