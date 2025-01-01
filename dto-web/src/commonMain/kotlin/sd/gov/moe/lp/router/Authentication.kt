/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
package sd.gov.moe.lp.router


enum class Authentication(val configurationName: String) {
    JWT(AuthenticationConstants.JWT_AUTH),
    Basic(AuthenticationConstants.BASIC_AUTH)
}

object AuthenticationConstants {
    const val BASIC_AUTH = "BasicAuth"
    const val JWT_AUTH = "JwtAuth"
}