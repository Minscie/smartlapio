##SMART LAPIO

Smart lapio is a device developed during Metropolia IoT course

##Prerequisite

Shovel
Arduino Nano
Gyroscope/Accelerometer
Bluetooth module
Load Cell, We recommend to use more than 20kg max weight load cells!
Thermistor
Power supply 5V in / 5V out
Android device 4


##Building device

See project documentation from the Project_documentation.pdf

## Custom sensor attempt
One of the main ideas of the project was in measuring weight, lifted with the shovel. In the final version it is done with usage of Load Cell. Price of load cells (and for many other sensors it is also true) varies from a couple to hundreds of euros. In addition it wasn't clear how precise the sensor had to be. Since the price of components has major importance, some kind of custom sensor made with cheap materials would be highly beneficial.
Now one the Internet one can find literally lots of examples of so called DIY. Couple of them looked very promising. They describe how to build very own 'flex sensor" with metal foil, wires, glue and paper.
Particular examples can be found under next links:
http://dev-blog.mimugloves.com/bendflex-sensor-overview/
http://www.instructables.com/id/How-to-Make-Bi-Directional-Flex-Sensors/
https://www.engineersgarage.com/contribution/interfacing-flex-sensor-arduino

We’ve managed to build something close to those sensor in provided examples. However their readings were not so clear. Finally team made a decision not to waste time on further exploration of ‘foil-based-sensors’(this flex-sensor wasn’t actually the only attempt to make some custom additions), and build the device with more appropriate sensors available on the market.

##Web app

Is designed for our imaginary slave master. The idea is that he can track his slave's activity and get data in readable form, to make desicions if these particular slaves can be feed today or not.

currently starting version is available as users.metropolia.fi/~robertst

##Android app

Is designed for the the customer, or some people call them slaves. With an android device customer can see hes/r own progress and post them to the database for the master.


##Arduino code

Arduino uses serial line at 9600 baud rate to send messages over bluetooth to the android app. Android app has to be paired with the arduino's bluetooth module.
In this project we used bluetooth module HC-06.
