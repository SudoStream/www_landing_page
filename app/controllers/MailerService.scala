package controllers

import java.io.File
import javax.inject.Inject

import model.User
import org.apache.commons.mail.EmailAttachment
import play.api.libs.mailer._

class MailerService @Inject()(mailerClient: MailerClient) {

  def sendEmail(user: User) = {
    val cid = "1234"
    val email = Email(
      "Welcome to Time to TEACH",
      from = "Time to TEACH Team <team@timetoteach.zone>",
      Seq(s"<${user.email}>"),
      bodyHtml = Some(
        s"""<html><body>
           |<p>Hi ${user.firstName}</p>
           |<p>Welcome to the <b>Time to TEACH</b> community!</p></body></html>""".stripMargin),
      replyTo = Seq("Time to TEACH Team <team@timetoteach.zone>")
    )
    mailerClient.send(email)
  }


}
