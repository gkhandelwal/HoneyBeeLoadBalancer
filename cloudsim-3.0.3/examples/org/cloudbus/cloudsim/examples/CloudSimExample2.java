/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.examples;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletLoadBalancer;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelPlanetLabInMemory;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.assignedVM;
import org.cloudbus.cloudsim.getRandomValue;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


/**
 * A simple example showing how to create
 * a datacenter with one host and run two
 * cloudlets on it. The cloudlets run in
 * VMs with the same MIPS requirements.
 * The cloudlets will take the same time to
 * complete the execution.
 */
public class CloudSimExample2 {

	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;
	//private static List<Cloudlet> cloudletList1;
	private static int numofTestVM=2;
	private static int numoftestCloudlet=100;

	/** The vmlist. */
	private static List<Vm> vmlist;
	//private static List<Vm> vmlist1;

	/**
	 * Creates main() to run this example
	 */
	public static void main(String[] args) {

		Log.printLine("Starting LoadBalancer...");

	        try {
	        	// First step: Initialize the CloudSim package. It should be called
	            	// before creating any entities.
	            	int num_user = 1;   // number of cloud users
	            	Calendar calendar = Calendar.getInstance();
	            	boolean trace_flag = false;  // mean trace events

	            	// Initialize the CloudSim library
	            	CloudSim.init(num_user, calendar, trace_flag);

	            	// Second step: Create Datacenters
	            	//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
	            	@SuppressWarnings("unused")
					Datacenter datacenter0 = createDatacenter("Datacenter_0");

	            	//Third step: Create Broker
	            	DatacenterBroker broker = createBroker("broker0");
	            	int brokerId = broker.getId();
	            	//DatacenterBroker broker1 = createBroker("broker1");
	            	//int brokerId1 = broker1.getId();

	            	//Fourth step: Create one virtual machine
	            	vmlist = new ArrayList<Vm>();
	            	//vmlist1 = new ArrayList<Vm>();
	            	double costPerProcessor = 1;
	            	//VM description
	            	int vmid = 0;
	            	int mips = 550;
	            	long size = 10000; //image size (MB)
	            	int ram = 512; //vm memory (MB)
	            	long bw = 1000;
	            	int pesNumber = 2; //number of cpus
	            	String vmm = "Xen"; //VMM name
	            	
	            	// for CPU 
	            	Scanner scanCPU;
	                File fileCPU = new File("/home/gaurav/Desktop/randomCPUFile.txt");
	                try {
	                    scanCPU = new Scanner(fileCPU);
	                    while(scanCPU.hasNextDouble())
	                    {
	                    	double doubletemp = scanCPU.nextDouble();
	                        getRandomValue.setCPUValue(doubletemp);
	                        
	                    }

	                } catch (FileNotFoundException e1) {
	                        e1.printStackTrace();
	                }
	            	
	            	
	            	
	            	// for double
	            	Scanner scan;
	                File file = new File("/home/gaurav/Desktop/randomFile.txt");
	                try {
	                    scan = new Scanner(file);
	                    while(scan.hasNextDouble())
	                    {
	                    	double doubletemp = scan.nextDouble();
	                        getRandomValue.setValue(doubletemp);
	                        
	                    }

	                } catch (FileNotFoundException e1) {
	                        e1.printStackTrace();
	                }

	                // for integer
	            	Scanner scanint;
	                File fileint = new File("/home/gaurav/Desktop/randomIntFile.txt");
	                try {
	                	scanint = new Scanner(fileint);
	                    while(scanint.hasNextInt())
	                    {
	                    	int inttemp = scanint.nextInt();
	                        getRandomValue.setIntValue(inttemp);
	                    }

	                } catch (FileNotFoundException e1) {
	                        e1.printStackTrace();
	                }
	                
	                
	            	//create two VMs
	            	Vm vm1 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
	            	//Vm vm1 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared(),costPerProcessor*pesNumber);
	            	//assignedVM.setVM(vmid);

	            	vmid++;
	            	Vm vm2 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());

	            	//add the VMs to the vmList
	            	vmlist.add(vm1);
	            	vmlist.add(vm2);
	            	
	            	// adding to OVM and LVM
	            	CloudletLoadBalancer.setLVM(vm1.getId(),0.0);
	            	CloudletLoadBalancer.setLVM(vm2.getId(),0.0);
	            	
	            	//submit vm list to the broker
	            	broker.submitVmList(vmlist);
	            	


	            	//Fifth step: Create two Cloudlets
	            	cloudletList = new ArrayList<Cloudlet>();
	            	

	            	//Cloudlet properties
	            	int id = 0;
	            	pesNumber=1;
	            	long length = 700;
	            	long fileSize = 300;
	            	long outputSize = 300;
	            	UtilizationModel utilizationModel = new UtilizationModelFull();
	            	//UtilizationModel utilizationModel = new UtilizationModelStochastic();

	            	
	            	for(int cid=0;cid<numoftestCloudlet;cid++)
	            	{
	            		//int randomInt = (int)(10.0 * Math.random());
	            		int randint = getRandomValue.getIntValue();
	            		//System.out.println(randint);
	            		Cloudlet cloudlet = new Cloudlet(cid, length*randint, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
		            	/*if(cid%2==0)
		            		cloudlet.setUserId(brokerId);
		            	else
		            		cloudlet.setUserId(brokerId1);
		            	
		            	cloudlet.setVmId(cid%2);
		            	//cloudlet.setVmId(0);*/
		            	/*if(cid%2==0)
		            		cloudletList.add(cloudlet);
		            	else
		            		cloudletList1.add(cloudlet);*/
	            		
	            		cloudlet.setUserId(brokerId);
	            		//cloudlet.setVmId(cid%2);
	            		cloudletList.add(cloudlet);
	      
	            	}

	            	//submit cloudlet list to the broker
	            	broker.submitCloudletList(cloudletList);
	            	// Sixth step: Starts the simulation
	            	CloudSim.startSimulation();


	            	// Final step: Print results when simulation is over
	            	List<Cloudlet> newList = broker.getCloudletReceivedList();
	            	//List<Cloudlet> newList1 = broker1.getCloudletReceivedList();
	            	CloudSim.stopSimulation();
	            	// try to merge these two
	            	printCloudletList(newList);
	            	//printCloudletList(newList1);
	            	
	            	Log.printLine("LoadBalancing finished!");
	            	printSolution(newList);
	            	//printSolution(newList1);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            Log.printLine("The simulation has been terminated due to an unexpected error");
	        }
	    }

