package com.tamdt.mynotes.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.tamdt.mynotes.R
import com.tamdt.mynotes.database.NoteDatabase
import com.tamdt.mynotes.databinding.ActivityCalendarBinding
import com.tamdt.mynotes.model.Note
import com.tamdt.mynotes.reponsitory.NoteRespository
import com.tamdt.mynotes.viewmodel.NoteViewModel
import com.tamdt.mynotes.viewmodel.NoteViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var notesViewModel: NoteViewModel
    private lateinit var noteViewModel: NoteViewModel
    private var listNote = ArrayList<Note>()
    private var listCalendar = ArrayList<Calendar>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        initData()
        initView()
    }

    private fun setupViewModel() {
        val noteRespository = NoteRespository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRespository)
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }

    @SuppressLint("SuspiciousIndentation")
    private fun initData() {
        notesViewModel = noteViewModel
        val lifecycleOwner: LifecycleOwner = this
        notesViewModel.getAllNotes().observe(lifecycleOwner) { note ->
            if (note.isNotEmpty()) {
                for (i in note.indices) {
                    listNote.add(note[i])
                    val calendar = getCalendarFromDateString(listNote[i].date)
                    listCalendar.add(calendar!!)
                    updateCalendarView()
                }
            } else {
                binding.llContentNote.visibility = View.GONE
            }

        }
    }

    private fun updateCalendarView() {

        val events = ArrayList<CalendarDay>()
        for (i in 0 until listCalendar.size) {
            val calendarDay = CalendarDay(listCalendar[i])
            calendarDay.imageResource = R.drawable.sample_icon_2
            events.add(calendarDay)
        }

        binding.calendarView.setCalendarDays(events)

        binding.calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                if (listCalendar.size > 0) {
                    val spyCalendar = isCalendarInList(eventDay.calendar, listCalendar)
                    if (spyCalendar) {
                        binding.llContentNote.visibility = View.VISIBLE
                        val foundNote = findNoteByDate(eventDay.calendar, listNote)
                        if (foundNote != null) {
                            binding.llContentNote.visibility = View.VISIBLE
                            binding.tvTitle.text = foundNote.noteTitle
                            binding.tvDes.text = foundNote.noteDesc
                        } else {
                            binding.llContentNote.visibility = View.GONE
                        }

                    } else {
                        binding.llContentNote.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun initView() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun getCalendarFromDateString(dateString: String): Calendar? {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun isCalendarInList(calendar: Calendar, calendarList: ArrayList<Calendar>): Boolean {
        return calendarList.any { it.timeInMillis == calendar.timeInMillis }
    }

    fun findNoteByDate(calendar: Calendar, notes: List<Note>): Note? {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = sdf.format(calendar.time)
        return notes.find { it.date == dateString }
    }
}