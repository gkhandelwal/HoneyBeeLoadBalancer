package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.List;

public  class getRandomValue {
	//public  list<double> list = new ArrayList<double>();
	private static List<Double> passedList = new ArrayList<Double>();
	private static List<Integer> passedIntList = new ArrayList<Integer>();
	private static List<Double> passedCPUList = new ArrayList<Double>();
	public static double getValue()
	{
		double returnelement = passedList.get(0);
		passedList.remove(0);
		passedList.add(returnelement);
		return returnelement ;
	}
	public static void setValue(double value)
	{
		passedList.add(value);
	}
	public static double getCPUValue()
	{
		double returnelement = passedCPUList.get(0);
		passedCPUList.remove(0);
		passedCPUList.add(returnelement);
		return returnelement ;
	}
	public static void setCPUValue(double value)
	{
		passedCPUList.add(value);
	}
	public static int getIntValue()
	{
		int returnelement = passedIntList.get(0);
		passedIntList.remove(0);
		passedIntList.add(returnelement);
		return returnelement ;
	}
	public static void setIntValue(int value)
	{
		passedIntList.add(value);
	}
}
