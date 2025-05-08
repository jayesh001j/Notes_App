package com.example.notes_app.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes_app.Note
import com.example.notes_app.NotePriority
import com.example.notes_app.NoteViewModel
import com.example.notes_app.R
import com.example.notes_app.ui.components.NoteCard
import com.example.notes_app.ui.components.PriorityTabLayout
import com.example.notes_app.ui.theme.AppColors
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun NotesListScreen(
    viewModel: NoteViewModel,
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = true
) {
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    // State for selected priority filter
    var selectedPriority by remember { mutableStateOf(NotePriority.HIGH) }
    
    // Filter notes by priority if needed
    val filteredNotes = if (viewModel.selectedTab.value == com.example.notes_app.NoteTab.HIGH_PRIORITY) {
        notes.filter { it.priority == selectedPriority }
    } else {
        notes
    }
    
    Box(modifier = Modifier.fillMaxSize().background(AppColors.getBackgroundColor(isDarkMode))) {
        Column {
            TopAppBar(
                title = {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Notes",
                            color = AppColors.getTextColor(isDarkMode),
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                    } else {
                        TextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = {
                                Text(
                                    text = "Search notes...",
                                    color = Color.Gray,
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.White,
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                backgroundColor = AppColors.getSurfaceColor(isDarkMode),
                actions = {
                    IconButton(onClick = { 
                        viewModel.setSearchQuery(if (searchQuery.isEmpty()) " " else "") 
                    }) {
                        Icon(
                            if (searchQuery.isEmpty()) Icons.Default.Search else Icons.Default.ArrowBack,
                            contentDescription = if (searchQuery.isEmpty()) "Search" else "Back",
                            tint = AppColors.getTextColor(isDarkMode)
                        )
                    }
                }
            )
            
            // Show priority tabs only when in HIGH_PRIORITY tab
            if (viewModel.selectedTab.value == com.example.notes_app.NoteTab.HIGH_PRIORITY) {
                PriorityTabLayout(
                    selectedPriority = selectedPriority,
                    onPrioritySelected = { priority ->
                        selectedPriority = priority
                    },
                    isDarkMode = isDarkMode
                )
            }
            
            if (filteredNotes.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.rafiki),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Create your first note!",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Tap + to get started",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredNotes.filter { note ->
                        searchQuery.isEmpty() || 
                        note.title.contains(searchQuery, ignoreCase = true) || 
                        note.content.contains(searchQuery, ignoreCase = true)
                    }, key = { it.id }) { note ->
                        val animatedScale = remember { Animatable(0.8f) }
                        LaunchedEffect(note) {
                            animatedScale.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        }
                        Card(
                            backgroundColor = note.color,
                            shape = RoundedCornerShape(16.dp),
                            elevation = animateFloatAsState(if (searchQuery.isEmpty()) 4f else 8f).value.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    scaleX = animatedScale.value
                                    scaleY = animatedScale.value
                                    alpha = animatedScale.value
                                }
                                .swipeToDismiss { 
                                    viewModel.deleteNote(note.id) 
                                }
                                .clickable { onNoteClick(note) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = note.title,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (note.content.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = note.content,
                                        fontSize = 14.sp,
                                        color = Color.Black.copy(alpha = 0.7f),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = viewModel.formatDate(note.dateTime),
                                    fontSize = 12.sp,
                                    color = Color.Black.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
        val fabScale = remember { Animatable(1f) }
        LaunchedEffect(notes.isEmpty()) {
            if (notes.isEmpty()) {
                fabScale.animateTo(
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
        }
        FloatingActionButton(
            onClick = onAddNoteClick,
            backgroundColor = Color(0xFF232323),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .shadow(8.dp, CircleShape)
                .scale(animateFloatAsState(if (notes.isEmpty()) 1.2f else 1f).value)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun Modifier.swipeToDismiss(onDismiss: () -> Unit): Modifier = composed {
    // State to track if we're swiping or not
    var isDragging by remember { mutableStateOf(false) }
    // State to track the offset
    val offsetX = remember { Animatable(0f) }
    // Coroutine scope for animations
    val scope = rememberCoroutineScope()
    
    this.pointerInput(Unit) {
        // Detect horizontal drag gestures
        detectHorizontalDragGestures(
            onDragStart = { isDragging = true },
            onDragEnd = {
                // When drag ends, check if we've dragged far enough to dismiss
                isDragging = false
                scope.launch {
                    if (abs(offsetX.value) > 100) {
                        // If dragged far enough, call onDismiss
                        onDismiss()
                    } else {
                        // Otherwise, animate back to start position
                        offsetX.animateTo(0f, spring())
                    }
                }
            },
            onDragCancel = {
                // If drag is canceled, animate back to start position
                isDragging = false
                scope.launch {
                    offsetX.animateTo(0f, spring())
                }
            },
            onHorizontalDrag = { change, dragAmount ->
                // Consume the drag event
                change.consume()
                // Update the offset
                scope.launch {
                    offsetX.snapTo(offsetX.value + dragAmount)
                }
            }
        )
    }
    .offset(x = offsetX.value.dp) // Apply the offset
    .graphicsLayer {
        // Fade out as we swipe
        alpha = 1f - (abs(offsetX.value) / 300f).coerceIn(0f, 0.5f)
    }
}
