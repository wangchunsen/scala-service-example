//package example.service.repo
//
//import java.util.Date
//
//import com.mongodb.ConnectionString
//import org.bson.{BsonReader, BsonWriter}
//import org.bson.codecs.{BsonDocumentCodec, Codec, DecoderContext, EncoderContext}
//import org.bson.codecs.configuration.{CodecProvider, CodecRegistries, CodecRegistry}
//import org.mongodb.scala.{MongoClient, MongoClientSettings, MongoCollection}
//import org.mongodb.scala.bson.{
//  BsonArray,
//  BsonBoolean,
//  BsonDecimal128,
//  BsonDocument,
//  BsonNull,
//  BsonNumber,
//  BsonString,
//  BsonTransformer,
//  BsonValue,
//  ObjectId
//}
//import org.mongodb.scala.bson.conversions.Bson
//import org.mongodb.scala.model.{Filters, ReplaceOptions}
//import play.api.libs.json.{JsArray, JsBoolean, JsNull, JsNumber, JsObject, JsString, JsValue, OWrites, Reads}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//import scala.reflect.ClassTag
//
//trait MongoRepo[V, F[_]] extends Repository[V, ObjectId, Bson, F] {
//  val collection: MongoCollection[Row]
//
//  override protected def newId(): ObjectId = Left(new ObjectId())
//
//  override def query(query: Bson): F[Seq[Row]] =
//    collection.find(query).toFuture()
//
//  override def getById(id: ObjectId): F[Option[Row]] =
//    collection.find(Filters.eq("_id", id)).first().toFutureOption()
//
//  override def save(row: Row): F[Boolean] = {
//    collection
//      .replaceOne(
//        filter = Filters.and(
//          Filters.eq("_id", row.id),
//          Filters.eq("_version", row.version - 1)
//        ),
//        replacement = row,
//        options = ReplaceOptions().upsert(true)
//      )
//      .toFuture()
//      .map { result =>
//        result.getModifiedCount == 1
//      }
//  }
//
//  override def delete(id: ObjectId): F[Boolean] =
//    collection.deleteOne(filter = Filters.eq("_id", id)).toFuture().map(_.getDeletedCount == 1)
//}
//
//object MongoRepoImp extends DbImp {
//  implicit class COps[A](val collection: MongoCollection[A]) extends AnyVal {
//    def :++(providers: CodecProvider*): MongoCollection[A] =
//      collection.withCodecRegistry(
//        CodecRegistries.fromRegistries(
//          collection.codecRegistry,
//          CodecRegistries.fromProviders(providers: _*)
//        )
//      )
//  }
//
//  override type ID = ObjectId
//  override type Query = Bson
//
//  private val database = {
//    val mongoClient = MongoClient(
//      MongoClientSettings.builder().applyConnectionString(new ConnectionString("mongodb://localhost")).build()
//    )
//    mongoClient.getDatabase("test")
//  }
//
//  override def getRepo[M: ClassTag: Reads: OWrites](tableName: String): R[M] =
//    new MongoRepo[M] {
//      override val collection: MongoCollection[Row] =
//        database.getCollection[Row](tableName) :++ CodecRepo.dbObjProvider[M]
//    }
//}
//object CodecRepo {
//  def dbObjProvider[V: ClassTag: Reads: OWrites]: CodecProvider = new CodecProvider {
//    override def get[T](clazz: Class[T], registry: CodecRegistry): Codec[T] = {
//      if (clazz.isAssignableFrom(classOf[DBObj[_, _]])) dbObj[V](registry).asInstanceOf[Codec[T]]
//      else null
//    }
//  }
//
//  def jsonToDocument(jsObject: JsObject): BsonDocument =
//    BsonDocument(jsObject.value.map { t =>
//      val (key, value) = t
//      key -> jsToValue(value)
//    })
//
//  import scala.collection.convert.ImplicitConversionsToScala._
//  def documentToJson(document: BsonDocument): JsObject =
//    JsObject(document.toMap.map { t =>
//      val (key, value) = t
//      key -> valueToJs(value)
//    })
//
//  import scala.collection.convert.ImplicitConversionsToScala._
//
//  def valueToJs(value: BsonValue): JsValue = value match {
//    case _: BsonNull     => JsNull
//    case b: BsonArray    => JsArray(b.getValues.map(valueToJs))
//    case b: BsonDocument => documentToJson(b)
//    case b: BsonNumber   => JsNumber(b.decimal128Value().bigDecimalValue())
//    case b: BsonString   => JsString(b.getValue)
//    case b: BsonBoolean  => JsBoolean(b.getValue)
//  }
//
//  def jsToValue(jsValue: JsValue): BsonValue = jsValue match {
//    case JsString(str)     => BsonString(str)
//    case JsBoolean(b)      => BsonBoolean(b)
//    case JsNull            => BsonNull()
//    case JsArray(values)   => BsonArray(values.map(jsToValue))
//    case jo: JsObject      => jsonToDocument(jo)
//    case JsNumber(numeric) => BsonDecimal128(numeric)
//  }
//
//  def dbObj[V](registry: CodecRegistry)(implicit tag: ClassTag[V], read: Reads[V], write: OWrites[V]): Codec[DBObj[V, ObjectId]] =
//    new Codec[DBObj[V, ObjectId]] {
//      val documentCodec = new BsonDocumentCodec()
//      val clz: Class[V] = implicitly[ClassTag[V]].runtimeClass.asInstanceOf[Class[V]]
//
//      override def decode(reader: BsonReader, decoderContext: DecoderContext): DBObj[V, ObjectId] = {
//        val document = documentCodec.decode(reader, decoderContext)
//        val id = document.getObjectId("_id").getValue
//        val version = document.getInt64("_version").getValue
//        val updatedAt = new Date(document.getDateTime("_updatedAt").getValue)
//
//        val valueDocument = BsonDocument(
//          document.entrySet().filterNot(_.getKey.startsWith("_")).map(e => e.getKey -> e.getValue)
//        )
//
//        DBObj(
//          version = version,
//          id = id,
//          value = read.reads(documentToJson(valueDocument)).get,
//          updatedAt = updatedAt
//        )
//      }
//
//      override def encode(writer: BsonWriter, value: DBObj[V, ObjectId], encoderContext: EncoderContext): Unit = {
//        val valueDocument = jsonToDocument(write.writes(value.value))
//        val docElements: Seq[(String, BsonValue)] = Seq[(String, BsonValue)](
//          "_id" -> value.id,
//          "_version" -> value.version,
//          "_updatedAt" -> value.updatedAt
//        ) ++ valueDocument.entrySet().map(t => t.getKey -> t.getValue)
//
//        documentCodec.encode(writer, BsonDocument(docElements), encoderContext)
//      }
//
//      override def getEncoderClass: Class[DBObj[V, ObjectId]] = classOf[DBObj[V, ObjectId]]
//
//      implicit private def toBson[T](value: T)(implicit transformer: BsonTransformer[T]): BsonValue =
//        transformer.apply(value)
//    }
//}
