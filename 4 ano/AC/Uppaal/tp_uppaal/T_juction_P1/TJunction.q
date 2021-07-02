//This file was generated from (Commercial) UPPAAL 4.0.15 rev. CB6BB307F6F681CB, November 2019

/*
Reachability: (1) the minor-road light can go green; 
*/
E<> Minor_Road_Sem.Green

/*
Reachability: (2) the major-road light can go red.
*/
E<> Major_Road_Sem.Red

/*
Reachability: (3) Eventually cars will arrive
*/
E<> Sensor.Occupied 

/*
Rechability: (4)
*/
 E<> Sensor.Innactive 

/*
Safety: (1) the system never enters in a deadlock state; 
*/
A[] not deadlock

/*
Safety: (2) the minor-road and major-road lights cannot be green at the same time.
*/
A[] not(Minor_Road_Sem.Green &&  Major_Road_Sem.Green)

/*
Safety: (3) the minor-road and major-road lights cannot be Yellow at the same time.
*/
A[] not(Minor_Road_Sem.Yellow &&  Major_Road_Sem.Yellow)

/*
Safety: (4) Whenever the sensor allows cars to pass, the major road traffic lights are red
*/
A[] Sensor.Innactive imply Major_Road_Sem.Red

/*
Safety: (5) Rafa Haterismos
*/
A[] Sensor.Occupied imply  not (Minor_Road_Sem.Green)

/*
Liveness: (1) If there are cars waiting they will eventually have green light.
*/
Sensor.Occupied --> Sensor.Innactive

/*
Liveness: (2) - the minor-road and major-road lights cannot be red at the same time.
*/
A[] not(Minor_Road_Sem.Red &&  Major_Road_Sem.Red)

/*
Liveness: (3)
*/
Sensor.Innactive --> Minor_Road_Sem.Red

/*
--- Bottom Line ---
*/
//NO_QUERY
