package com.eachubkov.todoapp.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eachubkov.todoapp.R
import com.eachubkov.todoapp.data.entity.NotesEntity
import com.eachubkov.todoapp.databinding.ItemNoteBinding
import com.eachubkov.todoapp.ui.fragments.listnote.ListNoteFragmentDirections
import com.eachubkov.todoapp.utils.MyDiffUtil
import com.eachubkov.todoapp.viewmodels.ToDoViewModel

class ListNoteAdapter(
     private val requireActivity: FragmentActivity,
     private val toDoViewModel: ToDoViewModel
): RecyclerView.Adapter<ListNoteAdapter.ViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private lateinit var actionMode: ActionMode
    private lateinit var rootView: View

    private var noteList = emptyList<NotesEntity>()
    private var colorList = emptyList<Int>()
    private var selectedList = arrayListOf<NotesEntity>()
    private var viewHolders = arrayListOf<ViewHolder>()

    class ViewHolder(val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(currentNote: NotesEntity, currentColor: Int) {
            binding.note = currentNote
            binding.color = currentColor
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(parent)

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentNote = noteList[position], currentColor = colorList[position])
        viewHolders.add(holder)
        rootView = holder.itemView.rootView

        holder.binding.noteItemLayout.setOnClickListener {
            if (multiSelection) {
                changeItem(holder, currentNote = noteList[position], noteColor = colorList[position])
            } else {
                val action = ListNoteFragmentDirections.actionListNoteFragmentToUpdateNoteFragment(currentItem = noteList[position])
                holder.itemView.findNavController().navigate(action)
            }
        }

        holder.binding.noteItemLayout.setOnLongClickListener {
            if (!multiSelection) {
                requireActivity.startActionMode(this)
                changeItem(holder, currentNote = noteList[position], noteColor = colorList[position])
            }
            multiSelection = !multiSelection
            multiSelection
        }
    }

    fun setNoteList(newNoteList: List<NotesEntity>) {
        val diffUtil = MyDiffUtil(noteList, newNoteList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        noteList = newNoteList
        diffResult.dispatchUpdatesTo(this)
    }

    fun setNoteColors(newColorList: List<Int>) {
        colorList = newColorList
    }

    /** ACTION MODE **/

    private fun changeItem(holder: ViewHolder, currentNote: NotesEntity, noteColor: Int) {
        if (selectedList.contains(currentNote)) {
            selectedList.remove(currentNote)
            changeItemStyle(holder, backgroundColor = noteColor, selectedColor = R.color.noteStrokeColor)
        } else {
            selectedList.add(currentNote)
            changeItemStyle(holder, backgroundColor = noteColor, selectedColor = R.color.noteSelectedStrokeColor)
        }
        changeActionModeTitle()
    }

    private fun changeItemStyle(holder: ViewHolder, backgroundColor: Int, selectedColor: Int) {
        holder.binding.noteItemLayout.apply {
            setCardBackgroundColor(backgroundColor)
            strokeColor = ContextCompat.getColor(requireActivity, selectedColor)
        }
    }

    private fun changeActionModeTitle() {
        when (selectedList.size) {
            0 -> actionMode.finish()
            1 -> actionMode.title = selectedList.size.toString()
            else -> actionMode.title = selectedList.size.toString()
        }
    }

    private fun changeStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.selected_note_menu, menu)
        actionMode = mode!!
        changeStatusBarColor(color = R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = true

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_delete) {
            selectedList.forEach {
                toDoViewModel.deleteNote(note = it)
            }
            multiSelection = false
            selectedList.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        viewHolders.forEachIndexed { index, viewHolder ->
            changeItemStyle(holder = viewHolder, backgroundColor = colorList[index], selectedColor = R.color.noteStrokeColor)
        }
        multiSelection = false
        selectedList.clear()
        changeStatusBarColor(R.color.statusBarColor)
    }

    fun clearContextualActionMode() {
        if (this::actionMode.isInitialized) {
            actionMode.finish()
        }
    }
}