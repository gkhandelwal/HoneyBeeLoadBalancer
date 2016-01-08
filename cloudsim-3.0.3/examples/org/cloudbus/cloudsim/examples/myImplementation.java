package org.cloudbus.cloudsim.examples;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletLoadBalancer;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
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
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.getRandomValue;
import org.cloudbus.cloudsim.helperClass;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


public class myImplementation {

	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;
	//private static List<Cloudlet> cloudletList1;
	private static int numofTestVM=15;
	private static int numoftestCloudlet=100;

	/** The vmlist. */
	private static List<Vm> vmlist;

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
	            	
	            	// In Case you want to create random files, create random files
	            	//helperClass.createRandomFile(10000);
	            	// Read Random Files 
	            	helperClass.readRandomFile();
	            	
	            	// set Algorithm Type
	            	helperClass.setAlgorithmType(helperClass.HONEYBEEALONEWITHUNIDIRECTION);
	            	
	            	//set no. of VMS in helper class
	            	helperClass.setNoofVms(numofTestVM);
	                
	              //Fourth step: Create one virtual machine
	            	vmlist = new ArrayList<Vm>();
	            
	            	//VM description
	            	int mips = 550;
	            	long size = 10000; //image size (MB)
	            	int ram = 512; //vm memory (MB)
	            	long bw = 1000;
	            	int npes = 1; // number of processor
	            	
	            	String vmm = "Xen"; //VMM name
	                
	                
	                for(int vmid=0;vmid<numofTestVM;vmid++)
	                {
	                	vmlist.add(new Vm(vmid, brokerId, mips*((vmid%2)+1),(vmid%2)+1, ram*((vmid%2)+1), bw*((vmid%2)+1), size*((vmid%2)+1), vmm, new CloudletSchedulerDynamicWorkload(mips*((vmid%2)+1), (vmid%2)+1)));
	                	//vmlist.add(new Vm(vmid, brokerId, mips,1, ram, bw, size, vmm, new CloudletSchedulerSpaceShared()));
	                }
	            	
	            	
	            	// adding to OVM and LVM
	                for(int vmid=0;vmid<numofTestVM;vmid++)
	                {
	                	CloudletLoadBalancer.setLVM(vmlist.get(vmid).getId(),0.0);
	                }
	            	
	            	
	            	//submit vm list to the broker
	            	broker.submitVmList(vmlist);
	            	
	            	//Fifth step: Create Cloudlets
	            	cloudletList = new ArrayList<Cloudlet>();
	            	//Cloudlet properties
	            	int pesNumber=1;
	            	long length = 700;
	            	long fileSize = 300;
	            	long outputSize = 300;
	            	UtilizationModel utilizationModel = new UtilizationModelFull();
	            	

	            	
	            	for(int cid=0;cid<numoftestCloudlet;cid++)
	            	{
	            		int randint = getRandomValue.getIntValue();
	            		Cloudlet cloudlet = new Cloudlet(cid, length*randint, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	            		cloudlet.setUserId(brokerId);
	            		cloudletList.add(cloudlet);
	      
	            	}

	            	//submit cloudlet list to the broker
	            	broker.submitCloudletList(cloudletList);
	            	// Sixth step: Starts the simulation
	            	CloudSim.startSimulation();


	            
	            	List<Cloudlet> newList = broker.getCloudletReceivedList();
	            	
	            	CloudSim.stopSimulation();
	            	
	            	printCloudletList(newList);
	            	Log.printLine("LoadBalancing finished!");
	            	
	            	
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
	    	for(int vmid=0;vmid<2*numofTestVM;vmid++)
	    	{
	    		peList.add(new Pe(vmid, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
	    	}

	        //4. Create Host with its id and list of PEs and add them to the list of machines
	        int hostId=0;
	        int ram = 204800000; //host memory (MB)
	        long storage = 100000000; //host storage
	        int bw = 100000000;

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
	        Log.printLine();
	        Log.printLine();
	        Log.printLine("TotalCPUTime : " + CPUTimeSum);
	        Log.printLine("TotalWaitTime : " + waitTimeSum);
	        Log.printLine("TotalCloudletsFinished : " + totalValues);
	        Log.printLine();
	        Log.printLine();
	        
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
