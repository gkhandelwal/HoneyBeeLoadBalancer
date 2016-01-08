package org.cloudbus.cloudsim;

import java.util.ArrayList;

public class lotteryTicket {
	double cpuTime;
	int vmid;
	public ArrayList<Integer> tickets;
	int numofTickets;
	public lotteryTicket(double cpu, int vm)
	{
		this.cpuTime = cpu;
		this.vmid = vm;
		tickets = new ArrayList<Integer>() ;
	}
}
