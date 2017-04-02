# METEORITES README #

## What is this repository for? ##

* This README is best viewed online to take advantage of the markdown formatting

* This README includes instructions on how to set-up and get the prototype 
	version of METEORITES running.
	 
* Version: Alpha

## How do I get set up? ##

* System Requirements
> In order to get METEORITES to run properly, please have the following installed on your system:
> Latest Java JDK 8
> Eclipse IDE 4.4 or greater with the e(fx)clipse plugin

* Eclipse Configuration
> To configure your Eclipse IDE correctly to run the game please do the following:
> Navigate to: Window (tab) > Preferences > Java > Installed JRES
> Select Add > 'Standard VM' > Next, then find the installation Directory of your JDK 8 > Finish
> Select the JDK you just added to make it your default > Apply, do not press OK yet
> Navigate to: Java > Compiler > and set 'Compiler compliance level' to 1.8 > Apply > OK

* Setting up METEORITES in Eclipse 
> Go to: https://bitbucket.org/cs302group27/uoa-cs302-2017-group27/downloads/?tab=tags
> Note: You must have access to this repository :)
> Download the commit tagged as "PROJECT-A-PROTOTYPE" and extract the folder to your choice of location
> Open Eclipse
> Go to: File > Import > General > Existing Projects into Workspace > Next
> Select root directory > Browse for the extracted project folder from your chosen location
> Tick the project to select > Finish

## How do I play? ##

* Follow the section "Setting up METEORITES in Eclipse" before coming to this section

* For this version, we only have two players on the two left hand corners of the playing area. AI is in the process of being implemented, so it is not included in this version.

* A very crude bat-ball collision mechanism is in place, it is still very buggy. 

* To run the game
> In the 'Project Explorer' open warlords > src > application > Game.java
> Press Run  
> Player 1 (Bottom-left) can control their bat with the [A] and [D] keys.
> Player 2 (Top-left) can control their bat with the [LEFT] and [RIGHT] arrow keys.

## How do I run JUnit tests? ##

<<<<<<< HEAD
* Follow the section "Setting up METEORITES in Eclipse" before coming to this section
* Make sure you have JUnit set up to use, if you're unsure, follow this:
> Right click the project in 'Project Explorer'
> Navigate to Properties > Java Build Path > 'Libraries' tab
> If JUnit is under the list of libraries then you are fine to proceed
> If not: Add Library > JUnit > Next > library version JUnit 4 > Finish
=======
* For this version, we only have two players on the bottom two corners of the
  playing area. AI is in the process of being implemented, so it is not included
  in this version.
  
* Player 1 can control their bat with the [A] and [D] keys.
* Player 2 can control their bat with the [LEFT] and [RIGHT] arrow keys.
>>>>>>> refs/heads/master

* To run the JUnit tests
> In the 'Project Explorer', open warlords > src > com.ttcj.testing
> Right click the package com.ttcj.testing > Run As > JUnit Test

## Who can I talk to? ##

* Please feel free to contact us if you have any questions regarding our game / README, thanks.

<<<<<<< HEAD
* Keison Tang
> ktan928@aucklanduni.ac.nz

* Chanokpol Janveerawat
> cjan957@aucklanduni.ac.nz

## Credits ##

* In-game background image - Free to use
> http://more-sky.com/WDF-326448.html

* Asteroid Sprite - Used under CC License
> Credit to Author: 'phaelax'
> http://opengameart.org/content/asteroids
=======
C.J
cjan957@aucklanduni.ac.nz
>>>>>>> refs/heads/master
