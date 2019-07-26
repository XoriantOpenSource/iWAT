# README

**iWAT** (Intelligent Webservice Automation Tool) Open Source 1.2 release


| License:  |Apache License 2.0 |
|       --  |              --   |
 
## About:

iWAT is an Open source tool to automate web services test cases for SOAP and REST APIs. It allows a person who doesn't have any programming expertise and having the good understanding of web services can also automate web services tests using this tool.

To begin with iWAT, first, download and configure the iWAT application on your computer, write your test case in the spreadsheet and place it in the directory to run.
To Run the tests you need to first install and configure the tool correctly. Please find installation and configuration instruction as follows.

## What's New in version 1.2:

 1. GET, POST, PUT and DELETE methods are supported
 2. Support for XPath and JSON path expressions
 3. Test cases and Test suites uses excel format for better handling.
 4. Additional request headers
 5. Basic authentication
 6. OAuth-2 support enabled.
 7. Complex path queries for JSON and XML (simple tests on fields)
 8. Detailed Html Report Generation after execution.
 9. Multiple test cases with multiple data sets are now supported.


 
 
## Prerequisite:

Make sure Java v1.8 and above is Installed in your Computer


## Installation and Configuration Notes:

To begin with iWAT first you import the project to your IDE and take a maven update for the same. For running the application you need to run the main class i.e: iWATEngine.class and please make sure you pass the command line argument to the main class i.e you need to pass the path of the folder where the config file is being stored. Eg: (D:\\Workspace\\iWAT\\Resources).

**OR**

If you have nothing to do with the code then just simply right click the run.bat file and provide the command line argument i.e the path of the folder where the "config.properties" file is being stored. Eg: (D:\\{Workspace}\\iWAT\\Resources).

To Build, please use Maven version 2 or 3 

Once cloned and imported, the project would look as - 
<pre><code>
	${basedir}/ 
		- pom.xml
		- .classpath
		- .project
		- TestResponse/
		  - Response/
		    - Json
		    - XML
		  - TestCase/
		    - Json
		    - XML
		- Test Data/
		- API_Files/
		- Images
		- Logs/
		- TestCases/
		- Resources/
		- .externalToolBuilders
		- .settings
		- src/
		- com/auto/iwat..
		- target/
		- classes/..
		- test-classes/
		- maven-status/
		- generated-sources
</code></pre>

## Testing with iWAT involves 4 main stages:

 1. Configuring the tool (One-time configuration)
 2. Write Test Cases
 3. Running Test
 4. Analyzing Test Results

## Configuration:

To start working with the iWAT you should first configure it by setting config.properties file from Resources folder.
Detailed explanation of configuration parameters are as follows:


##  Parameters of config.properties:

soapUrl =  Application URL against which tests is to be run for SOAP Tests
restUrl =  Application URL against which tests is to be run for REST Tests
soapActionUrl = If you have soapAction Url then fine else leave it blank
workspace = Provide Path of your workspace 

**NOTE- Only add your workspace path in the above variable and no where else.Everywhere else the workspace will automatically will be concatinated.

------------------------------------**To Make Choice**----------------------------

 **Set the Service type you want to run** 
 1 --> To run SOAP Test 
 2 --> To run REST Test
serviceType =2 

#### **Set the test type**
 1 --> To run each test case as separate test cases.
 2 --> To run entire worksheet as a single test case.
testType =1

------------------------------------**For SOAP**------------------------------------
<pre><code>
xPathXml = Here you need to provide the path of excel sheet of soap request which is in Excel_Templates folder, eg: ($WORKSPACE_DIR\iwat\\Excel_Templates\\SOAPTestSuite.xlsx)
actualXml = Here you need to provide the path of your actual XML file which you want to hit request which is in API_Files folder, eg: ($WORKSPACE_DIR\\iwat\\API_Files\\OTA_VehResRQ11.2.xml)
responseXml = Here you need to provide the path of the folder where you would be storing the response, eg: ($WORKSPACE_DIR\\iwat\\TestResponse\\Response\\Xml\\)
tempMandatoryXml = For this provide the same path where the config.properties is being stored. eg: ($WORKSPACE_DIR\\iwat\\Resources\\mandatoryXml.xml)
tempActualXml = This variable would store the test cases,so provide the test case path, eg: ($WORKSPACE_DIR\\iwat\\TestResponse\\TestCase\\Xml\\)
</code></pre>


------------------------------------**For REST**------------------------------------

