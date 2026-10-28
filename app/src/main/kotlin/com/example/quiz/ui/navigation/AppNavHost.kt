package com.example.quiz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quiz.storage.Storage
import com.example.quiz.ui.admin.AdminScreen
import com.example.quiz.ui.admin.AttemptsScreen
import com.example.quiz.ui.admin.BankQuestionScreen
import com.example.quiz.ui.admin.BankQuestionsScreen
import com.example.quiz.ui.admin.BankTopicsScreen
import com.example.quiz.ui.admin.ExportScreen
import com.example.quiz.ui.admin.SettingsScreen
import com.example.quiz.ui.login.LoginScreen
import com.example.quiz.ui.quiz.QuizScreen
import com.example.quiz.ui.quiz.QuizViewModel
import com.example.quiz.ui.student.HistoryScreen
import com.example.quiz.ui.student.ReportScreen
import com.example.quiz.ui.student.TopicsScreen

@Composable
fun AppNavHost(storage: Storage) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginRoute) {
        composable<LoginRoute> {
            LoginScreen(
                onStudentLogin = { login -> navController.navigate(TopicsRoute(login)) },
                onAdminLogin = { navController.navigate(AdminRoute) },
            )
        }

        composable<TopicsRoute> { entry ->
            val route = entry.toRoute<TopicsRoute>()
            TopicsScreen(
                storage = storage,
                login = route.login,
                onTopicClick = { topicId -> navController.navigate(QuizRoute(route.login, topicId)) },
                onHistoryClick = { navController.navigate(HistoryRoute(route.login)) },
                onBack = { navController.popBackStack() },
            )
        }

        composable<QuizRoute> { entry ->
            val route = entry.toRoute<QuizRoute>()
            val quizViewModel = viewModel { QuizViewModel(storage, route.login, route.topicId) }
            QuizScreen(
                viewModel = quizViewModel,
                onFinished = { attemptId ->
                    navController.navigate(ReportRoute(attemptId)) {
                        popUpTo<QuizRoute> { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable<ReportRoute> { entry ->
            val route = entry.toRoute<ReportRoute>()
            ReportScreen(
                storage = storage,
                attemptId = route.attemptId,
                isAdmin = route.isAdmin,
                onRetry = { login, topicId ->
                    navController.navigate(QuizRoute(login, topicId)) {
                        popUpTo<ReportRoute> { inclusive = true }
                    }
                },
                onTopicsClick = { login ->
                    if (!navController.popBackStack<TopicsRoute>(inclusive = false)) {
                        navController.navigate(TopicsRoute(login))
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable<HistoryRoute> { entry ->
            val route = entry.toRoute<HistoryRoute>()
            HistoryScreen(
                storage = storage,
                login = route.login,
                onAttemptClick = { attemptId -> navController.navigate(ReportRoute(attemptId)) },
                onBack = { navController.popBackStack() },
            )
        }

        composable<AdminRoute> {
            AdminScreen(
                onBankClick = { navController.navigate(BankTopicsRoute) },
                onSettingsClick = { navController.navigate(SettingsRoute) },
                onAttemptsClick = { navController.navigate(AttemptsRoute) },
                onExportClick = { navController.navigate(ExportRoute) },
                onBack = { navController.popBackStack() },
            )
        }

        composable<BankTopicsRoute> {
            BankTopicsScreen(
                storage = storage,
                onTopicClick = { topicId -> navController.navigate(BankQuestionsRoute(topicId)) },
                onBack = { navController.popBackStack() },
            )
        }

        composable<BankQuestionsRoute> { entry ->
            val route = entry.toRoute<BankQuestionsRoute>()
            BankQuestionsScreen(
                storage = storage,
                topicId = route.topicId,
                onQuestionClick = { questionId -> navController.navigate(BankQuestionRoute(questionId)) },
                onBack = { navController.popBackStack() },
            )
        }

        composable<BankQuestionRoute> { entry ->
            val route = entry.toRoute<BankQuestionRoute>()
            BankQuestionScreen(
                storage = storage,
                questionId = route.questionId,
                onBack = { navController.popBackStack() },
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(storage = storage, onBack = { navController.popBackStack() })
        }

        composable<AttemptsRoute> {
            AttemptsScreen(
                storage = storage,
                onAttemptClick = { attemptId -> navController.navigate(ReportRoute(attemptId, isAdmin = true)) },
                onBack = { navController.popBackStack() },
            )
        }

        composable<ExportRoute> {
            ExportScreen(storage = storage, onBack = { navController.popBackStack() })
        }
    }
}
