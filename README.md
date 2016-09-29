# HONEY BEE LOAD BALANCING
**Honey Bee Load Balancer - Class project for Intelligent Agents where we are comparing Biologically inspired Honey Bee Algorithm with FCFS Algorithm. We are also comparing results with or without lottery based system. We will include graphs of our result once we are done with data collection. Different Scenaiors which we will be examining are : **
a) Simple FCFS
b) Honey Bee Load Balancer
c) Lottery system without Honey Bee
d) Lottery system with Honey Bee

We have divided our test cases into following : 
a) Increasing number of VM's
b) Burst Traffic ( i.e sending all cloudlets in short span of time)
c) General distribution of Cloudlets
d) Increasing Number of CPU's on VM
e) Identical Cloudlets
f) Identical resources

For result metric, we will be comparing : 
a) total CPU time spent by all cloudlets in different cases and algorithm
b) Approximate degree of imbalanced or overloaded state vs Number of Submitted tasks ( Cloudlets)
c) Make Span time Vs Number of Submitted tasks ( Cloudlets)

###Reference
1. Tom Guerout, Thierry Monteil, Georges Da Costa, Rodrigo N. Calheiros, Rajkumar Buyya, Mihai Alexandru. Energy-aware simulation with DVFS. Simulation Modelling Practice and Theory, Volume 39, pages 76-91, December 2013
2. https://github.com/Cloudslab/cloudsim/