# gsheets-to-polarion-xunit

This is an auxiliary tool for Devstudio QE team to make our life and misery with Polarion a bit easier.

## How to run?
1) Enable google sheets API in Google docs & obtain yoursef an client_secret.json file and put it into src/main/resources [1]

2) Run 
`mvn exec:java -Dsheet.id=<your_google_sheet_id> -Dtestrun.id=<test_run_id>  -Did.column.char=<char> -Dverdict.column.char=<char> -Dcomment.column.char=<char>`

Chars can be in upper or lower case. There is some basic check in code for validity.

3) You are done! Desired XML should be written to standard output.

## How to import to polarion?
Once you have xunit xml file generated you can import them into polarion:
```
curl -k -u "<username>:<password>" -X POST -F file=@/path/to/xunit.xml <polarion-instance-url>/import/xunit
```
You should get response like:
```
<html>
<head>
<title>Polarion xUnit Importer</title>
</head>
<body bgcolor="white">
<p>The following files are being imported:</p>
<ul>
<li>xunit.xml</li>
</ul>
</body>
</html>
```
If you want to see the result of your import, you can subscribe to log. How to do that is described in this mojo page: https://mojo.redhat.com/docs/DOC-1073077

Note:
This is first version which expects google sheet in some specific format. If needed, we can make it more configurable.


[1] How to get it? See "Step 1" here: https://developers.google.com/sheets/api/quickstart/java
