package hala

import java.time.LocalDateTime
import org.bson.types.ObjectId

/** Common Log Format (ref: https://httpd.apache.org/docs/2.4/logs.html#common)
 * LogFormat: "%h %l %u %t \"%r\" %>s %b" common
 * Sample:      127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326
 *                  %h    %l  %u           %t                             \"%r\"             %>s  %b
 *
 * Fields: (ref: https://httpd.apache.org/docs/2.4/mod/mod_log_config.html#formats)
 *      %h - host ip address
 *      %l - remote logname from identd
 *      %u - remote user if request was authenticated.  May be bogus if status (%s) is 401 (unauthorized)
 *      %t - time in format [day/month/year:hour:minute:second zone]
 *              day = 2*digit
 *              month = 3*letter
 *              year = 4*digit
 *              hour = 2*digit
 *              minute = 2*digit
 *              second = 2*digit
 *              zone = ('+' | '-') 4*digit
 *      %r - first line of request (Method, resource requested, client used protocol)
 *      %>s - status code sent back to client
 *              success 2**
 *              redirection 3**
 *              error by client 4**
 *              error by server 5**
 *      %b - size of the response in bytes, excluding HTTP headers.  None is "-" (CLF format) or "0" (%B)
 *
 *  @constructor create a new log entry
 *  @param _id ObjectId
 *  @param hostIp String - host ip address
 *  @param logname String or null - remote logname
 *  @param remoteUser String or null - remote user
 *  @param time LocalDateTime - time (java.time.LocalDateTime)
 *  @param request String - first line of request (Method, resource requested, client used protocol)
 *  @param statusCode Int - status code
 *  @param size Int - response size in bytes
 */
case class
  LogEntry(
     _id: ObjectId,
     hostIp: String,
     logname: String,
     remoteUser: String,
     time: LocalDateTime,
     request: String,
     statusCode: Int,
     size: Int
          ) {}

/** Factory for [[hala.LogEntry]] instances.
 * Creates a log entry
 *
 *    @param _id ObjectId
 *    @param hostIp String - host ip address
 *    @param logname String or null - remote logname
 *    @param remoteUser String or null - remote user
 *    @param time LocalDateTime - time (java.time.LocalDateTime)
 *    @param request String - first line of request (Method, resource requested, client used protocol)
 *    @param statusCode Int - status code
 *    @param size Int - response size in bytes
 *    */
object LogEntry {
  /** Creates a common log entry */
  def apply(_id: ObjectId, hostIp: String, logname: String,
      remoteUser: String, time: LocalDateTime, request: String,
      statusCode: Int, size: Int) : LogEntry = new LogEntry(_id: ObjectId,
      hostIp: String, logname: String, remoteUser: String,
      time: LocalDateTime, request: String, statusCode: Int, size: Int)
}
