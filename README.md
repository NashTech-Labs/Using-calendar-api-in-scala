# Calendar-API in Scala

The Google Calendar API is a RESTful API that can be accessed through explicit HTTP calls or via the Google Client Libraries. The API exposes most of the features available in the Google Calendar Web interface.

This is a template code to use the Google Calendar API in scala. 

## You will be able to 
- Create Events in Google Calendar.
- Get Calendar List.
- Set reminder and occurence of events.

## Setup
- Clone this Repo.
- Replace `<GCP Project Name>` with your own GCP project name.
- Store the credentials in `credentials.json` file in `resources` folder.
- Replace `<calendar email>` with your own Google email.

## Build and Run
- Open terminal in this directory.
- Run `sbt run`
- Enter login details and Bingo! your event is created in your calendar.
