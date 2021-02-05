package com.eachubkov.todoapp.bindingadapters

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.eachubkov.todoapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapter {
    companion object {
        @BindingAdapter("navigateToAddNoteFragment")
        @JvmStatic fun navigateToAddNoteFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if(navigate) {
                    view.findNavController().navigate(R.id.action_listNoteFragment_to_addNoteFragment)
                }
            }
        }
        @BindingAdapter("setViewVisibility")
        @JvmStatic fun setViewVisibility(view: View, visible: Boolean) {
            when (view) {
                is ImageView -> view.isVisible = visible
                is TextView -> view.isVisible = visible
            }
        }
        @BindingAdapter("loadImage")
        @JvmStatic fun loadImage(view: ImageView, img: Bitmap?) {
            if (!view.isVisible) view.isVisible = true
            view.load(img)
        }
    }
}