package com.ims.app.data.network

import com.ims.app.data.model.*
import retrofit2.http.*

interface StudentApi {
    @GET("students")
    suspend fun getStudents(): List<Student>

    @GET("students/{id}")
    suspend fun getStudentById(@Path("id") id: String): Student

    @POST("students")
    suspend fun createStudent(@Body student: Student): Student

    @PUT("students/{id}")
    suspend fun updateStudent(@Path("id") id: String, @Body student: Student): Student

    @DELETE("students/{id}")
    suspend fun deleteStudent(@Path("id") id: String)
}

interface ExamApi {
    @GET("exams")
    suspend fun getExams(): List<Exam>

    @POST("exams")
    suspend fun createExam(@Body exam: Exam): Exam

    @PUT("exams/{id}")
    suspend fun updateExam(@Path("id") id: String, @Body exam: Exam): Exam

    @DELETE("exams/{id}")
    suspend fun deleteExam(@Path("id") id: String)

    @GET("results")
    suspend fun getExamResults(): List<ExamResult>

    @POST("results")
    suspend fun createExamResult(@Body result: ExamResult): ExamResult
}

interface CourseApi {
    @GET("courses")
    suspend fun getCourses(): List<Course>

    @GET("batches")
    suspend fun getBatches(): List<Batch>
}

// Data class to hold the parsed JSON from the dashboard stats endpoint
data class DashboardStatsResponse(
    val totalStudents: Int,
    val activeCourses: Int,
    val upcomingExams: Int,
    val pendingAdmissions: Int
)

interface DashboardApi {
    @GET("dashboard/stats")
    suspend fun getDashboardStats(): DashboardStatsResponse

    @GET("news")
    suspend fun getNews(): List<News>
}
