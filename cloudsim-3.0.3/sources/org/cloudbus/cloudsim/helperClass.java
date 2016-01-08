package org.cloudbus.cloudsim;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

public class helperClass {
public static int HONEYBEEALONEWITHUNIDIRECTION=1;
public static int HONEYBEEALONEWITHFCFSTYPE=2;
public static int HONEYBEEWITHLOTTERY=3;
public static int FCFS=4;
public static int LOTTERY=5;

static int numofVms;
static int flagForAlgorithm;
public static int getNoofVms()
{
	return numofVms;
}
public static void setNoofVms(int numofVm)
{
	numofVms = numofVm;
}
public static void createRandomFile(int num)
{

    Double randomDouble;
    String absoluteFilePath = new File("").getAbsolutePath();
    
	String strFilePath = absoluteFilePath.concat("/SimulationFiles/randomCPUFile.txt");
    FileOutputStream fos; 
    DataOutputStream dos;
    try {
		fos= new FileOutputStream(strFilePath);
		dos= new DataOutputStream(fos);
		for(int i=0;i<=num;i++)
		{
			randomDouble= (2.0 * Math.random());
			dos.writeBytes(Double.toString(randomDouble));
			dos.writeBytes("\n");
		}
        dos.close();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
   
    strFilePath = absoluteFilePath.concat("/SimulationFiles/randomFile.txt");
    try {
		fos= new FileOutputStream(strFilePath);
		dos= new DataOutputStream(fos);
		for(int i=0;i<=num;i++)
		{
			randomDouble= (500 * Math.random());
			dos.writeBytes(Double.toString(randomDouble));
			dos.writeBytes("\n");
		}
        dos.close();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
    
    int randomInt; 
    
	strFilePath = absoluteFilePath.concat("/SimulationFiles/randomIntFile.txt");
    try {
		fos= new FileOutputStream(strFilePath);
		dos= new DataOutputStream(fos);
		for(int i=0;i<=num;i++)
		{
			randomInt = (int)(10.0 * Math.random())+1;
			dos.writeBytes(Integer.toString(randomInt));
			dos.writeBytes("\n");
		}
        dos.close();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
    
    
}
public static void readRandomFile()
{
	String absoluteFilePath = new File("").getAbsolutePath();
	// for CPU 
	Scanner scanCPU;
    File fileCPU = new File(absoluteFilePath.concat("/SimulationFiles/randomCPUFile.txt"));
    try {
        scanCPU = new Scanner(fileCPU);
        while(scanCPU.hasNextDouble())
        {
        	double doubletemp = scanCPU.nextDouble();
            getRandomValue.setCPUValue(doubletemp);
            
        }
        scanCPU.close();
    } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return;
    }

	// for double
	Scanner scan;
    File file = new File(absoluteFilePath.concat("/SimulationFiles/randomFile.txt"));
    try {
        scan = new Scanner(file);
        while(scan.hasNextDouble())
        {
        	double doubletemp = scan.nextDouble();
            getRandomValue.setValue(doubletemp);
            
        }
        scan.close();

    } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return;
    }

    // for integer
	Scanner scanint;
	
    File fileint = new File(absoluteFilePath.concat("/SimulationFiles/randomIntFile.txt"));
    try {
    	scanint = new Scanner(fileint);
        while(scanint.hasNextInt())
        {
        	int inttemp = scanint.nextInt();
            getRandomValue.setIntValue(inttemp);
        }
        scanint.close();

    } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return;
    }
}
public static int getAlgorithmType()
{
	return flagForAlgorithm;
}
public static void setAlgorithmType(int flag)
{
	flagForAlgorithm = flag;
}
}
