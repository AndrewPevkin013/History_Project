package com.example.history_project_11

import android.view.View
import com.yandex.runtime.ui_view.ViewProvider

class CustomViewProvider(private val view: View) : ViewProvider(view) {
  fun getView(): View {
    return view
  }

  fun getZIndex(): Float {
    return 0f
  }
}
