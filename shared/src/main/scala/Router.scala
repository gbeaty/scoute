package scoute

trait Router {
  @inline def byte = ByteCodec
  @inline def short = ShortCodec
  @inline def int = IntCodec
  @inline def long = LongCodec
  @inline def float = FloatCodec
  @inline def double = DoubleCodec
  @inline def string = StringCodec
  @inline def boolean = BooleanCodec

  val prefix: String

  implicit def stringToPatternString(str: String) = PatternString(str,PNil)
  implicit def extractorToPatternCodec[A](ext: Codec[A]) = PatternCodec(ext,PNil)

  def link[P <: Pattern](route: RouteOf[P,_]) = route.pattern.buildLink("",prefix)
}