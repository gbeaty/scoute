package scoute

trait Codec[A] {
  type AddFunc[F] = A => F
  def apply(str: String): Option[A]
  def unapply(value: A) = value.toString
}
class CatchingCodec[A](f: String => A) extends Codec[A] {
  def apply(str: String) = try {
    Some(f(str))
  } catch {
    case e: Exception => None
  }
}
case object StringCodec extends Codec[String] {
  def apply(str: String) = if(str.length > 0) Some(str) else None
}
case object BooleanCodec extends Codec[Boolean] {
  def apply(str: String) =
    if(str == "0") Some(false)
    else if(str == "1") Some(true)
    else None

  override def unapply(value: Boolean) = if(value) "1" else "0"
}
case object ByteCodec extends CatchingCodec[Byte](_.toByte)
case object ShortCodec extends CatchingCodec[Short](_.toShort)
case object IntCodec extends CatchingCodec[Int](_.toInt)
case object LongCodec extends CatchingCodec[Long](_.toLong)
case object FloatCodec extends CatchingCodec[Float](_.toFloat)
case object DoubleCodec extends CatchingCodec[Double](_.toDouble)