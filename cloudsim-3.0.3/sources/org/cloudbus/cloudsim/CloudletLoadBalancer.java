package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

import org.cloudbus.cloudsim.core.CloudSim;

public class CloudletLoadBalancer 
{
	static ArrayList<mytrialVM> OVM =   new ArrayList<mytrialVM>();
	static ArrayList<mytrialVM> LVM =   new ArrayList<mytrialVM>();
	static ArrayList<mytrialVM> BVM =   new ArrayList<mytrialVM>();
	
	
	static double  threshHold = 0.9;
	static int counterLoadBalancer = 0;
	public static void setLVM(int vmID, double CPUvalue)
	{
		
		LVM.add( new mytrialVM(vmID,CPUvalue));
	}
	
	public static boolean containsUserFun(ArrayList<mytrialVM> temp, int tempVmid)
	{
		for(mytrialVM item: temp)
		{
			if(item.vmid==tempVmid)
				return true;
		}
		return false;
	}
	public static int HBLoadBalancer(List<Vm> list, Cloudlet cl)
	{
		for(int i=0;i<list.size();i++)
		{
			//Log.print(list.get(i).getId() + " : " + list.get(i).getTotalUtilizationOfCpu(CloudSim.clock())+ " ");
		}
		//Log.printLine();
		//Log.printLine(" LVM :"+LVM.size()+ " OVM :"+ OVM.size() + " BVM :"+ BVM.size());
		if(OVM.isEmpty())
		{
			//Log.printLine( ": System is balanced ");
			//return;
		}
		else
		{
			//Log.printLine(counterLoadBalancer);
			counterLoadBalancer++;
			if(LVM.isEmpty())
			{
				//Log.printLine(": No underload VM available ");
				//return;
			}
		}
		
		// update OVM, BVM and LVM list with current CPU values
		for(int i=0;i<OVM.size();i++)
		{
			int tempVmid = OVM.get(i).vmid;
			for( int j=0;j<list.size();j++)
			{
				if(list.get(j).getId()==tempVmid)
				{
					OVM.get(i).cpuValue = list.get(j).getTotalUtilizationOfCpu(CloudSim.clock());
					break;
				}
			}	
		}
		
		for(int i=0;i<BVM.size();i++)
		{
			int tempVmid = BVM.get(i).vmid;
			for( int j=0;j<list.size();j++)
			{
				if(list.get(j).getId()==tempVmid)
				{
					BVM.get(i).cpuValue = list.get(j).getTotalUtilizationOfCpu(CloudSim.clock());
					break;
				}
			}	
		}
		
		for(int i=0;i<LVM.size();i++)
		{
			int tempVmid = LVM.get(i).vmid;
			for( int j=0;j<list.size();j++)
			{
				if(list.get(j).getId()==tempVmid)
				{
					LVM.get(i).cpuValue = list.get(j).getTotalUtilizationOfCpu(CloudSim.clock());
					break;
				}
			}	
		}
		
		// shifting of things between OVM, LVM and BVM
		
		for(int i=0;i<OVM.size();i++)
		{
			if(OVM.get(i).cpuValue<threshHold)
			{
				LVM.add(new mytrialVM(OVM.get(i).vmid,OVM.get(i).cpuValue));
				OVM.get(i).vmid=-1;
				//OVM.remove(item);
				//Log.printLine(CloudSim.clock() + " : " + item.vmid  + " : move from OVM to LVM ");
			}
			else
			{
				if(OVM.get(i).cpuValue==threshHold)
				{
					BVM.add(new mytrialVM(OVM.get(i).vmid,OVM.get(i).cpuValue));
					OVM.get(i).vmid=-1;
					//OVM.remove(item);
					//Log.printLine(CloudSim.clock() + " : " + item.vmid  + " : move from OVM to BVM ");
				}
			}
		}
		for(int i=0;i<LVM.size();i++)
		{
			if(LVM.get(i).cpuValue>threshHold)
			{
				//OVM.add(LVM.get(i));
				OVM.add(new mytrialVM(LVM.get(i).vmid,LVM.get(i).cpuValue));
				//LVM.remove(item);
				LVM.get(i).vmid = -1;
				//Log.printLine(CloudSim.clock() + " : " + item.vmid  + " : move from LVM to OVM ");
			}
			else
			{
				if(LVM.get(i).cpuValue==threshHold)
				{
					//BVM.add(LVM.get(i));
					BVM.add(new mytrialVM(LVM.get(i).vmid,LVM.get(i).cpuValue));
					LVM.get(i).vmid = -1;
					//LVM.remove(item);
					//Log.printLine(CloudSim.clock() + " : " + item.vmid + " : move from LVM to BVM ");
				}
			}
		}
		for(int i=0;i<BVM.size();i++)
		{
			if(BVM.get(i).cpuValue>threshHold)
			{
				//OVM.add(BVM.get(i));
				OVM.add(new mytrialVM(BVM.get(i).vmid,BVM.get(i).cpuValue));
				BVM.get(i).vmid = -1;
				//BVM.remove(item);
				//Log.printLine(CloudSim.clock() + " : " + item.vmid + " : move from BVM to OVM ");
			}
			else
			{
				if(BVM.get(i).cpuValue<threshHold)
				{
					//LVM.add(BVM.get(i));
					LVM.add(new mytrialVM(BVM.get(i).vmid,BVM.get(i).cpuValue));
					BVM.get(i).vmid=-1;
					//BVM.remove(item);
					//Log.printLine(CloudSim.clock() + " : " + item.vmid  + " : move from BVM to LVM ");
				}
			}
		}
		int in=OVM.size()-1;
		while(in>=0)
		{
			if(OVM.get(in).vmid==-1)
			{
				OVM.remove(in);
			}
			in--;
		}
		in=LVM.size()-1;
		while(in>=0)
		{
			if(LVM.get(in).vmid==-1)
			{
				LVM.remove(in);
			}
			in--;
		}
		in=BVM.size()-1;
		while(in>=0)
		{
			if(BVM.get(in).vmid==-1)
			{
				BVM.remove(in);
			}
			in--;
		}
		//sort LVM in ascending order and OVM in descending order
		Collections.sort(LVM, new Comparator<mytrialVM>() 
		        {
		            @Override
		            public int compare(mytrialVM o1, mytrialVM o2) {
		                return Double.compare(o1.cpuValue, o2.cpuValue);
		            }
		        });
		
		//sort LVM in ascending order and OVM in descending order
		Collections.sort(OVM, new Comparator<mytrialVM>() 
		{
				  @Override
				            public int compare(mytrialVM o1, mytrialVM o2) {
				                return Double.compare(o1.cpuValue, o2.cpuValue);
				            }
	     });
		//reverse them
		if((containsUserFun(OVM,assignedVM.getVM()))&&(!LVM.isEmpty()))
		{
			assignedVM.setVM(LVM.get(0).vmid);
			return cl.getVmId();
		}
		else
		{
			if((containsUserFun(BVM,assignedVM.getVM()))&&(!LVM.isEmpty()))
			{
				assignedVM.setVM(LVM.get(0).vmid);
			}
			else
			{
				if((containsUserFun(OVM,assignedVM.getVM()))&&(LVM.isEmpty()))
				{
					
					assignedVM.setVM(OVM.get(0).vmid);
					//Log.printLine(" assigned: "+assignedVM.getVM());
				}
			}
		}
		return -1;
		//Collections.sort(OVM,Collections.reverseOrder());
		/*if((OVM.contains(assignedVM.getVM()))&&(!LVM.isEmpty()))
		{
			assignedVM.setVM(LVM.get(0));
			return cl.getVmId();
		}
		else
		{
			if(BVM.contains(assignedVM.getVM())&&(!LVM.isEmpty()))
			{
				assignedVM.setVM(LVM.get(0));
			}
			else
			{
				if((OVM.contains(assignedVM.getVM()))&&(LVM.isEmpty()))
				{
					assignedVM.setVM(BVM.get(0));
				}
			}
		}
		return -1;*/
		
	}
	public static int LotteryBasedBalancer(List<Vm> list, Cloudlet cl)
	{
		ArrayList<lotteryTicket> lotteryBasedArray = new ArrayList<lotteryTicket>();
		lotteryBasedArray.clear();
		double  totalCPU = 0.0;
		
		// initialization of everything done
		for(int i=0;i<list.size();i++)
		{
			lotteryBasedArray.add(new lotteryTicket(list.get(i).getTotalUtilizationOfCpu(CloudSim.clock()),list.get(i).getId()));
			totalCPU = totalCPU + list.get(i).getTotalUtilizationOfCpu(CloudSim.clock());
			//Log.printLine(list.get(i).getId() + " : " + "list.get(i).getCurrentAllocatedBw() " + list.get(i).getCurrentAllocatedBw()+ " : list.get(i).getCurrentRequestedBw()" + list.get(i).getCurrentRequestedBw());
			//Log.print(list.get(i).getId() + " : " + list.get(i).getTotalUtilizationOfCpu(CloudSim.clock())+ " ");
		}
		double proportion = 0.0;
		double usefulproportion = 0.0;
		// needs to compute lottery tickets assignment based on totalCPU computation and local CPU value
		if(totalCPU==0.0)
			return -1;
		int totalTickets=0;
		int initial = 0;
		for(int i=0;i<list.size();i++)
		{
				proportion = lotteryBasedArray.get(i).cpuTime/totalCPU;
				usefulproportion = 1-proportion;
				lotteryBasedArray.get(i).numofTickets = (int) ( usefulproportion*10000 );
				initial = totalTickets;
				//Log.print(" proportion: " + proportion + " tickets : " + lotteryBasedArray.get(i).numofTickets);
				totalTickets = totalTickets+ lotteryBasedArray.get(i).numofTickets;
				
				for(int ticket=0; ticket <lotteryBasedArray.get(i).numofTickets;ticket++)
				{
					int temp = ticket+initial;
					lotteryBasedArray.get(i).tickets.add(temp);
				}
		}
		//Log.printLine(" total tickets : " + totalTickets);
		// draw some random ticket based on limit of ticket
		int vmlocalid = randomTickets.getRandom(totalTickets);
		int vmglobalid = 0;
		int tot=0;
		
		for(int i=0;i<lotteryBasedArray.size();i++)
		{
			
			
			if(lotteryBasedArray.get(i).numofTickets+tot>vmlocalid)
			{
				
				vmglobalid=lotteryBasedArray.get(i).vmid;
				break;
			}
			else
			{
				if(lotteryBasedArray.get(i).numofTickets+tot==vmlocalid)
				{
					vmglobalid=lotteryBasedArray.get(i+1).vmid;
					break;
				}
			}
			tot = tot + lotteryBasedArray.get(i).numofTickets;
				
		}
		//Log.printLine(" lottery ticket : " + vmlocalid + " lottery VM : " + vmglobalid);
		// find the VM which has that ticket and set it to assigned VM
		return vmglobalid;
		
	}
	
}

