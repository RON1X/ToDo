package com.eachubkov.todoapp.ui.fragments.listnote

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eachubkov.todoapp.R
import com.eachubkov.todoapp.adapters.ListNoteAdapter
import com.eachubkov.todoapp.databinding.FragmentListNoteBinding
import com.eachubkov.todoapp.utils.GridAutoFitStaggeredLayoutManager
import com.eachubkov.todoapp.utils.getArrayFromRes
import com.eachubkov.todoapp.utils.hideKeyboard
import com.eachubkov.todoapp.viewmodels.SharedViewModel
import com.eachubkov.todoapp.viewmodels.ToDoViewModel
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListNoteFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding : FragmentListNoteBinding? = null
    private val binding get() = _binding!!

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private val listNoteAdapter: ListNoteAdapter by lazy { ListNoteAdapter(requireActivity(), toDoViewModel) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListNoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        toDoViewModel.getAllNotes.observe(viewLifecycleOwner, { noteList ->
            sharedViewModel.checkEmptyDatabase(listNotes = noteList)
            listNoteAdapter.setNoteList(newNoteList = noteList)
            listNoteAdapter.setNoteColors(newColorList = noteList.map {
                getArrayFromRes(requireActivity(), R.array.note_colors)[it.colorId]
            })
        })

        setupRecyclerView()

        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.listNoteRecyclerView.apply {
            adapter = listNoteAdapter
            layoutManager = GridAutoFitStaggeredLayoutManager(requireContext(), columnWidthDp = 600, maxColumns = 3)
            itemAnimator = SlideInUpAnimator().apply { addDuration = 200 }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_note_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchInDatabase(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchInDatabase(newText)
        return true
    }

    private fun searchInDatabase(query: String?) {
        query?.let { searchQuery ->
            toDoViewModel.searchNotes(searchQuery = "%$searchQuery%").observe(viewLifecycleOwner, { list ->
                list?.let { listNoteAdapter.setNoteList(newNoteList = it) }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> toDoViewModel.deleteAllNotes()
            R.id.menu_dark_theme -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            R.id.menu_light_theme -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            R.id.menu_system_theme -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listNoteAdapter.clearContextualActionMode()
    }
}
