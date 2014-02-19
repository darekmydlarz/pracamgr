import com.mohiva.play.htmlcompressor.HTMLCompressorFilter
import play.api.mvc._

object Global extends WithFilters(HTMLCompressorFilter()) {

}
