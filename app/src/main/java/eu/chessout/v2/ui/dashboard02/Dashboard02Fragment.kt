package eu.chessout.v2.ui.dashboard02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import eu.chessout.v2.R
import kotlinx.android.synthetic.main.fragment_dashboard02.*

class Dashboard02Fragment : Fragment() {


    private lateinit var viewModel: Dashboard02ViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard02, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(Dashboard02ViewModel::class.java)

        viewModel.text.observe(this, Observer {
            text_detail_2.text = it
        })

        viewModel.countValue.observe(this, Observer {
            icon_text_count.text = "Count value: $it"
        })

        viewModel.initializeCount()
    }

}
