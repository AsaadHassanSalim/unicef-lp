/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
package sd.gov.moe.lp.domain.user.profile

import sd.gov.moe.lp.common.DataResponse
import sd.gov.moe.lp.common.EndpointHandler
import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.dto.domain.hello_world.HelloWorldEndPoint


class HelloWorldController : EndpointHandler<HelloWorldEndPoint.Request, HelloWorldEndPoint.Response>(
    HelloWorldEndPoint.Request::class,
    HelloWorldEndPoint
) {
    override fun process(
        requestDto: HelloWorldEndPoint.Request,
        clientData: AuthorizedClientData?,
    ): DataResponse<HelloWorldEndPoint.Response> {
        return DataResponse(HelloWorldEndPoint.Response("${requestDto.data}:Hehe"))
    }


}
