package sd.gov.moe.lp.core

import sd.gov.moe.lp.dto.common.utils.MultiLingualTextDto


data class MultiLingualText(val en: String, val ar: String) {
    fun toDto(): MultiLingualTextDto = MultiLingualTextDto(en, ar)
}

fun MultiLingualTextDto.toDs(): MultiLingualText = MultiLingualText(en, ar)


