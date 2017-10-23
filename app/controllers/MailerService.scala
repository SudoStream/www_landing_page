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
      Seq(s"<team@timetoteach.zone>"),
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
           |<p>Welcome to the <b>Time to TEACH</b> community! ...we are very excited to have you on board! </p></body></html>
           |<p>
           | Let us first introduce ourselves...we are a very small team of 2! - Andy is our IT expert with over 15 years
           | experience, and Yvonne is a primary teacher. We are passionate about helping to reduce the stress of
           | teachers' workload.
           |</p>
           |<p>
           | Our aim is to provide teachers in Scotland with a planning tool which will save them time by automating
           |  many of their planning tasks. We are doing this by designing a tool which will have the curriculum for
           |  excellence embedded within the design. It will use state of the art technology to ensure teachers are
           |  getting the best product possible.  We recognise that every teacher, school, local authority plan
           |  differently.... which is why we want your ideas!
           |</p>
           |<p>
           | Obviously we know how busy you are, so we will not expect a lot of your time. We will be sending you
           | links to our webpage where you will be able to explore the site in your own time and give us feedback
           | on how user-friendly you find it, the layout of the design, the functionality and any functions you
           | would like added.
           |</p>
           |<p>
           | We will be back in touch soon! In the meantime if you have any questions or ideas please do get in touch.
           |</p>
           |<p>Many thanks,</p>
           |<p>Andy and Yvonne</p>
           |""".stripMargin),
      replyTo = Seq("Time to TEACH Team <team@timetoteach.zone>")
    )
    mailerClient.send(email)

  }


}
