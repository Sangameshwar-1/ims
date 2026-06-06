package com.ims.app.data.repository

import com.ims.app.data.dao.CourseDao
import com.ims.app.data.dao.StudentDao
import com.ims.app.data.model.Batch
import com.ims.app.data.model.Course
import com.ims.app.data.model.Guardian
import com.ims.app.data.model.Student
import kotlinx.coroutines.flow.Flow

/**
 * Repository for student-related data operations.
 * Mediates between the DAO layer and ViewModels, providing a clean API.
 */
class StudentRepository(
    private val studentDao: StudentDao,
    private val courseDao: CourseDao
) {
    // ─── Students ───────────────────────────────────────────────

    val allStudents: Flow<List<Student>> = studentDao.getAllStudents()
    val totalCount: Flow<Int> = studentDao.getTotalStudentCount()
    val activeCount: Flow<Int> = studentDao.getActiveStudentCount()

    suspend fun insertStudent(student: Student): Long = studentDao.insertStudent(student)
    suspend fun updateStudent(student: Student) = studentDao.updateStudent(student)
    suspend fun deleteStudent(student: Student) = studentDao.deleteStudent(student)
    suspend fun getStudentById(id: Long): Student? = studentDao.getStudentById(id)
    suspend fun getStudentByStudentId(studentId: String): Student? = studentDao.getStudentByStudentId(studentId)

    fun getStudentsByStatus(status: String): Flow<List<Student>> = studentDao.getStudentsByStatus(status)
    fun getStudentsByBatch(batchId: Long): Flow<List<Student>> = studentDao.getStudentsByBatch(batchId)
    fun getStudentsByCourse(courseId: Long): Flow<List<Student>> = studentDao.getStudentsByCourse(courseId)
    fun searchStudents(query: String): Flow<List<Student>> = studentDao.searchStudents(query)
    fun getRecentAdmissionCount(since: String): Flow<Int> = studentDao.getRecentAdmissionCount(since)

    fun advancedSearch(
        status: String = "",
        courseId: Long = 0,
        batchId: Long = 0,
        category: String = "",
        gender: String = ""
    ): Flow<List<Student>> = studentDao.advancedSearch(status, courseId, batchId, category, gender)

    /**
     * Generates a unique student ID based on current year and count.
     */
    suspend fun generateStudentId(): String {
        val year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val count = (totalCount as kotlinx.coroutines.flow.StateFlow?)?.value ?: 0
        return "STU-$year-${String.format("%04d", count + 1)}"
    }

    // ─── Guardians ──────────────────────────────────────────────

    suspend fun insertGuardian(guardian: Guardian): Long = studentDao.insertGuardian(guardian)
    suspend fun updateGuardian(guardian: Guardian) = studentDao.updateGuardian(guardian)
    suspend fun deleteGuardian(guardian: Guardian) = studentDao.deleteGuardian(guardian)
    fun getGuardiansByStudent(studentId: Long): Flow<List<Guardian>> = studentDao.getGuardiansByStudent(studentId)
    suspend fun deleteGuardiansByStudent(studentId: Long) = studentDao.deleteGuardiansByStudent(studentId)

    // ─── Courses & Batches (for dropdowns in admission forms) ──

    val activeCourses: Flow<List<Course>> = courseDao.getActiveCourses()
    val activeBatches: Flow<List<Batch>> = courseDao.getAllActiveBatches()

    suspend fun getCourseById(id: Long): Course? = courseDao.getCourseById(id)
    fun getBatchesByCourse(courseId: Long): Flow<List<Batch>> = courseDao.getBatchesByCourse(courseId)
}
