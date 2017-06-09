# gsheets-to-polarion-xunit

This is an auxiliary tool for Devstudio QE team to make our life and misery with Polarion a bit easier.

## How to run?
1) Enable google sheets API in Google docs & obtain yoursef an client_secret.json file and put it into src/main/resources [1]
2) run `mvn exec:java -Dsheet.id=<your_google_sheet_id>`
3) You are done! Desired XML should be written to standard output.

Note:
This is first version which expects google sheet in some specific format. If needed, we can make it more configurable.


[1] How to get it? See "Step 1" here: https://developers.google.com/sheets/api/quickstart/java
