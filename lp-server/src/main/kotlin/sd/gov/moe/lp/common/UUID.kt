package sd.gov.moe.lp.common

import java.util.*

fun String.toUUID(): UUID = UUID.fromString(this)