<pre><code>
jPathExcel = Here you need to provide the path of excell sheet of rest request which is in Excel_Templates folder,eg ($WORKSPACE_DIR\\iwat\\TestCases\\RestTestSuite.xlsx)
oriJsonFilePath = Here you need to provide the path of your actual json file which you want to hit  request which is in API_Files folder,eg ($WORKSPACE_DIR\\iwat\\demoJsonPut.json)
outputJsonFilePath = This variable would store the test cases,so provide the test case path, eg ($WORKSPACE_DIR\\iwat\\TestResponse\\TestCase\\Json\\)
responseJsonFilePath = Here you need to provide the path of the folder where you would be storing the response,eg ($WORKSPACE_DIR\\iwat\\TestResponse\\Response\\Json\\)
tempMandatoryJson =  For this provide the same path where the config.properties is being stored.eg ($WORKSPACE_DIR\\iwat\\Resources\\tempMandatoryJson.json)
</code></pre>

------------------------------------**For Logs**------------------------------------
 <pre><code>
logFileJson = Path for Rest log,eg ($WORKSPACE_DIR\\iwat\\Logs\\JSON\\)
  
logFileXML = Path for Soap log,eg ($WORKSPACE_DIR\\iwat\\Logs\\XML\\)
</code></pre>


## Writing Test Cases in iWAT :

For both SOAP and REST API tests, test cases are written in a spreadsheet and test case are written in a specific format to successfully run the test case. There are separate spreadsheets for soap and rest services.

### REST Test SpreadSheet Parameters:

<pre><code>
1) Test Case Number - Provide your test case number.
2) Test Case Description - Describe your test case here.
3) HttpURLParamater - Please provide your URL header parameter which will be appended to the main URL. Eg: (/api/users/2)
4) ActualJsonFilePath - Please Provide the path of your actual JSON file path which you would want to hit the server. Eg: ($WORKSPACE_DIR\\iWAT\\API_Files\\demoJsonPut.json)
5) IsAuthorizationRequired(If yes provide auth token else leave blank) - Please provide authToken if your webservice is authenticated.
6) JsonPath(**Request**) - Provide the different JSON Paths of the attributes which you want to alter and change and test accordingly. Please make sure you provide a valid JSON Path, you can use chrome extension JSON Pathfinder to validate your JSON Path.Eg ($.name,$.job)
7) Is Executable (**Yes/No**) - As the name suggests enter 'Yes' if you want to execute the test case else enter **'No'**.
8) Test Data File Path - Please provide the path of test data CSV file, this file stores your test data according to your JSON path. Make sure you have a CSV file with valid data in data.you can find a demo CSV file in the test Data folder of the project.
9) Method - Please provide your method type. Eg: (**POST,GET,PUT,DELETE,etc**)
10) Expected Response Code - Please provide a response code which you are expecting in response. Eg: (200,201,400,etc)
11) Actual Response Code - Leave it blank, will be filled by the application after running it.
12) JsonPath(**Response**) - 66666666
13) Response Data - Leave it blank application would fill it after running.
14) Status (**Pass/Fail/NotExecuted**) - Leave it blank application would fill it after running.
15) Response Link -Leave it blank application would fill it after running.
16) Log Link -Leave it blank application would fill it after running.
17) Execution Time (**Seconds**)- Leave it blank application would fill it after running.
</code></pre>

### Soap SpreadSheet Parameters:
<pre><code>
1) Test Case Number - Provide your test case number.
2) Test Case Description - Describe your test case here.
3) Xpath - Please provide a single XPath of the attribute which you want to alter, change and test accordingly.Note -- In the Xpath where there is no namespace Use DEFAULT as the namespace. Eg:(//DEFAULT:Country/DEFAULT:state/DEFAULT:district/@name).
4) Is Executable (**Yes/No**) - As the name suggests enter 'Yes' if you want to execute the test case else enter 'No'.
5) Test Data - Please provide the test data directly in the cell.
6) Method - Please provide your method type. Eg: (POST,GET,PUT,DELETE,etc).
7) Expected Response Code - Please provide a response code which you are expecting in response. Eg: (200,201,400,etc)
8) Actual Response Code - Leave it blank, will be filled by the application after running it.
9) Expected Response Message - Leave it blank, will be filled by the application after running it.
10) Actual Response Message - Leave it blank, will be filled by the application after running it.
11) Status (**Pass/Fail/NotExecuted**) - Leave it blank, will be filled by the application after running it.
12) Execution Time (**Seconds**)- Leave it blank application would fill it after running.
</code></pre>


## Running iWAT:

Run the run.bat file to start the application.


### Analyzing Test Results:

Please analyse the logs in Logs folder. Also, you can check the test response folder to view the stored responses. The excel sheet will also be updated after the successful running of the application 


### Examples:

For details on REST and SOAP test cases creation, please refer screenshots from image folder.
REST_SAMPLE_1.0.png
SOAP_SAMPLE_1.0.png