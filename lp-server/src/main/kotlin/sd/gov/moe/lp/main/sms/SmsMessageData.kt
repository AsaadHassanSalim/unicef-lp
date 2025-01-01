package sd.gov.moe.lp.main.sms

@Suppress("ArrayInDataClass")
data class SmsMessageData(
    val message: String,
    val phones: Array<Long>
)
