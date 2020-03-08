package eu.chessout.v2.ui.dashboard02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import eu.chessout.v2.R

class Dashboard02Fragment : Fragment() {

    companion object {
        fun newInstance() = Dashboard02Fragment()
    }

    private lateinit var viewModel: Dashboard02ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(Dashboard02ViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard02, container, false)
        val textView: TextView = root.findViewById(R.id.text_detail_2)
        viewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
/*
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(Dashboard02ViewModel::class.java)
        // TODO: Use the ViewModel
    }*/


}
