package sd.gov.moe.lp.dto.common.network

import kotlin.js.JsExport

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

@JsExport
class DataResponse<T>(
    val data: T
) : BasicResponse() {
    companion object {
        const val BASIC_SUCCESS = 0
        const val UNAUTHENTICATED = 10
        const val INVALID_REQUEST = "11"
        const val UNKNOWN_ERROR = "12"
        const val NOT_FOUND_ERROR = "13"
        const val OUTDATED_APP = "14"
    }
}
