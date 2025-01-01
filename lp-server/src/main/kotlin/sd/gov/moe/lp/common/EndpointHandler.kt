/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */


package sd.gov.moe.lp.common


import sd.gov.moe.lp.dto.models.RequestType
import sd.gov.moe.lp.router.EndPoint
import kotlin.reflect.KClass


abstract class EndpointHandler<V : Any, out D : Any>(
    private val requestDtoClass: KClass<V>,
    val endPoint: EndPoint<V, D>,
) : Handler<V, D>(requestDtoClass)

abstract class BasicEndpointHandler<out D : Any>(
    val endPoint: EndPoint<RequestType.FormData, D>,
) : BasicHandler<D>()