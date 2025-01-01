package sd.gov.moe.lp.web.storage

import com.narbase.kunafa.core.components.View
import com.narbase.kunafa.core.css.transform
import sd.gov.moe.lp.web.common.models.Direction

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

fun <T> bidirectional(ltrValue: T, rtlValue: T): T {
    return if (StorageManager.language.direction == Direction.RTL) rtlValue else ltrValue
}

fun bidirectional(ltrValue: () -> Unit, rtlValue: () -> Unit) {
    if (StorageManager.language.direction == Direction.RTL) rtlValue() else ltrValue()
}

fun View.bidirectionalView() {
    style {
        transform = bidirectional("unset", "scaleX(-1)")
    }
}
