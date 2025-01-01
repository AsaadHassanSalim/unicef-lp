package sd.gov.moe.lp.web.utils.views

import com.narbase.kunafa.core.components.View
import sd.gov.moe.lp.web.common.AppConfig
import sd.gov.moe.lp.web.utils.PopupZIndex
import disableBlurOptions
import tippy
import kotlin.js.json

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

@Suppress("ConstantConditionIf")
fun View.tooltip(message: String, delay: Int = 400) {
    if (AppConfig.isMobile.not()) {
        tippy(
            this.element, json(
                "content" to message,
                "delay" to arrayOf(delay, 0),
                "arrow" to "large",
                "zIndex" to PopupZIndex.getTopIndex(),
                disableBlurOptions
            )
        )
    }
}

