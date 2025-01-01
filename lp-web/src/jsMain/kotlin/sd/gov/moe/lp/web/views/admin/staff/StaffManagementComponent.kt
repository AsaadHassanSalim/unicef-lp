package sd.gov.moe.lp.web.views.admin.staff

import com.narbase.kunafa.core.components.*
import com.narbase.kunafa.core.components.layout.LinearLayout
import com.narbase.kunafa.core.css.*
import com.narbase.kunafa.core.dimensions.dependent.matchParent
import com.narbase.kunafa.core.dimensions.dependent.wrapContent
import com.narbase.kunafa.core.dimensions.dimen
import com.narbase.kunafa.core.dimensions.px
import com.narbase.kunafa.core.drawable.Color
import com.narbase.kunafa.core.lifecycle.LifecycleOwner
import sd.gov.moe.lp.web.common.AppColors
import sd.gov.moe.lp.web.translations.localized
import sd.gov.moe.lp.web.utils.horizontalFiller
import sd.gov.moe.lp.web.utils.scrollable.scrollable
import sd.gov.moe.lp.web.utils.table.headerCell
import sd.gov.moe.lp.web.utils.table.listTable
import sd.gov.moe.lp.web.utils.table.tableCell
import sd.gov.moe.lp.web.utils.table.tableRow
import sd.gov.moe.lp.web.utils.verticalSeparator
import sd.gov.moe.lp.web.utils.views.*

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
class StaffManagementComponent(private val showCurrent: Boolean) : Component() {

    private var paginationControls: PaginationControls? = null
    private val viewModel = StaffManagementViewModel(showCurrent)
    private val upsertDialog = UpsertStaffMemberDialog(viewModel, showCurrent)

    private var listTableBody: View? = null
    private var contentLayout: View? = null

    override fun onViewCreated(lifecycleOwner: LifecycleOwner) {
        super.onViewCreated(lifecycleOwner)
        contentLayout?.withLoadingAndError(viewModel.uiState, onRetryClicked = {
            viewModel.getStaff()
        }, onLoaded = {
            onListLoaded()
        })
    }

    override fun onViewMounted(lifecycleOwner: LifecycleOwner) {
        super.onViewMounted(lifecycleOwner)
        viewModel.getStaff()
    }

    private fun onListLoaded() {
        paginationControls?.update(viewModel.pageNo, viewModel.pageSize, viewModel.total)
        listTableBody?.clearAllChildren()
        listTableBody?.apply {
            viewModel.data.forEachIndexed { index, item ->
                tableRow {
                    id = item.fullName
                    tableCell(item.fullName, 3, 16.px)
                    tableCell(1) {
                        textView {
                            text = "${item.callingCode} ${item.localPhone}"
                            style {
                                width = wrapContent
                                fontSize = 14.px
                                color = AppColors.black
                                direction = "ltr"
                            }
                        }

                    }
                    val dynamicRoles = item.dynamicRoles.map { it.name }.sorted()

                    tableCell(dynamicRoles.joinToString(), 1, 14.px)
                    onClick = {
                        upsertDialog.edit(item)
                    }
                }

                if (index != viewModel.data.lastIndex) {
                    verticalSeparator()
                }
            }
        }
    }

    override fun View?.getView() = view {
        id = "staffManagementRootView"
        mount(upsertDialog)
        style {
            matchParentDimensions
        }
        scrollable {
            style {
                matchParentDimensions
            }

            contentLayout = verticalLayout {
                style {
                    width = matchParent
                    height = wrapContent
                    padding = 32.px
                }
                horizontalLayout {
                    style {
                        width = matchParent
                    }
                    textView {
                        text = if (showCurrent) "Current Staff".localized() else "Inactive Staff".localized()
                        style {
                            width = wrapContent
                            fontSize = 20.px
                            fontWeight = "bold"
                        }
                    }

                    horizontalFiller()

                    if (showCurrent) {
                        addStaffButton()
                    }


                }

                horizontalLayout {
                    style {
                        width = matchParent
                        marginTop = 16.px
                    }

                    horizontalFiller()
                    searchTextInput("Search".localized()) {
                        viewModel.searchFor(it)
                    }
                }

                listTableBody = listTable {
                    headerCell("Full name".localized(), 3)
                    headerCell("Phone number".localized(), 1)
                    headerCell("Roles".localized(), 1)
                }
                paginationControls = setupPaginationControls(viewModel::getNextPage, viewModel::getPreviousPage)
            }
        }
    }

    private fun LinearLayout.addStaffButton() {
        textView {
            style {
                color = Color.white
                padding = "2px 12px".dimen()
                backgroundColor = AppColors.narcoreColor
                borderRadius = 12.px
                pointerCursor()
                hover {
                    backgroundColor = AppColors.narcoreDarkColor
                }
            }

            onClick = {
                upsertDialog.add()
            }

            id = "AddStaffButton"
            text = "+ Add new staff"
        }
    }

}
