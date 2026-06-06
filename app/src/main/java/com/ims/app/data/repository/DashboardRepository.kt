package com.ims.app.data.repository

import com.ims.app.data.dao.CourseDao
import com.ims.app.data.dao.ExamDao
import com.ims.app.data.dao.NewsDao
import com.ims.app.data.dao.StudentDao
import com.ims.app.data.model.News
import kotlinx.coroutines.flow.Flow

/**
 * Repository aggregating dashboard-level data from multiple DAOs.
 * Provides counts, recent activity, and news for the dashboard overview.
 */
class DashboardRepository(
    private val studentDao: StudentDao,
    private val examDao: ExamDao,
    private val courseDao: CourseDao,
    private val newsDao: NewsDao
) {
    val totalStudents: Flow<Int> = studentDao.getTotalStudentCount()
    val activeStudents: Flow<Int> = studentDao.getActiveStudentCount()
    val activeCourseCount: Flow<Int> = courseDao.getActiveCourseCount()
    val upcomingExamCount: Flow<Int> = examDao.getUpcomingExamCount()
    val totalExamCount: Flow<Int> = examDao.getTotalExamCount()
    val latestNews: Flow<List<News>> = newsDao.getLatestNews(5)

    fun getUpcomingExams() = examDao.getUpcomingExams()
    fun getRecentStudents() = studentDao.getAllStudents()
}
