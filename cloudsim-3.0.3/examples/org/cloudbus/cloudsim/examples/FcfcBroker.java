package org.cloudbus.cloudsim.examples;

import org.cloudbus.cloudsim.DatacenterBroker;

/**
 * A Broker that schedules Tasks to the VMs 
 * as per FCFS Scheduling Policy
 * @author Linda J
 *
 */
public class FcfcBroker extends DatacenterBroker {

	public FcfcBroker(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
	}

	//scheduling function
	public void scheduleTaskstoVms(){
		int reqTasks=cloudletList.size();
		int reqVms=vmList.size();
		
		System.out.println("\n\tFCFS Broker Schedules\n");
    	for(int i=0;i<reqTasks;i++){
    		bindCloudletToVm(i, (i%reqVms));
    		System.out.println("Task"+cloudletList.get(i).getCloudletId()+" is bound with VM"+vmList.get(i%reqVms).getId());
    	}
    	
    	System.out.println("\n");
	}
}

