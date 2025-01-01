package sd.gov.moe.lp.dto.common.enums

import sd.gov.moe.lp.dto.common.EnumDtoName

enum class SmsMessageStatus(override val persistenceName: String, override val dtoName: String) : EnumPersistenceName,
    EnumDtoName {
    /**
     * Message not send and its timeToSend has not passed
     */
    Pending("Pending", "Pending"),

    Sent("Sent", "Sent"),

    Failed("Failed", "Failed"),
}