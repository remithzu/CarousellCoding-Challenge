package robi.codingchallenge.carousellnews.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import robi.codingchallenge.carousellnews.R
import robi.codingchallenge.carousellnews.databinding.ActivityMainBinding
import robi.codingchallenge.networks.NetworkState
import robi.codingchallenge.networks.data.News
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var viewModel: NewsViewModel
    lateinit var adapter: Adapter
    private var dataSource: MutableList<News> = mutableListOf()

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.app_name)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            @Suppress("DEPRECATION")
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        val overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_3_dot_menu)
        toolbar.overflowIcon = overflowIcon

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
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = true
                viewModel.getNews()
            }
            rvContentNews.adapter = adapter
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.recent -> {
                        val shuffledDataSource = dataSource.sortedBy { it.timeCreated }
                        getNews(shuffledDataSource.toMutableList())
                        true
                    }
                    R.id.popular -> {
                        val shuffledDataSource = dataSource.sortedWith(compareBy({ it.rank }, { it.timeCreated }))
                        getNews(shuffledDataSource.toMutableList())
                        true
                    }
                    else -> false
                }
            }
        }
        adapter.setData(mutableListOf())
    }

    private fun getNews(shuffle: MutableList<News>) {
        adapter.setData(shuffle)
        dataSource = shuffle
    }

    private fun setupViewModel() {
        viewModel.news.observe(this) {
            when (it) {
                is NetworkState.Loading -> {
                    adapter.setData(mutableListOf())
                }
                is NetworkState.Success -> {
                    it.data?.let {
                        it1 -> adapter.setData(it1)
                        dataSource = it1
                    }
                    if (binding.swipeRefresh.isRefreshing) binding.swipeRefresh.isRefreshing = false
                }
                else -> {
                    Toast.makeText(this@MainActivity, "Error:: ${it.message?.message}", Toast.LENGTH_SHORT).show()
                    Log.d("News", "response:: ${it.message?.message}")
                }
            }
        }
    }
}