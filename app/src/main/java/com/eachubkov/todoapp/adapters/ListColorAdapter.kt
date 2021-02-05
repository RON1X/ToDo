package com.eachubkov.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eachubkov.todoapp.databinding.ItemColorBinding
import com.eachubkov.todoapp.models.NoteColor
import com.eachubkov.todoapp.ui.dialog.SelectColorDialog
import com.eachubkov.todoapp.utils.MyDiffUtil
import com.eachubkov.todoapp.viewmodels.SharedViewModel

class ListColorAdapter(
        private val sharedViewModel: SharedViewModel,
        private val selectColorDialog: SelectColorDialog
) : RecyclerView.Adapter<ListColorAdapter.ViewHolder>() {

    private var list = emptyList<NoteColor>()
    private var viewHolders = arrayListOf<ViewHolder>()

    private var selectedColorId : Int = sharedViewModel.noteColorID.value ?: 0

    class ViewHolder(val binding: ItemColorBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(currentNoteColor: NoteColor) {
            binding.noteColor = currentNoteColor
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemColorBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentNoteColor = list[position])

        viewHolders.add(holder)

        holder.binding.colorCardView.setOnClickListener {
            viewHolders[selectedColorId].binding.colorImageView.visibility = View.INVISIBLE
            viewHolders[position].binding.colorImageView.visibility = View.VISIBLE
            selectedColorId = position
            sharedViewModel.saveNoteColorId(colorId = position)
            selectColorDialog.changeDialogColor(colorId = position)
        }
    }

    override fun getItemCount(): Int = list.size

    fun setData(newList: List<NoteColor>) {
        val diffUtil = MyDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    interface ClickListener {
        fun changeDialogColor(colorId: Int)
    }
}

