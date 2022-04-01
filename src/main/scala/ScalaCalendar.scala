import com.google.api.client.auth.oauth2.Credential

import scala.collection.JavaConverters._
import java.io.{FileNotFoundException, IOException, InputStreamReader}
import java.time.Duration
import java.util.{Collections, Date, GregorianCalendar}
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import com.google.api.services.calendar.model.EventReminder
import com.google.api.services.calendar.model.EventAttendee

object ScalaCalendar extends App{
  val APPLICATION_NAME = "<GCP Project Name>"

  val JSON_FACTORY = GsonFactory.getDefaultInstance

  val TOKENS_DIRECTORY_PATH = "tokens"

  val SCOPES = Collections.singletonList(CalendarScopes.CALENDAR)
  val CREDENTIALS_FILE_PATH = "/credentials.json"

  @throws[IOException]
  def getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential = {
    val in = getClass.getResourceAsStream(CREDENTIALS_FILE_PATH)
    if (in == null) throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH)
    val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in))

    val flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))).setAccessType("offline").build
    val receiver = new LocalServerReceiver.Builder().setPort(8080).build
    val credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    credential
  }

  def bookCalendar(startDate: Date, calendarId: String, eventName: String): Unit = {
    val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport
    val service = new calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build
    var event = new Event().setSummary(eventName).setDescription("This is an event created by Google Calendar API in scala.")
    val endDate = Date.from(startDate.toInstant.plus(Duration.ofMinutes(45)))

    val startDateTime = new DateTime(startDate)
    val start = new EventDateTime().setDateTime(startDateTime)
    event.setStart(start)

    val endDateTime = new DateTime(endDate)
    val end = new EventDateTime().setDateTime(endDateTime)
    event.setEnd(end)

    val attendees = Array[EventAttendee](new EventAttendee().setEmail("someone@example.com"), new EventAttendee().setEmail("other@example.com"))
    event.setAttendees(attendees.toList.asJava)

    val reminderOverrides = Array[EventReminder](new EventReminder().setMethod("email").setMinutes(24 * 60), new EventReminder().setMethod("popup").setMinutes(10))
    val reminders = new Event.Reminders().setUseDefault(false).setOverrides(reminderOverrides.toList.asJava)
    event.setReminders(reminders)

    event = service.events().insert(calendarId, event).execute
  }
  val eventDate = new GregorianCalendar(2022, 2, 7, 15, 15).getTime

  bookCalendar(eventDate,"<calendar email>","New Event")
}
