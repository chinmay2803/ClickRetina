package com.example.skillforge.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.skillforge.ui.screens.CourseDetailScreen
import com.example.skillforge.ui.screens.HomeScreen
import com.example.skillforge.ui.screens.LessonScreen
import com.example.skillforge.viewmodel.SkillforgeViewModel

object Routes {
    const val HOME = "home"
    const val COURSE_DETAIL = "course/{courseTitle}"
    const val LESSON = "course/{courseTitle}/lesson/{lessonTitle}"

    fun courseDetail(courseTitle: String) =
        "course/${Uri.encode(courseTitle)}"

    fun lesson(courseTitle: String, lessonTitle: String) =
        "course/${Uri.encode(courseTitle)}/lesson/${Uri.encode(lessonTitle)}"
}

@Composable
fun SkillforgeNavGraph(navController: NavHostController = rememberNavController()) {
    // Shared ViewModel across the whole graph — simplest way to avoid
    // re-fetching or threading data through every destination manually.
    val viewModel: SkillforgeViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onCourseClick = { course ->
                    navController.navigate(Routes.courseDetail(course.title))
                }
            )
        }

        composable(
            route = Routes.COURSE_DETAIL,
            arguments = listOf(navArgument("courseTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseTitle = backStackEntry.arguments?.getString("courseTitle").orEmpty()
            CourseDetailScreen(
                viewModel = viewModel,
                courseTitle = courseTitle,
                onBack = { navController.popBackStack() },
                onLessonClick = { lesson ->
                    navController.navigate(Routes.lesson(courseTitle, lesson.title))
                }
            )
        }

        composable(
            route = Routes.LESSON,
            arguments = listOf(
                navArgument("courseTitle") { type = NavType.StringType },
                navArgument("lessonTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseTitle = backStackEntry.arguments?.getString("courseTitle").orEmpty()
            val lessonTitle = backStackEntry.arguments?.getString("lessonTitle").orEmpty()
            LessonScreen(
                viewModel = viewModel,
                courseTitle = courseTitle,
                lessonTitle = lessonTitle,
                onBack = { navController.popBackStack() },
                onLessonClick = { clickedLesson ->
                    navController.navigate(Routes.lesson(courseTitle, clickedLesson.title)) {
                        popUpTo(Routes.courseDetail(courseTitle)) { inclusive = false }
                    }
                }
            )
        }
    }
}
