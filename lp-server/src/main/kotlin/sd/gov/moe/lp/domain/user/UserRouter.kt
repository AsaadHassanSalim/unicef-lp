package sd.gov.moe.lp.domain.user

import sd.gov.moe.lp.common.auth.AuthenticationConstants
import sd.gov.moe.lp.domain.user.files.CreateFileController
import sd.gov.moe.lp.domain.user.login.LoginController
import sd.gov.moe.lp.domain.user.profile.GetProfileController
import sd.gov.moe.lp.domain.user.profile.UpdateProfileController
import sd.gov.moe.lp.domain.user.profile.UserPasswordController
import sd.gov.moe.lp.domain.user.websocket.WebSocketController
import sd.gov.moe.lp.domain.utils.addInactiveUserInterceptor
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
const val myWsCustomAuthHeader = "ws_custom_auth"

fun Routing.setupUserRoutes() {
    route("/public/api/user/v1") {
        webSocket("/socket", myWsCustomAuthHeader) {
            call.request.header("")
            WebSocketController.handle(this)
        }
    }

    authenticate(AuthenticationConstants.JWT_AUTH) {
        route("/api/user") {
            addInactiveUserInterceptor()
            route("/v1") {
                post("/login") { LoginController().handle(call) }

                post("/upload_file") { CreateFileController(shouldCompress = true).handle(call) }
                post("/upload_raw_file") { CreateFileController(shouldCompress = false).handle(call) }

                route("/profile") {
                    post("/details") {
                        GetProfileController().handle(call)
                    }
                    post("/update") {
                        UpdateProfileController().handle(call)
                    }
                    post("/update_password") { UserPasswordController().handle(call) }
                }
            }
        }
    }
}

