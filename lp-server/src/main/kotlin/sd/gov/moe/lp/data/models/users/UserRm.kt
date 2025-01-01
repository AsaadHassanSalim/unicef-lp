package sd.gov.moe.lp.data.models.users

import sd.gov.moe.lp.data.models.clients.Client
import sd.gov.moe.lp.data.models.roles.Role

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
class UserRm(
    val client: Client,
    val user: User,
    val roles: List<Role>
)
