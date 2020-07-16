package busymachines.pureharm.internals.rest

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jul 2020
  */
trait PureharmTapirModelAliases {
  type Cookie = sttp.model.Cookie
  val Cookie: sttp.model.Cookie.type = sttp.model.Cookie

  type CookieValueWithMeta = sttp.model.CookieValueWithMeta
  val CookieValueWithMeta: sttp.model.CookieValueWithMeta.type = sttp.model.CookieValueWithMeta

  type CookieWithMeta = sttp.model.CookieWithMeta
  val CookieWithMeta: sttp.model.CookieWithMeta.type = sttp.model.CookieWithMeta

  type HasHeaders = sttp.model.HasHeaders

  type Header = sttp.model.Header
  val Header: sttp.model.Header.type = sttp.model.Header

  type HeaderNames = sttp.model.HeaderNames
  val HeaderNames: sttp.model.HeaderNames.type = sttp.model.HeaderNames

  type Headers = sttp.model.Headers
  val Headers: sttp.model.Headers.type = sttp.model.Headers

  type StatusCode = sttp.model.StatusCode
  val StatusCode: sttp.model.StatusCode.type = sttp.model.StatusCode

  type MediaType = sttp.model.MediaType
  val MediaType: sttp.model.MediaType.type = sttp.model.MediaType

  type Uri = sttp.model.Uri
  val Uri: sttp.model.Uri.type = sttp.model.Uri
}
