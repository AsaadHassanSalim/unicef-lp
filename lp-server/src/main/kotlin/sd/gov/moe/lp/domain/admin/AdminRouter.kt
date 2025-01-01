package sd.gov.moe.lp.domain.admin

import sd.gov.moe.lp.common.auth.AuthenticationConstants
import sd.gov.moe.lp.domain.admin.staff.EnableStaffController
import sd.gov.moe.lp.domain.admin.staff.UsersCrudController
import sd.gov.moe.lp.domain.admin.staff.roles.RolesCurdController
import sd.gov.moe.lp.domain.user.crud.crud
import sd.gov.moe.lp.domain.utils.addInactiveUserInterceptor
import sd.gov.moe.lp.dto.models.roles.Privilege
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

fun Routing.setupAdminRoutes() {
    authenticate(AuthenticationConstants.JWT_AUTH) {
        route("/api/admin") {
            addInactiveUserInterceptor()
            route("/v1") {

                route("/settings") {
                    crud(
                        "/users",
                        UsersCrudController(),
                        Privilege.UsersManagement,
                    )
                    crud(
                        "/roles",
                        RolesCurdController(),
                        Privilege.UsersManagement,
                    )
                    post("/enable_user") {
                        EnableStaffController().handle(call)
                    }
                }
            }
        }
    }
}