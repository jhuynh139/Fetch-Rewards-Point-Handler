Running The Web Service

1. Download and install Java Development Kit 15 from
	https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html

2. Add an environment variable on your system named JAVA_HOME and set it to
	the home directory of your JDK (usually within C:\Program Files\Java\jdk-15)

3. Open the apache-tomcat-8.5.78/bin folder

4. Run the startup.bat file.

5. The web service is up and running!
	5.1. You can go to localhost:8080/FetchRewardsPointHandler in your browser
		to use the HTML file to send HTTP requests to the web service.

	5.2. You can also use something like Postman to send HTTP requests instead.
		The web service serves GET requests on localhost:8080/FetchRewardsPointHandler/display
		to show running point totals and serves POST requests on localhost:8080/FetchRewardsPointHandler/add 
		and localhost:8080/FetchRewardsPointHandler/spend for adding transactions and spending points,
		respectively. Send the form data within the HTTP request body in the x-www-form-urlencoded
		form. 

	5.3. localhost:8080/FetchRewardsPointHandler/add takes three parameters, "payer", "points" and "timestamp"

	5.4. localhost:8080/FetchRewardsPointHandler/spend takes one parameter, "points"

6. You can get Postman for free from https://www.postman.com/downloads/, or even use the browser version
	6.1. To send HTTP requests on the application, select File > New > HTTP Request
	
	6.2. Input the respective URLs listed above in 5.2 in the "Enter request URL" box
		and click on the drop-down next to it to pick either GET or POST depending 
		on which request/function you'd like for both

	6.3. If you picked to send a GET request to localhost:8080/FetchRewardsPointHandler/display, you can go ahead and
		click "SEND" to show the totals

	6.4. If you picked one of the POST methods, make sure to include the form parameters mentioned above
		under the "Body" tab in "x-www-form-urlencoded" form by clicking the radio button and entering the
		relevant information.

7. startup.bat unpacks the FetchRewardsPointHandler.war file under apache-tomcat-8.5.78\webapps after deploying, and all of the code is visible
	under apache-tomcat-8.5.78\webapps\FetchRewardsPointHandler\WEB-INF\classes\fetchrewards.


:)
