package com.example.notes_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes_app.NotePriority
import com.example.notes_app.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun PriorityTabLayout(
    selectedPriority: NotePriority,
    onPrioritySelected: (NotePriority) -> Unit,
    isDarkMode: Boolean = true
) {
    val priorities = listOf(
        NotePriority.HIGH,
        NotePriority.URGENT,
        NotePriority.MEDIUM,
        NotePriority.NORMAL
    )
    
    val pagerState = rememberPagerState(
        initialPage = priorities.indexOf(selectedPriority),
        initialPageOffsetFraction = TODO(),
        pageCount = TODO()
    )
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.getBackgroundColor(isDarkMode))
            .padding(8.dp)
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = AppColors.getSurfaceColor(isDarkMode),
            contentColor = AppColors.getTextColor(isDarkMode),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.width(tabPositions[pagerState.currentPage].width)
                        .offset(x = tabPositions[pagerState.currentPage].left),
                    color = when (priorities[pagerState.currentPage]) {
                        NotePriority.HIGH -> NotePriority.HIGH.color
                        NotePriority.URGENT -> NotePriority.URGENT.color
                        NotePriority.MEDIUM -> NotePriority.MEDIUM.color
                        NotePriority.NORMAL -> NotePriority.NORMAL.color
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            priorities.forEachIndexed { index, priority ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                            onPrioritySelected(priority)
                        }
                    },
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        text = priority.name,
                        color = if (pagerState.currentPage == index) AppColors.getTextColor(isDarkMode) else AppColors.getTextSecondaryColor(isDarkMode),
                        fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
