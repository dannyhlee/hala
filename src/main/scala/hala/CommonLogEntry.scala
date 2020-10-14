package hala

import org.bson.types.ObjectId

//* A case class object to represent documents in our collection
// for what is known as Common Log Format (ref: https://httpd.apache.org/docs/2.4/logs.html#common)
// LogFormat: "%h %l %u %t \"%r\" %>s %b" common
// Sample:      127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326
//                  %h    %l  %u           %t                             \"%r\"             %>s  %b
//
// Fields: (ref: https://httpd.apache.org/docs/2.4/mod/mod_log_config.html#formats)
//      %h - host ip address
//      %l - remote logname from identd
//      %u - remote user if request was authenticated.  May be bogus if status (%s) is 401 (unauthorized)
//      %t - time in format [day/month/year:hour:minute:second zone]
//              day = 2*digit
//              month = 3*letter
//              year = 4*digit
//              hour = 2*digit
//              minute = 2*digit
//              second = 2*digit
//              zone = (`+' | `-') 4*digit
//      %r - first line of request (Method, resource requested, client used protocol)
//      %>s - status code sent back to client (success 2**, redirection 3**, error by client 4**, error by server (5**))
//      %b - size of the response in bytes, excluding HTTP headers.  None is "-" (CLF format) or "0" (%B)
//      */

case class CommonLogEntry(_id: ObjectId, sample: String, year: Option[Int]) {}

object CommonLogEntry {
    def apply(sample: String, year:Option[Int]) : CommonLogEntry = CommonLogEntry(new ObjectId(), sample, year)
}
