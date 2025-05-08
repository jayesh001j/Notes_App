package com.example.notes_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.notes_app.Note
import com.example.notes_app.NotePriority
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        backgroundColor = note.color
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Priority indicator
            Box(
                modifier = Modifier
                    .size(width = 8.dp, height = 80.dp)
                    .background(getPriorityColor(note.priority), RoundedCornerShape(4.dp))
                    .align(Alignment.CenterVertically)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (note.priority == NotePriority.HIGH || note.priority == NotePriority.URGENT) {
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Icon(
                            imageVector = Icons.Default.PriorityHigh,
                            contentDescription = "High Priority",
                            tint = getPriorityColor(note.priority),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.body2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatDate(note.dateTime),
                        style = MaterialTheme.typography.caption,
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Text(
                        text = note.priority.name,
                        style = MaterialTheme.typography.caption,
                        color = getPriorityColor(note.priority),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun getPriorityColor(priority: NotePriority): Color {
    return when (priority) {
        NotePriority.HIGH -> Color(0xFFE57373)    // Red
        NotePriority.URGENT -> Color(0xFFFFB74D)  // Orange
        NotePriority.MEDIUM -> Color(0xFFFFF176)  // Amber
        NotePriority.NORMAL -> Color(0xFFE0E0E0)  // Gray
    }
}

private fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return formatter.format(date)
}
