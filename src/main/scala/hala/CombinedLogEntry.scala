package hala

import org.bson.types.ObjectId

//* A case class object to represent documents in our collection
// for what is known as Combined Log Format (ref: https://httpd.apache.org/docs/2.4/logs.html#combined)
// LogFormat:   "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\"" combined
// Sample:      127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326 (continued line 10)
//                  %h    %l  %u           %t                             \"%r\"             %>s  %b
//              "http://semicomplete.com/presentations/logstash-puppetconf-2012/" "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0"
//                                          %{Referer}i                                              %{User-agent}i
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
//      %{VARNAME}i - contents of VARNAME: header line(s)
//      */

case class CombinedLogEntry(_id: ObjectId, sample: String, year: Option[Int]) {}

object CombinedLogEntry {
    def apply(sample: String, year:Option[Int]) : CombinedLogEntry = CombinedLogEntry(new ObjectId(), sample, year)
}

// https://mongodb-documentation.readthedocs.io/en/latest/use-cases/storing-log-data.html#gsc.tab=0



