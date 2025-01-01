package sd.gov.moe.lp.dto.domain.user.websocket

import sd.gov.moe.lp.dto.common.enums.MessageTypes
import kotlin.js.JsExport

@JsExport
object WebSocketDtos {

    open class Message(val type: String)

    class Greeting(val greeting: String) : Message(MessageTypes.Greeting.toString())
}