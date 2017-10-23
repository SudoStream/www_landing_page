package model

case class User(
               firstName: String,
               surname: Option[String],
               email: String,
               positionType: Option[String],
               localAuthority: Option[String]
               )
