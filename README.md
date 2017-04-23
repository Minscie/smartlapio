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

Custom sensor building attempt (failed so far, may be used later)



##Web app

Is designed for our imaginary slave master. The idea is that he can track his slave's activity and get data in readable form, to make desicions if these particular slaves can be feed today or not.


##Android app

Is designed for the the customer, or some people call them slaves. With an android device customer can see hes/r own progress and post them to the database for the master.



##Arduino code

Arduino uses serial line at 9600 baud rate to send messages over bluetooth to the android app. Android app has to be paired with the arduino's bluetooth module.
In this project we used bluetooth module HC-06.