		private static Datacenter createDatacenter(String name){

	        // Here are the steps needed to create a PowerDatacenter:
	        // 1. We need to create a list to store
	    	//    our machine
	    	List<Host> hostList = new ArrayList<Host>();

	        // 2. A Machine contains one or more PEs or CPUs/Cores.
	    	// In this example, it will have only one core.
	    	List<Pe> peList = new ArrayList<Pe>();

	    	int mips = 1100;

	        // 3. Create PEs and add these into a list.
	    	peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
	    	//gaurav remove it later
	    	peList.add(new Pe(1, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
	    	//peList.add(new Pe(2, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
	    	//peList.add(new Pe(3, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

	        //4. Create Host with its id and list of PEs and add them to the list of machines
	        int hostId=0;
	        int ram = 20480; //host memory (MB)
	        long storage = 1000000; //host storage
	        int bw = 10000;

	        hostList.add(
	    			new Host(
	    				hostId,
	    				new RamProvisionerSimple(ram),
	    				new BwProvisionerSimple(bw),
	    				storage,
	    				peList,
	    				new VmSchedulerTimeShared(peList)
	    			)
	    		); // This is our machine


	        // 5. Create a DatacenterCharacteristics object that stores the
	        //    properties of a data center: architecture, OS, list of
	        //    Machines, allocation policy: time- or space-shared, time zone
	        //    and its price (G$/Pe time unit).
	        String arch = "x86";      // system architecture
	        String os = "Linux";          // operating system
	        String vmm = "Xen";
	        double time_zone = 10.0;         // time zone this resource located
	        double cost = 3.0;              // the cost of using processing in this resource
	        double costPerMem = 0.05;		// the cost of using memory in this resource
	        double costPerStorage = 0.001;	// the cost of using storage in this resource
	        double costPerBw = 0.0;			// the cost of using bw in this resource
	        LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

	        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
	                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


	        // 6. Finally, we need to create a PowerDatacenter object.
	        Datacenter datacenter = null;
	        try {
	            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return datacenter;
	    }

	    //We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	    //to the specific rules of the simulated scenario
		// earlier changes done by gaurav : private static DatacenterBroker createBroker()
	    private static DatacenterBroker createBroker(String name){

	    	DatacenterBroker broker = null;
	        try {
			//broker = new DatacenterBroker("Broker");
	        	broker = new DatacenterBroker(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	    	return broker;
	    }

	    
	    private static void printSolution(List<Cloudlet> list) throws IOException 
	    {
	    	int size = list.size();
	        Cloudlet cloudlet;
	        
	        Log.printLine();
	        ArrayList<IntervalCalculation> mergeintervalList0;
	        ArrayList<IntervalCalculation> mergeintervalList1;
	        ArrayList<IntervalCalculation> intervalList0 = new ArrayList<IntervalCalculation> ();
	        ArrayList<IntervalCalculation> intervalList1 = new ArrayList<IntervalCalculation> ();
	     
	        for (int i = 0; i < size; i++) 
	        {
	            cloudlet = list.get(i);
	            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS)
	            {
	            	IntervalCalculation interval = new IntervalCalculation();
	            	interval.starttime = cloudlet.getExecStartTime();
	            
		            //Log.printLine(interval.starttime);
		            interval.endtime =  cloudlet.getFinishTime();
		            //Log.printLine(interval.endtime);
		            interval.cloudletID = cloudlet.getCloudletId();
		            if(cloudlet.getVmId()==0)
		            	intervalList0.add(interval);
		            else
		            	intervalList1.add(interval);
	            }
	        }
	        /*for(int i=0;i<intervalList0.size();i++)
	        	 Log.printLine("start : " +  intervalList0.get(i).starttime + "entime : " +  intervalList0.get(i).endtime );*/
	        
	        mergeintervalList0 = mergeNew(intervalList0);
	        mergeintervalList1 = mergeNew(intervalList1);
	       
	        double costTemp=0, costTemp1 = 0;
	        for(IntervalCalculation item : mergeintervalList0)
	        {
	        	costTemp = costTemp + item.endtime - item.starttime;
	        	//Log.printLine(item.starttime + ":" + item.endtime);
	        }
	        Log.printLine("List 0 : cost " +  costTemp);
	        //Log.printLine("List 1");
	        //costTemp = 0;
	        for(IntervalCalculation item : mergeintervalList1)
	        {
	        	costTemp1 = costTemp1 + item.endtime - item.starttime;
	        	//Log.printLine(item.starttime + ":" + item.endtime);
	        }
	        Log.printLine("List 1 : cost " +  costTemp1);
	        Log.printLine("total cost " +  (costTemp1 + costTemp));
	        
	        
	       
	        /* write randomFileLogic */
	        /*Double randomDouble;
	        
			String strFilePath = "/home/gaurav/Desktop/randomCPUFile.txt";
	        FileOutputStream fos; 
	        DataOutputStream dos;
	        try {
				fos= new FileOutputStream(strFilePath);
				dos= new DataOutputStream(fos);
				for(int i=0;i<=1000;i++)
				{
					randomDouble= (2.0 * Math.random());
					dos.writeBytes(Double.toString(randomDouble));
					dos.writeBytes("\n");
				}
		        dos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
	        /* write random file logic ends here */
	        
	        /* write randomintLogic */
	        /*int randomInt; 
	        
			String strFilePath = "/home/gaurav/Desktop/randomIntFile.txt";
	        FileOutputStream fos; 
	        DataOutputStream dos;
	        try {
				fos= new FileOutputStream(strFilePath);
				dos= new DataOutputStream(fos);
				for(int i=0;i<=1000;i++)
				{
					randomInt = (int)(10.0 * Math.random())+1;
					dos.writeBytes(Integer.toString(randomInt));
					dos.writeBytes("\n");
				}
		        dos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
	        /* write random int logic ends here */
	        
	    }
	    
	    
	    /**
	     * Prints the Cloudlet objects
	     * @param list  list of Cloudlets
	     */
	    private static void printCloudletList(List<Cloudlet> list) {
	        int size = list.size();
	        Cloudlet cloudlet;

	        String indent = "    ";
	        Log.printLine();
	        Log.printLine("========== OUTPUT ==========");
	        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
	                "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time"+ indent + "Waiting Time");
	        double waitTimeSum = 0.0;
	        double CPUTimeSum = 0.0;
	        int totalValues=0;
	        DecimalFormat dft = new DecimalFormat("###.##");
	        for (int i = 0; i < size; i++) {
	            cloudlet = list.get(i);
	            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

	            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
	                Log.print("SUCCESS");
	                CPUTimeSum = CPUTimeSum + cloudlet.getActualCPUTime();
	                waitTimeSum = waitTimeSum + cloudlet.getWaitingTime();
	                totalValues++;
	            	Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
	                     indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
                             indent + indent + dft.format(cloudlet.getFinishTime())+ indent + indent + dft.format(cloudlet.getWaitingTime()));
	            }
	        }
	        Log.printLine("TotalCPUTime : " + CPUTimeSum);
	        Log.printLine("TotalWaitTime : " + waitTimeSum);
	        Log.printLine("TotalCloudletsFinished : " + totalValues);
	    }
	    
	    public static ArrayList<IntervalCalculation> merge(ArrayList<IntervalCalculation> intervals) 
	    {

	        if(intervals.size() == 0 || intervals.size() == 1)
	            return intervals;
	        
	        Collections.sort(intervals, new Comparator<IntervalCalculation>() 
	        {
	            @Override
	            public int compare(IntervalCalculation o1, IntervalCalculation o2) {
	                return Double.compare(o1.starttime, o2.starttime);
	            }
	        });
	        ArrayList<IntervalCalculation> result = new ArrayList<IntervalCalculation>();
	        result.clear();
	        IntervalCalculation prev = intervals.get(0);
	        
	        for (int i = 1; i < intervals.size(); i++) 
	        {
	        	IntervalCalculation curr = intervals.get(i);
	        	if (prev.endtime >= curr.starttime) {
					// merged case
	        		IntervalCalculation merged = new IntervalCalculation(prev.starttime, Math.max(prev.endtime, curr.endtime));
					prev = merged;
				} else {
					result.add(prev);
					prev = curr;
				}
	        }

	        result.add(prev);
	        return result;
	    }
	    public static ArrayList<IntervalCalculation> mergeNew(ArrayList<IntervalCalculation> intervals) {
	        if (intervals==null ||intervals.size()<2){
	            return intervals;
	        }
	        
	        ArrayList<IntervalCalculation> result=new ArrayList<IntervalCalculation>();
	       
	        Comparator<IntervalCalculation> intervalComperator=new Comparator<IntervalCalculation>(){
	            public int compare(IntervalCalculation i1, IntervalCalculation i2){
	                double i1St=i1.starttime;
	                double i2St=i2.starttime;
	                return Double.compare(i1St,i2St);
	                
	            }
	        };
	        
	        Collections.sort(intervals, intervalComperator);
	        
	        IntervalCalculation current=intervals.get(0);
	        
	        int i=1;
	        
	        while (i<intervals.size() ){
	        	IntervalCalculation currentToCompare=intervals.get(i);
	            if (current.endtime<currentToCompare.starttime){
	                result.add(current);
	                current=currentToCompare;
	                
	            }
	            else{
	                current.endtime=Math.max(current.endtime, currentToCompare.endtime);
	                
	            }
	            i++;
	        }
	        
	        result.add(current);
	        
	        return result;
	    }
	
}
