package com.eachubkov.todoapp.ui.fragments.addnote

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eachubkov.todoapp.R
import com.eachubkov.todoapp.data.entity.NotesEntity
import com.eachubkov.todoapp.databinding.FragmentAddNoteBinding
import com.eachubkov.todoapp.utils.Constants.Companion.REQUEST_CODE
import com.eachubkov.todoapp.utils.getArrayFromRes
import com.eachubkov.todoapp.viewmodels.SharedViewModel
import com.eachubkov.todoapp.viewmodels.ToDoViewModel
import java.io.IOException

class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private val toDoViewModel: ToDoViewModel by viewModels()
    private lateinit var sharedViewModel: SharedViewModel

    private var noteColorId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        sharedViewModel.saveNoteColorId(colorId = noteColorId)

        sharedViewModel.noteColorID.observe(viewLifecycleOwner, {
            noteColorId = it
            changeStyleFragment(color = getArrayFromRes(requireActivity(), R.array.note_colors)[it])
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_done -> addNote()
            R.id.menu_add_image -> openGallery()
            R.id.menu_send -> sendNote()
            R.id.menu_color ->  findNavController().navigate(R.id.action_addNoteFragment_to_selectColorDialog)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNote() {
        toDoViewModel.addNote(
            NotesEntity(
                id = 0,
                title = binding.noteTitleTextView.text.toString(),
                description = binding.noteDescriptionTextView.text.toString(),
                image = (binding.noteImageImageView.drawable as BitmapDrawable?)?.bitmap,
                colorId = noteColorId
            )
        )
        findNavController().navigate(R.id.action_addNoteFragment_to_listNoteFragment)
    }

    private fun openGallery() {
        val galleryIntent = Intent().apply {
            action = Intent.ACTION_PICK
            type = "image/*"
        }
        startActivityForResult(galleryIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            binding.noteImageImageView.apply {
                visibility = View.VISIBLE
                setImageURI(data?.data)
            }
        }
    }

    private fun sendNote() {
        val title = binding.noteTitleTextView.text.toString()
        val description = binding.noteDescriptionTextView.text.toString()
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "$title\n\n$description")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun changeStyleFragment(color: Int) {
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
        binding.root.background = ColorDrawable(color)
    }

    override fun onDestroyView() {
        changeStyleFragment(color = ContextCompat.getColor(requireContext(), R.color.silver))
        super.onDestroyView()
        _binding = null
    }
}