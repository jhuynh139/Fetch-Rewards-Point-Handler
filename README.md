Running The Web Service

1. Download and install Java Development Kit 15 from
	https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html

2. Add an environment variable on your system named JAVA_HOME and set it to
	the home directory of your JDK (usually within C:\Program Files\Java\jdk-15)

3. Download apache-tomcat-8.5.78 from https://tomcat.apache.org/download-80.cgi and unzip.

4. Download the FetchRewardsPointHandler.war file and move it to the apache-tomcat-8.5.78\webapps directory.

5. Open the apache-tomcat-8.5.78/bin folder

6. Run the startup.bat file.

7. The web service is up and running!
	7.1. You can go to localhost:8080/FetchRewardsPointHandler in your browser
		to use the HTML file to send HTTP requests to the web service.

	7.2. You can also use something like Postman to send HTTP requests instead.
		The web service serves GET requests on localhost:8080/FetchRewardsPointHandler/display
		to show running point totals and serves POST requests on localhost:8080/FetchRewardsPointHandler/add 
		and localhost:8080/FetchRewardsPointHandler/spend for adding transactions and spending points,
		respectively. Send the form data within the HTTP request body in the x-www-form-urlencoded
		form. 

	7.3. localhost:8080/FetchRewardsPointHandler/add takes three parameters, "payer", "points" and "timestamp"

	7.4. localhost:8080/FetchRewardsPointHandler/spend takes one parameter, "points"

8. You can get Postman for free from https://www.postman.com/downloads/, or even use the browser version
	8.1. To send HTTP requests on the application, select File > New > HTTP Request
	
	8.2. Input the respective URLs listed above in 5.2 in the "Enter request URL" box
		and click on the drop-down next to it to pick either GET or POST depending 
		on which request/function you'd like for both

	8.3. If you picked to send a GET request to localhost:8080/FetchRewardsPointHandler/display, you can go ahead and
		click "SEND" to show the totals

	8.4. If you picked one of the POST methods, make sure to include the form parameters mentioned above
		under the "Body" tab in "x-www-form-urlencoded" form by clicking the radio button and entering the
		relevant information.

:)
