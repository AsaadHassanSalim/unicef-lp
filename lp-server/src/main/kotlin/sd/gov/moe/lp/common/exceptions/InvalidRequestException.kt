package sd.gov.moe.lp.common.exceptions

import sd.gov.moe.lp.common.BasicResponse
import sd.gov.moe.lp.dto.common.network.CommonCodes

open class InvalidRequestException(message: String = "") : Exception(message)

class MissingArgumentException(parameter: String = "") : InvalidRequestException("parameter '$parameter' is missing")

class OutdatedAppException : Exception()

class InvalidRequestResponse(message: String) : BasicResponse(CommonCodes.INVALID_REQUEST, message)
