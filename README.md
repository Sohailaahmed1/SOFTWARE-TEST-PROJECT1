This repository documents the testing activities performed on the ParaBank website (https://parabank.parasoft.com/parabank/index.htm), a demo online banking application by Parasoft.
Testing Activities

Manual Testing

Conducted exploratory testing to verify core features: login, fund transfers, view transaction history, open new account, and bill payment.
Identified usability and functional issues through manual execution.

API Testing with Postman

Tested RESTful services (e.g., funds transfer, login,....) using Postman.
Validated request/response workflows, status codes, and data integrity.
Wrote JavaScript test scripts for automated API validation.

UI Testing with Selenium WebDriver

Automated UI tests for registration, login, and account management using Selenium WebDriver.
Verified element visibility, navigation, and user interactions.
Ensured consistent UI behavior across scenarios.

Setup

Access the website at https://parabank.parasoft.com/parabank/index.htm.
For local deployment, build the parabank.war using Maven and deploy on Apache Tomcat 9 with Java 17.

Contributors

Testing team members involved in manual, API, and UI testing efforts.


