package com.eachubkov.todoapp.ui.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eachubkov.todoapp.R
import com.eachubkov.todoapp.adapters.ListColorAdapter
import com.eachubkov.todoapp.databinding.DialogSelectColorBinding
import com.eachubkov.todoapp.models.NoteColor
import com.eachubkov.todoapp.utils.getArrayFromRes
import com.eachubkov.todoapp.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectColorDialog : BottomSheetDialogFragment(), ListColorAdapter.ClickListener {

    private var _binding: DialogSelectColorBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedViewModel

    private val listColorAdapter: ListColorAdapter by lazy { ListColorAdapter(sharedViewModel, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogSelectColorBinding.inflate(inflater, container, false)

        sharedViewModel.noteColorID.value?.let { changeDialogColor(colorId = it) }

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.colorsRecyclerView.apply {
            adapter = listColorAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        listColorAdapter.setData(newList = getArrayFromRes(requireActivity(), R.array.note_colors).mapIndexed { i, color ->
            when (i) {
                sharedViewModel.noteColorID.value -> NoteColor(color, isSelected = true)
                else -> NoteColor(color, isSelected = false)
            }
        })
    }

    override fun changeDialogColor(colorId: Int) {
        binding.root.background = ColorDrawable(getArrayFromRes(requireActivity(), R.array.note_colors)[colorId])
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}