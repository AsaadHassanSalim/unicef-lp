package sd.gov.moe.lp.web.utils.session

import sd.gov.moe.lp.dto.models.roles.Privilege
import sd.gov.moe.lp.web.storage.SessionInfo

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

inline fun authorized(privileges: List<Privilege> = listOf(), block: () -> Unit) {
    if (isAuthorized(privileges)) {
        block()
    }
}

fun isAuthorized(privileges: List<Privilege> = listOf()): Boolean {
    return SessionInfo.loggedInUser.privileges.any { it in privileges }
}
