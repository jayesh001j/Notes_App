package com.example.notes_app.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notes_app.NoteTab
import com.example.notes_app.ui.theme.AppColors

@Composable
fun ModernNotesBottomNavigation(
    currentTab: NoteTab,
    onTabSelected: (NoteTab) -> Unit,
    isDarkMode: Boolean = true
) {
    BottomNavigation(
        backgroundColor = AppColors.getSurfaceColor(isDarkMode),
        contentColor = AppColors.getTextColor(isDarkMode),
        elevation = 8.dp
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "All Notes") },
            label = { Text("All Notes") },
            selected = currentTab == NoteTab.ALL_NOTES,
            onClick = { onTabSelected(NoteTab.ALL_NOTES) },
            selectedContentColor = AppColors.getTextColor(isDarkMode),
            unselectedContentColor = AppColors.getTextSecondaryColor(isDarkMode)
        )
        

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentTab == NoteTab.SETTINGS,
            onClick = { onTabSelected(NoteTab.SETTINGS) },
            selectedContentColor = AppColors.getTextColor(isDarkMode),
            unselectedContentColor = AppColors.getTextSecondaryColor(isDarkMode)
        )
    }
}
