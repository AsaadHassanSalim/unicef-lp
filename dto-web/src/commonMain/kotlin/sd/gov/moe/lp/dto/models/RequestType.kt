/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
package sd.gov.moe.lp.dto.models


sealed interface RequestType {
    object FormData : RequestType
}