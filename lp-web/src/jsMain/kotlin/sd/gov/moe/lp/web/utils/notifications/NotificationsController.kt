package sd.gov.moe.lp.web.utils.notifications

import sd.gov.moe.lp.dto.common.enums.MessageTypes
import sd.gov.moe.lp.web.common.AppConfig
import sd.gov.moe.lp.web.events.ServerConnectionEvent
import sd.gov.moe.lp.web.network.ServerCaller
import sd.gov.moe.lp.web.storage.StorageManager
import sd.gov.moe.lp.dto.common.network.DataResponse
import sd.gov.moe.lp.dto.domain.user.websocket.WebSocketDtos
import sd.gov.moe.lp.web.utils.eventbus.EventBus
import sd.gov.moe.lp.web.utils.views.SnackBar
import kotlinx.browser.window
import org.w3c.dom.WebSocket
@JsExport
object NotificationsController {
    var isConnectedToServer = false

    /**
     * Call initializedWebSocket before using
     */
    private var webSocket: WebSocket? = null

    private fun initializedWebSocket(): WebSocket {
        val location = window.location
        val host = location.host
        val wsProtocol = if (location.protocol == "https:") "wss:" else "ws:"
        val baseUrl =
            if (AppConfig.isDev)
                ServerCaller.BASE_URL.replace("http://", "ws://").replace("https://", "ws://")
            else
                "$wsProtocol//$host"
        return WebSocket("${baseUrl}/public/api/user/v1/socket",
            StorageManager.accessToken?.let { arrayOf("ws_custom_auth", it) })
    }

    fun connect() {
        if (isConnectedToServer) {
            disconnect()
        }
        webSocket = initializedWebSocket()
        webSocket?.onclose = {
            isConnectedToServer = false
            EventBus.publish(ServerConnectionEvent(isConnected = false))
            val connectionRetryDuration = 5_000
            window.setTimeout({
                connect()
            }, connectionRetryDuration)
        }
        isConnectedToServer = true
        EventBus.publish(ServerConnectionEvent(isConnected = true))

        webSocket?.onmessage = { messageEvent ->
            messageEvent.data?.toString()?.let { data ->
                val message = JSON.parse<DataResponse<WebSocketDtos.Message>>(data).data
                when (message.type) {
                    MessageTypes.Greeting.toString() -> {
                        SnackBar.showText("Greetings")
                    }
                }

            }
        }
    }


    fun disconnect() {
        webSocket?.onclose = {
            isConnectedToServer = false
            EventBus.publish(ServerConnectionEvent(isConnected = false))
        }
        webSocket?.close()
    }
}
