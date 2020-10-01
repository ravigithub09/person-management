# person-management
This repository is used to manage the person information

# Application Configuration and Build Process in Eclipse
<b>Step 1:</b> Clone the repository in to eclipse. Right click on the project and select <b>Configure</b> -> <b>Convert to Maven Project</b><br>
<b>Step 2:</b> Right click on <b>pom.xml</b> -> <b>Run Configurations</b><br>
<b>Step 3:</b> Provide the Name as you want <b>(Eg. person-management)</b><br>
<b>Step 4:</b> Select the Base Directory of person-management application by clicking on Workspace button<br>
<b>Step 5:</b> Provide goals: clean install<br>
<b>Step 6:</b> Click on Apply -> Run<br>

# Launch the application in Eclipse
<b>Step 1:</b> Right click on the <b>PersonApplication.java</b> file -> <b>Run as</b> -> <b>Java Application</b>.<br>
<b>Step 2:</b> Spring Boot application will start with embedded tomcat server.<br>

After successful build, person-management-0.0.1-SNAPSHOT.jar will be created in target folder.
# Build and Run through command line
<b>Step 1:</b> Once you clone the project, go to the project directory in the command prompt. <b>Eg. cd c:\person-management</b><br>
<b>Step 2:</b> Execute the command <b>mvn clean install</b><br>
<b>Step 3:</b> After successful build, person-management-0.0.1-SNAPSHOT.jar will be created in target folder.<br>
<b>Step 4:</b> Change the directory to target folder and then execute the command <b>java -jar person-management-0.0.1-SNAPSHOT.jar</b>  

#### You can launch the application by using the URL (http://localhost:8081/Person/)

# Test Cases Execution
During application build all test cases will be executed.<br>
In order to execute them manually, you need to select and right click on <b>PersonRestControllerTest</b> -> <b>Run as</b> -> <b>JUnit Test</b>
