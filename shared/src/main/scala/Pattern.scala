package scoute

sealed trait PatternList {
  type LocBuilder[+L]
  type LinkBuilder

  def apply[L](strings: Seq[String], f: LocBuilder[L]): Option[L]
  def buildLink(res: String, divider: String): LinkBuilder
  val length: Int
}
sealed trait Pattern extends PatternList {
  type Head
  type Tail <: PatternList
  val head: Head
  val tail: Tail

  val length = tail.length + 1
}
sealed trait PatternOf[H,T <: PatternList] extends Pattern {
  type Head = H
  type Tail = T
}
case class PatternCodec[H,T <: PatternList](head: Codec[H], tail: T) extends PatternOf[Codec[H],T] {
  type LocBuilder[+L] = H => tail.LocBuilder[L]
  type LinkBuilder = H => tail.LinkBuilder

  def /::(str: String) = PatternString(str, this)
  def /::[A](codec: Codec[A]) = PatternCodec(codec, this)
  def >>[L](handler: LocBuilder[L]) = {
    val res = RouteOf(this, handler)
    res
  }

  def apply[L](strings: Seq[String], handler: LocBuilder[L]) = head(strings.head) match {
    case Some(value) => tail(strings.tail, handler(value))
    case None => None
  }

  def buildLink(res: String, divider: String) = (value: H) => tail.buildLink(res + divider + head.unapply(value), "/")
}
case class PatternString[T <: PatternList](head: String, tail: T) extends PatternOf[String,T] {
  type LocBuilder[+L] = tail.LocBuilder[L]
  type LinkBuilder = T#LinkBuilder

  def /::(str: String) = PatternString(str, this)
  def /::[A](codec: Codec[A]) = PatternCodec(codec, this)
  def >>[L](handler: LocBuilder[L]) = {
    val res = RouteOf(this, handler)
    res
  }

  def apply[L](strings: Seq[String], handler: LocBuilder[L]) = if(strings.head == head) tail(strings.tail,handler) else None
  def buildLink(res: String, divider: String) = tail.buildLink(res + divider + head, "/")
}
trait PNil extends PatternList {
  type LocBuilder[+L] = L
  type LinkBuilder = String

  val length = 0
  def apply[L](strings: Seq[String], handler: LocBuilder[L]) = Some(handler)
  def buildLink(res: String, divider: String) = res
}
object PNil extends PNil

trait Route[+L] {
  type Pattern <: scoute.Pattern
  val pattern: Pattern
  protected val handler: Pattern#LocBuilder[L]

  def apply(strings: Seq[String]) = pattern(strings, handler.asInstanceOf[pattern.LocBuilder[L]])
}
case class RouteOf[P <: Pattern,+L](val pattern: P, protected val handler: P#LocBuilder[L]) extends Route[L] {
  type Pattern = P
}