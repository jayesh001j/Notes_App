
package com.example.notes_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.setContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.animateFloatAsState

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.material.Text
import androidx.compose.foundation.Image
import androidx.compose.animation.core.*
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.launch
import kotlin.math.abs


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun NotesScreen(
    notes: List<Note>,
    onAddNote: () -> Unit,
    onNoteClick: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF232323))) {
        Column {
            TopAppBar(
                title = {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Notes",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                    } else {
                        TextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            placeholder = {
                                Text(
                                    text = "Search notes...",
                                    color = Color.Gray,
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.White,
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                backgroundColor = Color(0xFF232323),
                actions = {
                    IconButton(onClick = { onSearchQueryChange(if (searchQuery.isEmpty()) " " else "") }) {
                        Icon(
                            if (searchQuery.isEmpty()) Icons.Default.Search else Icons.Default.ArrowBack,
                            contentDescription = if (searchQuery.isEmpty()) "Search" else "Back",
                            tint = Color.White
                        )
                    }
                }
            )
            if (notes.isEmpty()) {
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
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(notes.filter { note ->
                        searchQuery.isEmpty() ||
                                note.title.contains(searchQuery, ignoreCase = true) ||
                                note.content.contains(searchQuery, ignoreCase = true)
                    }) { note ->
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
                                .swipeToDismiss { onDeleteNote(note) }
                                .clickable { onNoteClick(note) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = note.title,

                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                                if (note.content.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = note.content,

                                        fontSize = 14.sp,
                                        color = Color.Black.copy(alpha = 0.7f),
                                        maxLines = 2
                                    )
                                }
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
            onClick = onAddNote,
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

@Composable
fun Modifier.swipeToDismiss(onDismiss: () -> Unit): Modifier = composed {
    val offsetX = remember { androidx.compose.animation.core.Animatable(0f) }
    val scope = rememberCoroutineScope()

    pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragEnd = {
                scope.launch {
                    if (abs(offsetX.value) > 100) {
                        onDismiss()
                    } else {
                        offsetX.animateTo(0f, spring())
                    }
                }
            }
        ) { change, dragAmount ->
            change.consume()
            scope.launch {
                offsetX.snapTo(offsetX.value + dragAmount)
            }
        }
    }.offset(x = offsetX.value.dp)
        .graphicsLayer {
            alpha = 1 - (abs(offsetX.value) / 300f).coerceIn(0f, 0.5f)
        }
}

@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    Card(
        backgroundColor = note.color,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            androidx.compose.material.Text(note.title, color = Color.Black, style = MaterialTheme.typography.h6)
        }
    }
}


@Composable
fun NoteEditorScreen(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onBack: () -> Unit
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
                    IconButton(onClick = { /* Preview */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Preview", tint = Color.White)
                    }
                    IconButton(onClick = { /* Save */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Save", tint = Color.White)
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
                    cursorColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
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
                    cursorColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF232323)),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* Bold */ }) {
                    Icon(Icons.Default.Done, contentDescription = null, tint = Color.White)
                }
                IconButton(onClick = { /* Italic */ }) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                }
                IconButton(onClick = { /* Underline */ }) {
                    Icon(Icons.Default.Build, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun App() {
    val context = LocalContext.current
    val db = remember { NotesDatabase(context) }
    val notes = remember { mutableStateListOf<Note>().apply {
        addAll(db.getAllNotes())
    }}
    var searchQuery by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var editingNoteId by remember { mutableStateOf<Long?>(null) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var noteColor by remember { mutableStateOf(Color(0xFFFFF59D)) }

    val colors = listOf(
        Color(0xFFFFA8F7), Color(0xFFFFA8A8), Color(0xFF8AFFA8),
        Color(0xFFFFFFA8), Color(0xFFA8FFFF), Color(0xFFA8A8FF)
    )

    if (isEditing) {
        NoteEditorScreen(
            title = title,
            content = content,
            onTitleChange = { title = it },
            onContentChange = { content = it },
            onBack = {
                isEditing = false
                title = ""
                content = ""
            }
        )
        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = {
                    if (title.isNotBlank() || content.isNotBlank()) {
                        val note = Note(
                            title = title.ifBlank { "Untitled" },
                            content = content,
                            color = colors[notes.size % colors.size]
                        )
                        val id = db.insertNote(note)
                        notes.add(0, note)
                        isEditing = false
                        editingNoteId = null
                        title = ""
                        content = ""
                    }
                },
                backgroundColor = Color(0xFF232323),
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                Icon(Icons.Default.Done, contentDescription = "Save")
            }
        }
    } else {
        NotesScreen(
            notes = notes,
            onAddNote = {
                isEditing = true
                title = ""
                content = ""
            },
            onNoteClick = { note ->
                title = note.title
                content = note.content
                noteColor = note.color
                isEditing = true
            },
            onDeleteNote = { note ->
                notes.remove(note)
                if (editingNoteId != null) {
                    db.deleteNote(editingNoteId!!)
                }
                Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
            },
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )
    }
}