package dao

import java.util.UUID
import javax.inject.Inject

import com.google.inject.ImplementedBy
import model.User
import org.mongodb.scala.{Completed, Document, MongoCollection}
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

@ImplementedBy(classOf[MongoDbUserWriterDao])
trait UserWriterDao {

  def insertUser(userToInsert: User): Future[Completed]

}

class MongoDbUserWriterDao @Inject()(mongoDbConnectionWrapper: MongoDbConnectionWrapper) extends UserWriterDao {

  val logger = Logger("MongoDbUserWriterDao")
  val usersCollection: MongoCollection[Document] = mongoDbConnectionWrapper.getUsersCollection

  override def insertUser(userToInsert: User): Future[Completed] = {
    println(s"Inserting User to Database: ${userToInsert.toString}")

    val userToInsertAsDocument = convertUserToDocument(userToInsert)
    val observable = usersCollection.insertOne(userToInsertAsDocument)
    val insertCompleted = observable.toFuture()

    insertCompleted.onComplete {
      case Success(completed) =>
        logger.info(s"Successfully inserted user ${userToInsert.firstName}:${userToInsert.email}")
      case Failure(t) =>
        val errorMsg = s"Failed to inserted user ${userToInsert.firstName}:${userToInsert.email}" +
          s" with error ${t.getMessage}. Full stack trace .... \n${t.printStackTrace()}"
        logger.error(errorMsg)
    }

    insertCompleted
  }

  private[dao] def convertUserToDocument(userToConvert: User): Document = {
    Document(
      "_id" -> UUID.randomUUID().toString,
      "firstName" -> userToConvert.firstName,
      "surname" -> userToConvert.surname.getOrElse(""),
      "position" -> userToConvert.positionType.getOrElse(""),
      "email" -> userToConvert.email,
      "localAuthority" -> userToConvert.localAuthority.getOrElse("")
    )
  }

}
