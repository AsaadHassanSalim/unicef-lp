package sd.gov.moe.lp.web.utils.views

import com.narbase.kunafa.core.components.View
import com.narbase.kunafa.core.components.imageView
import com.narbase.kunafa.core.components.textView
import com.narbase.kunafa.core.components.verticalLayout
import com.narbase.kunafa.core.css.*
import com.narbase.kunafa.core.dimensions.percent
import com.narbase.kunafa.core.dimensions.px
import sd.gov.moe.lp.web.common.AppColors
import sd.gov.moe.lp.web.translations.localized

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

fun View.emptyList(message: String = "No data yet.".localized()) {
    verticalLayout {
        style {
            alignSelf = Alignment.Center
            margin = 30.px
        }

        imageView {
            style {
                margin = 30.px
                width = 200.px
                maxWidth = 60.percent
                alignSelf = Alignment.Center
                objectFit = "cover"
            }

            element.src = "/public/img/no_data.svg"
        }

        textView {
            text = message
            style {
                margin = 30.px
                alignSelf = Alignment.Center
                color = AppColors.textLight
                fontSize = 18.px
            }
        }
    }

}
