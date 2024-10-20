package com.tamdt.mynotes.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.tamdt.mynotes.MainActivity
import com.tamdt.mynotes.R
import com.tamdt.mynotes.databinding.FragmentEditNoteBinding
import com.tamdt.mynotes.model.Note
import com.tamdt.mynotes.viewmodel.NoteViewModel

class EditNoteFragment : Fragment(R.layout.fragment_edit_note), MenuProvider {

    private var editNoteBinding: FragmentEditNoteBinding? = null
    private val binding get() = editNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote: Note

    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        notesViewModel = (activity as MainActivity).noteViewModel

        currentNote = args.note!!
        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDesc)
        binding.editNoteFab.setOnClickListener{
            val noteTile = binding.editNoteTitle.text.toString().trim()
            val noteDesc = binding.editNoteDesc.text.toString().trim()

            if (noteTile.isNotEmpty()){
                val updatedNote = Note(
                    currentNote.id,
                    noteTile,
                    noteDesc,
                    currentNote.date
                )
                notesViewModel.updateNote(updatedNote)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }else {
                Toast.makeText(context, "Hãy nhập tiêu đề", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun deleteNote() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Xóa ghi chú")
            setMessage("Bạn có chắc chắn muốn xóa ghi chú này?")
            setPositiveButton("Xóa") { _, _ ->
                notesViewModel.deleteNote(currentNote)
                Toast.makeText(context, "Đã xóa ghi chú", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Hủy", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.deleteMenu -> {
                deleteNote()
                true
            } else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editNoteBinding = null
    }

}