package controllers

import javax.inject._

import model.User
import play.api._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, mailerService: MailerService) extends AbstractController(cc) {

  private val postUrl = routes.HomeController.thankyou()
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def signup() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signup(postUrl))
  }

  def thankyou() = Action { implicit request: Request[AnyContent] =>
    val user = User(
      firstName = request.body.asFormUrlEncoded.get("firstName").head,

      surname = if (request.body.asFormUrlEncoded.get("surname").head.isEmpty) None
      else Some(request.body.asFormUrlEncoded.get("surname").head),

      email = request.body.asFormUrlEncoded.get("email").head,

      localAuthority = if (request.body.asFormUrlEncoded.get("localAuthority").head.isEmpty) None
      else Some(request.body.asFormUrlEncoded.get("localAuthority").head)
    )

    mailerService.sendEmail(user)
    Ok(views.html.thank_you_for_signing_up())
  }


}
