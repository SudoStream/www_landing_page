package dao

import java.net.URI
import javax.inject.Singleton
import javax.net.ssl.{HostnameVerifier, SSLSession}

import com.google.inject.ImplementedBy
import com.mongodb.connection.ClusterSettings
import com.typesafe.config.ConfigFactory
import org.mongodb.scala.connection.{NettyStreamFactoryFactory, SslSettings}
import org.mongodb.scala.{Document, MongoClient, MongoClientSettings, MongoCollection, MongoDatabase, ServerAddress}

import scala.collection.JavaConverters._

@ImplementedBy(classOf[MongoDbConnectionWrapperImpl])
trait MongoDbConnectionWrapper {

  def getUsersCollection: MongoCollection[Document]

}

sealed class MongoDbConnectionWrapperImpl() extends MongoDbConnectionWrapper {

  private val config = ConfigFactory.load()
  private val mongoDbUriString = config.getString("mongodb.connection_uri")
  private val mongoDbUri = new URI(mongoDbUriString)
  private val usersDatabaseName = config.getString("landing-page.database_name")
  private val usersCollectionName = config.getString("landing-page.users_collection")

  def getUsersCollection: MongoCollection[Document] = {
    def createMongoClient: MongoClient = buildLocalMongoDbClient

    val mongoClient = createMongoClient
    val database: MongoDatabase = mongoClient.getDatabase(usersDatabaseName)
    database.getCollection(usersCollectionName)
  }

  private def buildLocalMongoDbClient = {
    val mongoKeystorePassword = try {
      sys.env("MONGODB_KEYSTORE_PASSWORD")
    } catch {
      case e: Exception => ""
    }

    val mongoDbHost = mongoDbUri.getHost
    val mongoDbPort = mongoDbUri.getPort
    println(s"mongo host = '$mongoDbHost'")
    println(s"mongo port = '$mongoDbPort'")

    val clusterSettings: ClusterSettings = ClusterSettings.builder().hosts(
      List(new ServerAddress(mongoDbHost, mongoDbPort)).asJava).build()

    val mongoSslClientSettings = MongoClientSettings.builder()
      .sslSettings(SslSettings.builder()
        .enabled(true)
        .invalidHostNameAllowed(true)
        .build())
      .streamFactoryFactory(NettyStreamFactoryFactory())
      .clusterSettings(clusterSettings)
      .build()

    MongoClient(mongoSslClientSettings)
  }

}

class AcceptAllHostNameVerifier extends HostnameVerifier {
  override def verify(s: String, sslSession: SSLSession) = true
}

