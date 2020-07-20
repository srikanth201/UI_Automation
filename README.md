# UI_Automation
Automated UI scenarios which was provided for test
•	Created a Maven Project
•	Added Selenium, testing, Mavensurefireplugin, Jsoup, IO, Log4j and extentreports jar dependencies in POM.XML file 
•	Created a data. Properties file to capture the which type of browser and URL and will drive that file using create an object for properties file and via loading
•	Created a MyStore_Class to capture all the Objects in POM style
•	Initiated a constructor to take current driver every time
•	Created multiple methods to utilize the created objects , which would use these methods in test cases
•	Synchronization class will use for explicit wait conditions
•	Base_Class is to load properties file and initiating the sepicific browsers
•	Log4j.xml contains standard template of log4j and would use in logs
•	Listeners class contains predefined inbuilt annotations to execute prior and after the tests, imcluded reporting stuff
•	Mystore_test class contains mentioned scenarios test cases as per the priority wise
