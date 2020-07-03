package busymachines.pureharm.db.psql

import cats.Show
import org.postgresql.util.PSQLException

object PSQLExceptionDebug {

  def apply(e: PSQLException): String = PSQLExceptionShow.show(e)

  implicit val PSQLExceptionShow: Show[PSQLException] = Show.show { e =>
    val msg = e.getServerErrorMessage
    s"""
       |
       |---------------------
       |class    = ${e.getClass.getCanonicalName}
       |toString = ${e.toString}
       |
       |msg parts:
       |
       |state      = ${e.getSQLState}
       |column     = ${msg.getColumn}
       |datatype   = ${msg.getDatatype}
       |detail     = ${msg.getDetail}
       |file       = ${msg.getFile}
       |hint       = ${msg.getHint}
       |line       = ${msg.getLine}
       |message    = ${msg.getMessage}
       |position   = ${msg.getPosition}
       |routine    = ${msg.getRoutine}
       |schema     = ${msg.getSchema}
       |severity   = ${msg.getSeverity}
       |table      = ${msg.getTable}
       |where      = ${msg.getWhere}
       |constraint = ${msg.getConstraint}
       |internal_query    = ${msg.getInternalQuery}
       |internal_position = ${msg.getInternalPosition}
       |""".stripMargin
  }
}
