/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */


package sd.gov.moe.lp.dto.domain.hello_world

import sd.gov.moe.lp.router.EndPoint


object HelloWorldEndPoint : EndPoint<HelloWorldEndPoint.Request, HelloWorldEndPoint.Response>() {
    class Request(
        val data: String? = null,
    )

    class Response(
        val data: String? = null,
    )
}

