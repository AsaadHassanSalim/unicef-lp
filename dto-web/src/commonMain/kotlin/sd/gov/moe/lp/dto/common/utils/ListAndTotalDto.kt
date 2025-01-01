package sd.gov.moe.lp.dto.common.utils

import sd.gov.moe.lp.dto.common.KmmLong

class ListAndTotalDto<T>(val list: Array<T>, val total: KmmLong)