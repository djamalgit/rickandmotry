package com.example.rickandmorty

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.api.ApiHelper
import com.example.rickandmorty.api.RetrofitClient
import com.example.rickandmorty.api.RetrofitQuery
import com.example.rickandmorty.viewModel.MainViewModel
import com.example.rickandmorty.viewModel.ViewModelFactory
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private var currentNamePersonage: String = ""
    private lateinit var viewModel: MainViewModel

    lateinit var mService: RetrofitQuery
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: RecyclerViewAdapter
    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbarTop))
        // Диалог загрузки
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(this).build()


        // Задание 1
        rvMain.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        rvMain.layoutManager = layoutManager

        mService = RetrofitClient.getClient

        // Задание 2 почти работает )))
//        setupViewModel("")
//        setupUI()
//        setupObservers()

        //Выборка по статусу персонажа f
        btnAlive.setOnClickListener { getPersonage(currentNamePersonage, "Alive") }
        btnDead.setOnClickListener { getPersonage(currentNamePersonage, "Dead") }
        btnAll.setOnClickListener { getPersonage(currentNamePersonage, "") }
        //btnAll.setOnClickListener {  }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
// Выборка по именам персонажа
        return when (item.itemId) {
            R.id.action_Rick -> {
                currentNamePersonage = "rick"
                getPersonage(currentNamePersonage)
                true
            }
            R.id.action_Morty -> {
                currentNamePersonage = "morty"
                getPersonage(currentNamePersonage)
                true
            }
            R.id.action_All -> {
                currentNamePersonage = ""
                getPersonage(currentNamePersonage)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Основной метод загрузки данных с сайта
    private fun getPersonage(name: String = "", status: String = "") {
        dialog.show()
        mService.getCharacter(name, status).enqueue(object : Callback<CharacterBase> {
            override fun onResponse(call: Call<CharacterBase>, response: Response<CharacterBase>) {
                // println(response.body().toString())
                val tt = response.body() as CharacterBase

                adapter = RecyclerViewAdapter(tt.results)

                rvMain.adapter = adapter
                dialog.dismiss()
            }

            override fun onFailure(call: Call<CharacterBase>, t: Throwable) {
                println("getPersonage ${t.message}")
                dialog.dismiss()
                Toast.makeText(this@MainActivity, "something went wrong", Toast.LENGTH_LONG)
            }
        })
    }


    // Задание 2
    private fun setupViewModel(query: String) {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitClient.getClient, query))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        rvMain.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter(arrayListOf())
        rvMain.addItemDecoration(
            DividerItemDecoration(
                rvMain.context,
                (rvMain.layoutManager as LinearLayoutManager).orientation
            )
        )
        rvMain.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.getCharacter().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {

                        resource.data?.let { character -> retrieveList(character) }
                        dialog.dismiss()
                    }
                    Status.ERROR -> {

                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        println(it.message)
                        dialog.dismiss()
                    }
                    Status.LOADING -> {
                        dialog.show()
                    }
                }
            }
        })
    }

    private fun retrieveList(character: CharacterBase) {
        adapter.apply {
            addCharacter(character.results)
            notifyDataSetChanged()
        }
    }


}




