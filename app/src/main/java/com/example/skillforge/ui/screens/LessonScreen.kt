package com.example.skillforge.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.FullscreenExit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.skillforge.data.Lesson
import com.example.skillforge.ui.theme.*
import com.example.skillforge.viewmodel.SkillforgeViewModel

@Composable
fun LessonScreen(
    viewModel: SkillforgeViewModel,
    courseTitle: String,
    lessonTitle: String,
    onBack: () -> Unit,
    onLessonClick: (Lesson) -> Unit
) {
    viewModel.uiState.collectAsStateWithLifecycle()
    val lesson = viewModel.findLesson(courseTitle, lessonTitle)
    val course = viewModel.findCourse(courseTitle)

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        if (lesson == null || course == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Teal)
            }
            return@Surface
        }

        // Find current lesson index to display in header
        val currentLessonIndex = remember(course.lessons, lesson) {
            course.lessons.indexOfFirst { it.title == lesson.title } + 1
        }

        val context = LocalContext.current
        var isFullscreen by remember { mutableStateOf(false) }
        var isVideoLoading by remember { mutableStateOf(true) }

        // Setup ExoPlayer using the actual link parsed from JSON
        val exoPlayer = remember(lesson.videoUrl) {
            ExoPlayer.Builder(context).build().apply {
                playWhenReady = true
                val mediaItem = MediaItem.fromUri(lesson.videoUrl)
                setMediaItem(mediaItem)
                prepare()
            }
        }

        // Listen for playback state to show loading progress bar
        DisposableEffect(exoPlayer) {
            val listener = object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    isVideoLoading = (playbackState == Player.STATE_BUFFERING || playbackState == Player.STATE_IDLE)
                }
            }
            exoPlayer.addListener(listener)
            onDispose {
                exoPlayer.removeListener(listener)
                exoPlayer.release()
            }
        }

        if (isFullscreen) {
            // Full-screen video layout
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = true
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Loading progress indicator
                if (isVideoLoading) {
                    CircularProgressIndicator(
                        color = Teal,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Exit fullscreen button in TopEnd corner
                IconButton(
                    onClick = { isFullscreen = false },
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FullscreenExit,
                        contentDescription = "Exit Fullscreen",
                        tint = Color.White
                    )
                }
            }
        } else {
            // Standard screen layout
            Column(modifier = Modifier.fillMaxSize()) {
                // 1. Video Player Area (height: 248.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(248.dp)
                        .background(TealDark)
                ) {
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                player = exoPlayer
                                useController = true
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Loading progress indicator
                    if (isVideoLoading) {
                        CircularProgressIndicator(
                            color = Teal,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    // Floating overlays (Back & Fullscreen buttons)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 18.dp, vertical = 12.dp)
                            .align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back button circle
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.4f))
                                .clickable { onBack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Fullscreen button circle
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.4f))
                                .clickable { isFullscreen = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Fullscreen,
                                contentDescription = "Fullscreen",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // 2. Lesson Title & Description Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 10.dp)
                ) {
                    Text(
                        text = "LESSON $currentLessonIndex · ${course.title.uppercase()}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 0.4.sp
                        ),
                        color = Teal
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 21.sp,
                            letterSpacing = (-0.3).sp
                        ),
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = lesson.content,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        ),
                        color = TextLightMuted
                    )
                }

                // 3. Tabs Row (Lessons / Notes / Resources)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .drawWithDividerBottom(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // "Lessons" Tab - Active
                    Column(
                        modifier = Modifier.padding(bottom = 11.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Lessons",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(11.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 54.dp, height = 2.5.dp)
                                .background(Teal, RoundedCornerShape(99.dp))
                        )
                    }

                    // "Notes" Tab - Inactive
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        ),
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 11.dp)
                    )

                    // "Resources" Tab - Inactive
                    Text(
                        text = "Resources",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        ),
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 11.dp)
                    )
                }

                // 4. Scrollable List of Lessons
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(course.lessons) { index, item ->
                        val isPlaying = item.title == lesson.title
                        
                        LessonListItem(
                            lesson = item,
                            isPlaying = isPlaying,
                            onClick = {
                                if (item.isFree && !isPlaying) {
                                    onLessonClick(item)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// Custom extension modifier to draw the Divider line underneath tabs
private fun Modifier.drawWithDividerBottom(): Modifier {
    return this.drawWithContent {
        drawContent()
        drawLine(
            color = Divider,
            start = androidx.compose.ui.geometry.Offset(0f, size.height),
            end = androidx.compose.ui.geometry.Offset(size.width, size.height),
            strokeWidth = 1.dp.toPx()
        )
    }
}

@Composable
private fun LessonListItem(lesson: Lesson, isPlaying: Boolean, onClick: () -> Unit) {
    val cardBg = if (isPlaying) Color(0x140FB5A4) else Color.White
    val cardBorderColor = if (isPlaying) Color(0x400FB5A4) else Divider
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardBg, RoundedCornerShape(14.dp))
            .border(1.dp, cardBorderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 13.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isPlaying) {
                // Active playing double-bars icon container
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Teal),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .size(width = 3.dp, height = 13.dp)
                                .background(Color.White, RoundedCornerShape(1.dp))
                        )
                        Box(
                            Modifier
                                .size(width = 3.dp, height = 13.dp)
                                .background(Color.White, RoundedCornerShape(1.dp))
                        )
                    }
                }
            } else {
                // Circle play/lock background
                val iconBgColor = if (lesson.isFree) Color(0x1F0FB5A4) else Color(0xFFF3F2EE)
                val iconColor = if (lesson.isFree) Teal else Color(0xFFA6A49C)
                
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (lesson.isFree) Icons.Filled.PlayArrow else Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(Modifier.width(13.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = if (isPlaying) Color(0xFF0E7C70) else if (lesson.isFree) TextPrimary else Color(0xFF6B6B62),
                    maxLines = 1
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = if (isPlaying) "Now playing · ${lesson.durationMinutes} min" else "${lesson.durationMinutes} min",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (isPlaying) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 12.sp
                    ),
                    color = if (isPlaying) Color(0xFF5BA89F) else TextSecondary
                )
            }

            if (!isPlaying && lesson.isFree) {
                Box(
                    modifier = Modifier
                        .background(Color(0x1A14B8A6), RoundedCornerShape(7.dp))
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "FREE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = Color(0xFF14B8A6)
                    )
                }
            }
        }
    }
}
