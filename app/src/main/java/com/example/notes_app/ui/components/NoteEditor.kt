package com.example.notes_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes_app.NotePriority

@Composable
fun NoteEditor(
    title: String,
    content: String,
    priority: NotePriority,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onPriorityChange: (NotePriority) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    isNewNote: Boolean
) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF232323))) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Row {
                    if (!isNewNote) {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                        }
                    }
                    IconButton(onClick = onSave) {
                        Icon(Icons.Default.Done, contentDescription = "Save", tint = Color.White)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = {
                    Text(
                        text = "Title",
                        color = Color.Gray,
                        fontSize = 32.sp
                    )
                },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            
            TextField(
                value = content,
                onValueChange = onContentChange,
                placeholder = {
                    Text(
                        text = "Type something...",
                        color = Color.Gray
                    )
                },
                textStyle = TextStyle(
                    color = Color.White
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
            
            // Priority selector (now at the bottom as a toolbar)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF232323))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PriorityItem(
                    text = "High",
                    color = Color(0xFFE57373),
                    selected = priority == NotePriority.HIGH,
                    onClick = { onPriorityChange(NotePriority.HIGH) }
                )
                
                PriorityItem(
                    text = "Urgent",
                    color = Color(0xFFFFB74D),
                    selected = priority == NotePriority.URGENT,
                    onClick = { onPriorityChange(NotePriority.URGENT) }
                )
                
                PriorityItem(
                    text = "Medium",
                    color = Color(0xFFFFF176),
                    selected = priority == NotePriority.MEDIUM,
                    onClick = { onPriorityChange(NotePriority.MEDIUM) }
                )
                
                PriorityItem(
                    text = "Normal",
                    color = Color(0xFFE0E0E0),
                    selected = priority == NotePriority.NORMAL,
                    onClick = { onPriorityChange(NotePriority.NORMAL) }
                )
            }
        }
    }
}

@Composable
fun PriorityItem(
    text: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color)
                .then(
                    if (selected) {
                        Modifier.border(2.dp, MaterialTheme.colors.primary, CircleShape)
                    } else {
                        Modifier
                    }
                )
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = text,
            style = MaterialTheme.typography.caption,
            color = if (selected) MaterialTheme.colors.primary else Color.Gray
        )
    }
}
