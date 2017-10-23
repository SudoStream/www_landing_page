package controllers

import javax.inject.Inject

import dao.{MongoDbUserWriterDao, UserWriterDao}
import model.User
import play.api.libs.mailer._

class MailerService @Inject()(mailerClient: MailerClient, userWriterDao: UserWriterDao) {

  def sendEmail(user: User) = {
    userWriterDao.insertUser(user)

    val emailToTimeToTeachTeam = Email(
      "New person registered interest to Time to TEACH",
      from = "Time to TEACH Team <team@timetoteach.zone>",
      Seq(s"<andy@timetoteach.zone>"),
      bodyHtml = Some(
        s"""<html><body>
           |<p>Hi</p>
           |<p>New user has registered</p>
           |<br>
           |<p>Firstname: ${user.firstName}</p>
           |<p>Surname: ${user.surname.getOrElse("")}</p>
           |<p>Email: ${user.email}</p>
           |<p>Local Authority: ${user.localAuthority.getOrElse("")}</p>
           |<p>Position Type: ${user.positionType.getOrElse("")}</p>
           |""".stripMargin),
      replyTo = Seq("Time to TEACH Team <team@timetoteach.zone>")
    )
    mailerClient.send(emailToTimeToTeachTeam)

    val email = Email(
      "Welcome to Time to TEACH",
      from = "Time to TEACH Team <team@timetoteach.zone>",
      Seq(s"<${user.email}>"),
      bodyHtml = Some(
        s"""<html><body>
           |<p>Hi ${user.firstName}</p>
           |<p>Welcome to the <b>Time to TEACH</b> community!</p></body></html>
           |<p>
           |We are very excited .... TODO TODO
           |</p>
           |""".stripMargin),
      replyTo = Seq("Time to TEACH Team <team@timetoteach.zone>")
    )
    mailerClient.send(email)

  }


}
