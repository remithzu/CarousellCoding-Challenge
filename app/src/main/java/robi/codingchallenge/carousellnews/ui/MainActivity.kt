package robi.codingchallenge.carousellnews.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import robi.codingchallenge.carousellnews.R
import robi.codingchallenge.carousellnews.databinding.ActivityMainBinding
import robi.codingchallenge.networks.NetworkState
import robi.codingchallenge.networks.data.News
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var viewModel: NewsViewModel
    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.app_name)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        initListener()
        setupViewModel()
        viewModel.getNews()
    }

    private fun initListener() {
        adapter = Adapter()
        adapter.actionListener = object : Adapter.OnActionListener {
            override fun onAction(result: News) {
                Toast.makeText(this@MainActivity, "Detail", Toast.LENGTH_SHORT).show()
            }
        }

        binding.apply {
            rvContentNews.adapter = adapter
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.more -> {
                        Toast.makeText(this@MainActivity, "More", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }
        adapter.setData(mutableListOf())
    }

    private fun setupViewModel() {
        viewModel.news.observe(this) {
            when (it) {
                is NetworkState.Loading -> {
                    Toast.makeText(this@MainActivity, "Loading", Toast.LENGTH_SHORT).show()
                    adapter.setData(mutableListOf())
                }
                is NetworkState.Success -> {
                    Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
                    it.data?.let { it1 -> adapter.setData(it1) }
                }
                else -> {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    Log.d("News", "response:: ${it.message?.message}")
                }
            }
        }
    }
}