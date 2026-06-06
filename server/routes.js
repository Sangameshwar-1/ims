const express = require('express');
const { Student, Course, Batch, Subject, Exam, ExamResult, News } = require('./models');

const router = express.Router();

// Helper function to create generic CRUD routes for a Mongoose model
const createCrudRoutes = (model, basePath) => {
  // GET all
  router.get(`/${basePath}`, async (req, res) => {
    try {
      const items = await model.find().sort({ _id: -1 });
      res.json(items);
    } catch (err) {
      res.status(500).json({ error: err.message });
    }
  });

  // GET by ID
  router.get(`/${basePath}/:id`, async (req, res) => {
    try {
      const item = await model.findById(req.params.id);
      if (!item) return res.status(404).json({ error: 'Not found' });
      res.json(item);
    } catch (err) {
      res.status(500).json({ error: err.message });
    }
  });

  // POST create
  router.post(`/${basePath}`, async (req, res) => {
    try {
      const newItem = new model(req.body);
      const saved = await newItem.save();
      res.status(201).json(saved);
    } catch (err) {
      res.status(400).json({ error: err.message });
    }
  });

  // PUT update
  router.put(`/${basePath}/:id`, async (req, res) => {
    try {
      const updated = await model.findByIdAndUpdate(req.params.id, req.body, { new: true });
      if (!updated) return res.status(404).json({ error: 'Not found' });
      res.json(updated);
    } catch (err) {
      res.status(400).json({ error: err.message });
    }
  });

  // DELETE
  router.delete(`/${basePath}/:id`, async (req, res) => {
    try {
      const deleted = await model.findByIdAndDelete(req.params.id);
      if (!deleted) return res.status(404).json({ error: 'Not found' });
      res.json({ message: 'Deleted successfully' });
    } catch (err) {
      res.status(500).json({ error: err.message });
    }
  });
};

// Create routes for all models
createCrudRoutes(Student, 'students');
createCrudRoutes(Course, 'courses');
createCrudRoutes(Batch, 'batches');
createCrudRoutes(Subject, 'subjects');
createCrudRoutes(Exam, 'exams');
createCrudRoutes(ExamResult, 'results');
createCrudRoutes(News, 'news');

// Specific Dashboard Stats Route
router.get('/dashboard/stats', async (req, res) => {
  try {
    const totalStudents = await Student.countDocuments({ status: 'Active' });
    const activeCourses = await Course.countDocuments({ status: 'Active' });
    const upcomingExams = await Exam.countDocuments({ status: 'Scheduled' });
    
    // We don't have an Admission model yet, so using a dummy value or query students created recently
    const pendingAdmissions = 0; 

    res.json({
      totalStudents,
      activeCourses,
      upcomingExams,
      pendingAdmissions
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
