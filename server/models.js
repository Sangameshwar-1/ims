const mongoose = require('mongoose');

// --- Student ---
const studentSchema = new mongoose.Schema({
  studentId: { type: String, required: true, unique: true },
  firstName: { type: String, required: true },
  lastName: { type: String, required: true },
  email: { type: String, required: true },
  phone: { type: String, required: true },
  dateOfBirth: String,
  gender: String,
  address: String,
  city: String,
  state: String,
  pinCode: String,
  bloodGroup: String,
  nationality: String,
  religion: String,
  category: String,
  photoUrl: String,
  batchId: Number, // Reference to Batch (keeping as Number to match Android Long IDs temporarily, or could be ObjectId)
  courseId: Number, 
  admissionDate: String,
  previousSchool: String,
  previousGrade: String,
  previousPercentage: String,
  status: { type: String, default: 'Active' },
  createdAt: { type: Number, default: Date.now }
});
// Using an auto-incrementing integer plugin or just relying on MongoDB _id as string. 
// The Android app uses Long for IDs. We'll need to adapt the Android side to use String IDs for MongoDB _id, 
// OR we can generate Long IDs in Mongo. 
// Given the scope of refactoring, changing Long to String in Kotlin is the easiest way to map to MongoDB's _id.
// But to keep it simple without changing the entire Android app's data models (which use Long), 
// we will generate a numeric 'id' field in MongoDB, or we just map `_id` to string in Android. 
// Let's modify Android to use String for IDs later. For now, schemas will just use default MongoDB ObjectIds.

const Student = mongoose.model('Student', studentSchema);

// --- Course ---
const courseSchema = new mongoose.Schema({
  name: { type: String, required: true },
  code: { type: String, required: true },
  duration: String,
  description: String,
  department: String,
  status: { type: String, default: 'Active' },
  createdAt: { type: Number, default: Date.now }
});
const Course = mongoose.model('Course', courseSchema);

// --- Batch ---
const batchSchema = new mongoose.Schema({
  courseId: String, // String to match Mongo ObjectId
  name: { type: String, required: true },
  startYear: String,
  endYear: String,
  currentSemester: Number,
  capacity: Number,
  status: { type: String, default: 'Active' },
  createdAt: { type: Number, default: Date.now }
});
const Batch = mongoose.model('Batch', batchSchema);

// --- Subject ---
const subjectSchema = new mongoose.Schema({
  courseId: String,
  name: { type: String, required: true },
  code: { type: String, required: true },
  credits: Number,
  semester: Number,
  type: String,
  status: { type: String, default: 'Active' },
  createdAt: { type: Number, default: Date.now }
});
const Subject = mongoose.model('Subject', subjectSchema);

// --- Exam ---
const examSchema = new mongoose.Schema({
  name: { type: String, required: true },
  type: String,
  subjectId: String,
  courseId: String,
  batchId: String,
  date: String,
  startTime: String,
  endTime: String,
  totalMarks: Number,
  passingMarks: Number,
  description: String,
  venue: String,
  status: { type: String, default: 'Scheduled' },
  createdAt: { type: Number, default: Date.now }
});
const Exam = mongoose.model('Exam', examSchema);

// --- Exam Result ---
const examResultSchema = new mongoose.Schema({
  examId: String,
  studentId: String,
  marksObtained: Number,
  grade: String,
  status: String,
  remarks: String,
  createdAt: { type: Number, default: Date.now }
});
const ExamResult = mongoose.model('ExamResult', examResultSchema);

// --- News ---
const newsSchema = new mongoose.Schema({
  title: { type: String, required: true },
  content: { type: String, required: true },
  author: String,
  category: String,
  isPublished: Boolean,
  publishedAt: String,
  createdAt: { type: Number, default: Date.now }
});
const News = mongoose.model('News', newsSchema);

module.exports = {
  Student, Course, Batch, Subject, Exam, ExamResult, News
};
