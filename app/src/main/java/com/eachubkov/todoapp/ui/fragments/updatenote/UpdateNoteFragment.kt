package com.eachubkov.todoapp.ui.fragments.updatenote

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eachubkov.todoapp.R
import com.eachubkov.todoapp.data.entity.NotesEntity
import com.eachubkov.todoapp.databinding.FragmentUpdateNoteBinding
import com.eachubkov.todoapp.utils.Constants
import com.eachubkov.todoapp.utils.getArrayFromRes
import com.eachubkov.todoapp.viewmodels.SharedViewModel
import com.eachubkov.todoapp.viewmodels.ToDoViewModel

class UpdateNoteFragment : Fragment() {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val toDoViewModel: ToDoViewModel by viewModels()
    private lateinit var sharedViewModel: SharedViewModel

    private val args by navArgs<UpdateNoteFragmentArgs>()

    private var noteColorId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        binding.args = args

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        sharedViewModel.saveNoteColorId(colorId = args.currentItem.colorId)

        sharedViewModel.noteColorID.observe(viewLifecycleOwner, {
            noteColorId = it
            changeStyleFragment(color = getArrayFromRes(requireActivity(), R.array.note_colors)[it])
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_done -> updateNote()
            R.id.menu_add_image -> openGallery()
            R.id.menu_send -> sendNote()
            R.id.menu_delete -> deleteNote()
            R.id.menu_color ->  findNavController().navigate(R.id.action_updateNoteFragment_to_selectColorDialog)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateNote() {
        toDoViewModel.updateNote(
                NotesEntity(
                        id = args.currentItem.id,
                        title = binding.noteTitleTextView.text.toString(),
                        description = binding.noteDescriptionTextView.text.toString(),
                        image = (binding.noteImageImageView.drawable as BitmapDrawable?)?.bitmap,
                        colorId = noteColorId
                )
        )
        findNavController().navigate(R.id.action_updateNoteFragment_to_listNoteFragment)
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

    private fun openGallery() {
        val galleryIntent = Intent().apply {
            action = Intent.ACTION_PICK
            type = "image/*"
        }
        startActivityForResult(galleryIntent, Constants.REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE){
            binding.noteImageImageView.apply {
                visibility = View.VISIBLE
                setImageURI(data?.data)
            }
        }
    }

    private fun deleteNote() {
        toDoViewModel.deleteNote(note = args.currentItem)
        findNavController().navigate(R.id.action_updateNoteFragment_to_listNoteFragment)
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