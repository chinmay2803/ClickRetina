package com.example.skillforge.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.skillforge.data.Course
import com.example.skillforge.data.Lesson
import com.example.skillforge.ui.theme.*
import com.example.skillforge.viewmodel.SkillforgeViewModel

@Composable
fun CourseDetailScreen(
    viewModel: SkillforgeViewModel,
    courseTitle: String,
    onBack: () -> Unit,
    onLessonClick: (Lesson) -> Unit
) {
    viewModel.uiState.collectAsStateWithLifecycle()
    val course = viewModel.findCourse(courseTitle)

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        if (course == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Teal)
            }
            return@Surface
        }

        val totalDurationMinutes = remember(course.lessons) {
            course.lessons.sumOf { it.durationMinutes }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Main content area
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // 1. Hero Image Section
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) {
                        AsyncImage(
                            model = course.thumbnailUrl,
                            contentDescription = course.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Gradient Overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.28f),
                                            Color.Transparent,
                                            Color.Transparent,
                                            Cream
                                        )
                                    )
                                )
                        )
                        // Top bar navigation overlay
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(horizontal = 18.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Circular back button
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.92f))
                                    .clickable { onBack() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Circular bookmark button
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.92f))
                                    .clickable { /* TODO */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // 2. Info & Metadata Section
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                    ) {
                        // Tag capsules
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            course.tags.forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .background(TealSoft, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 11.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        ),
                                        color = Teal
                                    )
                                }
                            }
                        }

                        // Title & Subtitle
                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 25.sp,
                                letterSpacing = (-0.5).sp,
                                lineHeight = 29.sp
                            ),
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = course.subtitle,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 15.sp,
                                lineHeight = 19.sp
                            ),
                            color = TextLightMuted
                        )

                        // Stats Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(18.dp),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            // Star Rating
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFF5A623),
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "${course.rating}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    ),
                                    color = Color(0xFF5C5C54)
                                )
                            }

                            // Students count
                            Text(
                                text = "%,d".format(course.studentsEnrolled),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                ),
                                color = Color(0xFF5C5C54)
                            )

                            // Duration
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.AccessTime,
                                    contentDescription = null,
                                    tint = TextLightMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${course.durationHours}h",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    ),
                                    color = Color(0xFF5C5C54)
                                )
                            }

                            // Level tag
                            Text(
                                text = course.level,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                ),
                                color = Teal
                            )
                        }

                        // Instructor Card
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 18.dp)
                                .background(Color.White, RoundedCornerShape(16.dp))
                                .border(1.dp, Divider, RoundedCornerShape(16.dp))
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = course.instructor.avatarUrl,
                                contentDescription = course.instructor.name,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = course.instructor.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    color = TextPrimary
                                )
                                Spacer(Modifier.height(1.dp))
                                Text(
                                    text = course.instructor.title,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.5.sp
                                    ),
                                    color = TextLightMuted
                                )
                            }
                            Text(
                                text = "Follow",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                ),
                                color = Teal,
                                modifier = Modifier.clickable { /* TODO */ }
                            )
                        }

                        // Description
                        Text(
                            text = course.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.5.sp,
                                lineHeight = 23.sp
                            ),
                            color = TextBody,
                            modifier = Modifier.padding(top = 18.dp)
                        )

                        // Course content header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp, bottom = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "Course content",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp
                                ),
                                color = TextPrimary
                            )
                            Text(
                                text = "${course.lessons.size} lessons · ${totalDurationMinutes} min",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                ),
                                color = TextLightMuted
                            )
                        }
                    }
                }

                // 3. Lessons list
                items(course.lessons) { lesson ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
                        LessonRow(lesson = lesson, onClick = { onLessonClick(lesson) })
                    }
                }

                item {
                    Spacer(Modifier.height(24.dp))
                }
            }

            // 4. Sticky Bottom Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.94f))
                    .border(1.dp, Divider, RoundedCornerShape(0.dp))
                    .padding(horizontal = 20.dp, vertical = 14.dp)
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PRICE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = TextSecondary
                        )
                        Text(
                            text = "Free",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            ),
                            color = Teal
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .background(Teal, RoundedCornerShape(14.dp))
                            .clickable { /* TODO */ }
                            .padding(vertical = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Enroll now",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonRow(lesson: Lesson, onClick: () -> Unit) {
    val iconBgColor = if (lesson.isFree) Color(0x1F0FB5A4) else Color(0xFFF3F2EE)
    val iconColor = if (lesson.isFree) Teal else Color(0xFFA6A49C)
    
    val baseModifier = Modifier
        .fillMaxWidth()
        .background(Color.White, RoundedCornerShape(15.dp))
        .border(1.dp, Divider, RoundedCornerShape(15.dp))

    val clickableModifier = if (lesson.isFree) {
        baseModifier.clickable { onClick() }
    } else {
        baseModifier
    }

    Box(
        modifier = clickableModifier.padding(vertical = 13.dp, horizontal = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle play/lock background
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (lesson.isFree) Icons.Filled.PlayArrow else Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(Modifier.width(13.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title, 
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp
                    ), 
                    color = if (lesson.isFree) TextPrimary else Color(0xFF6B6B62),
                    maxLines = 1
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${lesson.durationMinutes} min",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    ),
                    color = TextSecondary
                )
            }
            if (lesson.isFree) {
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
