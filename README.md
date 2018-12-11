# A simple push notification server

Prerequisites:
* Maven > 3
* Java 1.8
* Firebase account

How to install this:

* Follow instructions from https://golb.hplar.ch/2018/01/Sending-Web-push-messages-from-Spring-Boot-to-Browsers.html#firebase
* You need to update the applications.properties path with your own file path
* In the index.js and sw.js replace the ids/keys as needed
* Run mvn spring-boot:run
* If everything is ok, open your browser and access http://127.0.0.1:8080/index.html
* Enjoy
