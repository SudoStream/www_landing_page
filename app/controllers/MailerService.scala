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
      "Simple email",
      "Time to TEACH Team <team@timetoteach.zone>",
      Seq(s"${user.firstName} ${user.surname} <${user.email}>"),
//      attachments = Seq(
//        AttachmentFile("image.jpg", new File(routes.Assets.versioned("images/bluedot.gif").absoluteURL()), contentId = Some(cid))
//      ),
      // sends text, HTML or both...
      bodyText = Some("A text message"),
      bodyHtml = Some(s"""<html><body><p>An <b>html</b> message with cid <img src="cid:$cid"></p></body></html>""")
    )
    mailerClient.send(email)
  }


}
