package com.example.skillforge.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.skillforge.data.Category
import com.example.skillforge.data.Course
import com.example.skillforge.ui.theme.*
import com.example.skillforge.viewmodel.SkillforgeViewModel
import com.example.skillforge.viewmodel.UiState

@Composable
fun HomeScreen(
    viewModel: SkillforgeViewModel,
    onCourseClick: (Course) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        when (val s = state) {
            is UiState.Loading -> LoadingState()
            is UiState.Error -> ErrorState(message = s.message, onRetry = viewModel::fetchData)
            is UiState.Success -> HomeContent(categories = s.categories, onCourseClick = onCourseClick)
        }
    }
}

@Composable
private fun HomeContent(categories: List<Category>, onCourseClick: (Course) -> Unit) {
    val popularCourses = remember(categories) { categories.flatMap { it.courses } }
    
    // Dynamic user avatar loaded from API data as requested
    val userAvatarUrl = remember(categories) {
        categories.firstOrNull()?.courses?.firstOrNull()?.instructor?.avatarUrl
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Welcome and Header area (equivalent to padding: 42px 20px 14px)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 42.dp, bottom = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Welcome back",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        ),
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Find your next skill",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            letterSpacing = (-0.3).sp
                        ),
                        color = TextPrimary
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Notification bell circle
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Divider, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFF5C5C54),
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // Circular profile image loaded from API
                    AsyncImage(
                        model = userAvatarUrl,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Divider)
                    )
                }
            }
        }

        // Search Bar (padding: 0px 20px 16px)
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                SearchBar()
            }
        }

        // Categories section
        item {
            SectionHeader(title = "Categories", onSeeAll = { /* TODO */ })
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(category)
                }
            }
        }

        // Popular Courses section
        item {
            Spacer(Modifier.height(10.dp))
            SectionHeader(title = "Popular courses", onSeeAll = { /* TODO */ })
        }
        
        items(popularCourses) { course ->
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                CourseCard(course = course, onClick = { onCourseClick(course) })
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SearchBar() {
    var query by remember { mutableStateOf("") }
    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        placeholder = { 
            Text(
                "Search courses, topics…", 
                fontSize = 15.sp, 
                color = Color(0xFFA6A49C),
                fontWeight = FontWeight.Normal
            ) 
        },
        leadingIcon = { 
            Icon(
                Icons.Filled.Search, 
                contentDescription = null, 
                tint = Color(0xFFA6A49C),
                modifier = Modifier.size(18.dp)
            ) 
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Divider,
            unfocusedBorderColor = Divider
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = title, 
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            ),
            color = TextPrimary
        )
        Text(
            text = "See all", 
            color = Color(0xFF0FB5A4), 
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            ),
            modifier = Modifier.clickable { onSeeAll() }
        )
    }
}

@Composable
private fun CategoryCard(category: Category) {
    val categoryColor = remember(category.iconColor) {
        try {
            Color(android.graphics.Color.parseColor(category.iconColor))
        } catch (e: Exception) {
            Teal
        }
    }

    Box(
        modifier = Modifier
            .width(148.dp)
            .height(144.dp)
            .background(Color.White, RoundedCornerShape(18.dp))
            .border(1.dp, Divider, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column {
            // Icon container with 16% opacity background
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(categoryColor.copy(alpha = 0.16f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Small inner colored square matching HTML mockup
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(categoryColor, RoundedCornerShape(6.dp))
                )
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = category.name, 
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 17.sp
                ), 
                maxLines = 2,
                color = TextPrimary
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = "${category.courses.size} courses",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp
                ),
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun CourseCard(course: Course, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .border(1.dp, Divider, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = course.thumbnailUrl,
                contentDescription = course.title,
                modifier = Modifier
                    .size(width = 98.dp, height = 74.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                LevelBadge(level = course.level)
                Spacer(Modifier.height(3.dp))
                Text(
                    text = course.title, 
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        lineHeight = 19.sp
                    ), 
                    maxLines = 2,
                    color = TextPrimary
                )
                Text(
                    text = course.instructor.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    ),
                    color = TextSecondary
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Star rating row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFF5A623),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${course.rating}", 
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            ),
                            color = Color(0xFF6B6B62)
                        )
                    }

                    // Duration row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Drawing custom clock icon matching outline bell stroke
                        Icon(
                            imageVector = Icons.Outlined.AccessTime, // replace with custom clock or standard outlined clock
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${course.durationHours}h", 
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            ),
                            color = Color(0xFF6B6B62)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LevelBadge(level: String) {
    val isIntermediate = level.equals("Intermediate", ignoreCase = true)
    val badgeBg = if (isIntermediate) IntermediateBadge else BeginnerBadge
    val badgeText = if (isIntermediate) IntermediateBadgeText else BeginnerBadgeText

    Box(
        modifier = Modifier
            .background(badgeBg, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = level.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 0.4.sp
            ),
            color = badgeText
        )
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Teal)
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Couldn't load courses", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(message, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = Teal)) {
            Text("Retry")
        }
    }
}
