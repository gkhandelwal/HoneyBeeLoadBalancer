package org.cloudbus.cloudsim;

import java.util.ArrayList;

public class randomTickets {

	public static int getRandom(int maxNo)
	{
		ArrayList<Integer> list =   new ArrayList<Integer>();
		list.clear();
		int randomInt;
		for(int i=0;i<10;i++)
		{
			randomInt= (int)(maxNo * Math.random());
			list.add(randomInt);
		}
		randomInt = (int)(10 * Math.random());
		return list.get(randomInt);
		
	}
}
