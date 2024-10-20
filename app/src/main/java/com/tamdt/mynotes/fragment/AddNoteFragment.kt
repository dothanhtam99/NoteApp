package com.tamdt.mynotes.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.tamdt.mynotes.MainActivity
import com.tamdt.mynotes.R
import com.tamdt.mynotes.databinding.FragmentAddNoteBinding
import com.tamdt.mynotes.model.Note
import com.tamdt.mynotes.viewmodel.NoteViewModel
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {
    private var addNodeBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNodeBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var addNoteView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addNodeBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        notesViewModel = (activity as MainActivity).noteViewModel
        addNoteView = view
    }

    private fun saveNote(view: View){
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        if (noteTitle.isNotEmpty()){
            val note = Note(0, noteTitle, noteDesc, currentDate)
            Log.d("tamdt", "saveNote: date" + currentDate)
            notesViewModel.addNote(note)

            Toast.makeText(addNoteView.context, "Đã Lưu", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        }else{
            Toast.makeText(addNoteView.context, "Hãy nhập tiêu đề", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu -> {
                saveNote(addNoteView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addNodeBinding = null
    }

}