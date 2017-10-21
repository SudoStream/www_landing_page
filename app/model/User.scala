package model

case class User(
               firstName: String,
               surname: Option[String],
               email: String,
               localAuthority: Option[String]
               )
