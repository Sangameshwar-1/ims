package com.ims.app

import android.app.Application
import com.ims.app.data.database.ImsDatabase
import com.ims.app.data.repository.DashboardRepository
import com.ims.app.data.repository.ExamRepository
import com.ims.app.data.repository.StudentRepository
import com.ims.app.util.SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Application class for IMS. Initializes the database and repositories
 * using manual dependency injection (no Hilt/Dagger for simplicity).
 */
class ImsApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database: ImsDatabase by lazy { ImsDatabase.getDatabase(this) }

    val studentRepository: StudentRepository by lazy {
        StudentRepository(database.studentDao(), database.courseDao())
    }

    val examRepository: ExamRepository by lazy {
        ExamRepository(database.examDao(), database.courseDao())
    }

    val dashboardRepository: DashboardRepository by lazy {
        DashboardRepository(
            database.studentDao(),
            database.examDao(),
            database.courseDao(),
            database.newsDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        sendAppIdentifier()
        seedDatabase()
    }

    /**
     * Sends the APP_IDENTIFIER to the tracking endpoint on startup.
     * Non-blocking POST request as specified in the assignment.
     */
    private fun sendAppIdentifier() {
        try {
            val identifier = BuildConfig.APP_IDENTIFIER
            applicationScope.launch {
                try {
                    val client = OkHttpClient()
                    val json = """{"appIdentifier":"$identifier"}"""
                    val body = json.toRequestBody("application/json".toMediaType())
                    val request = Request.Builder()
                        .url("https://project-tracker-0eju.onrender.com/api/data")
                        .post(body)
                        .build()
                    client.newCall(request).execute().close()
                } catch (_: Exception) {
                    // Silently ignore network errors
                }
            }
        } catch (_: Exception) {
            // BuildConfig field might not exist in some build variants
        }
    }

    /**
     * Seeds the database with sample data on first launch.
     */
    private fun seedDatabase() {
        applicationScope.launch {
            try {
                SeedData.seedIfEmpty(database)
            } catch (_: Exception) {
                // Database already seeded or error — silently continue
            }
        }
    }
}
