package com.ims.app.data.repository

import com.ims.app.data.dao.CourseDao
import com.ims.app.data.dao.ExamDao
import com.ims.app.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for examination-related data operations.
 * Handles exam creation, result recording, and statistical reporting.
 */
class ExamRepository(
    private val examDao: ExamDao,
    private val courseDao: CourseDao
) {
    // ─── Exams ──────────────────────────────────────────────────

    val allExams: Flow<List<Exam>> = examDao.getAllExams()
    val totalExamCount: Flow<Int> = examDao.getTotalExamCount()
    val upcomingExamCount: Flow<Int> = examDao.getUpcomingExamCount()
    val upcomingExams: Flow<List<Exam>> = examDao.getUpcomingExams()

    suspend fun insertExam(exam: Exam): Long = examDao.insertExam(exam)
    suspend fun updateExam(exam: Exam) = examDao.updateExam(exam)
    suspend fun deleteExam(exam: Exam) = examDao.deleteExam(exam)
    suspend fun getExamById(id: Long): Exam? = examDao.getExamById(id)

    fun getExamsByStatus(status: String): Flow<List<Exam>> = examDao.getExamsByStatus(status)
    fun getExamsByCourse(courseId: Long): Flow<List<Exam>> = examDao.getExamsByCourse(courseId)
    fun getExamsByBatch(batchId: Long): Flow<List<Exam>> = examDao.getExamsByBatch(batchId)
    fun getExamsBySubject(subjectId: Long): Flow<List<Exam>> = examDao.getExamsBySubject(subjectId)
    fun searchExams(query: String): Flow<List<Exam>> = examDao.searchExams(query)

    // ─── Results ────────────────────────────────────────────────

    suspend fun insertResult(result: ExamResult): Long = examDao.insertResult(result)
    suspend fun insertResults(results: List<ExamResult>) = examDao.insertResults(results)
    suspend fun updateResult(result: ExamResult) = examDao.updateResult(result)
    suspend fun deleteResult(result: ExamResult) = examDao.deleteResult(result)

    fun getResultsByExam(examId: Long): Flow<List<ExamResult>> = examDao.getResultsByExam(examId)
    fun getResultsByStudent(studentId: Long): Flow<List<ExamResult>> = examDao.getResultsByStudent(studentId)
    suspend fun getResult(examId: Long, studentId: Long): ExamResult? = examDao.getResult(examId, studentId)

    // ─── Statistics ─────────────────────────────────────────────

    suspend fun getExamStats(examId: Long): ExamStats {
        val avg = examDao.getAverageMarks(examId) ?: 0.0
        val highest = examDao.getHighestMarks(examId) ?: 0.0
        val lowest = examDao.getLowestMarks(examId) ?: 0.0
        val passCount = examDao.getPassCount(examId)
        val totalAttempted = examDao.getTotalAttempted(examId)
        val passPercentage = if (totalAttempted > 0) (passCount.toDouble() / totalAttempted) * 100 else 0.0

        return ExamStats(
            averageScore = avg,
            passPercentage = passPercentage,
            highestScore = highest,
            lowestScore = lowest
        )
    }

    // ─── Courses & Subjects (for exam creation dropdowns) ──────

    val activeCourses: Flow<List<Course>> = courseDao.getActiveCourses()
    val activeSubjects: Flow<List<Subject>> = courseDao.getAllActiveSubjects()
    val activeBatches: Flow<List<Batch>> = courseDao.getAllActiveBatches()

    fun getSubjectsByCourse(courseId: Long): Flow<List<Subject>> = courseDao.getSubjectsByCourse(courseId)
    fun getBatchesByCourse(courseId: Long): Flow<List<Batch>> = courseDao.getBatchesByCourse(courseId)
    suspend fun getCourseById(id: Long): Course? = courseDao.getCourseById(id)
    suspend fun getSubjectById(id: Long): Subject? = courseDao.getSubjectById(id)
}
