package com.example.rickandmorty

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.DaggerSimple.AppComponent
import com.example.rickandmorty.DaggerSimple.Computer
import com.example.rickandmorty.DaggerSimple.DaggerAppComponent
import com.example.rickandmorty.api.ApiHelper
import com.example.rickandmorty.api.RetrofitClient
import com.example.rickandmorty.api.RetrofitQuery
import com.example.rickandmorty.model.CharacterBase
import com.example.rickandmorty.model.Results
import com.example.rickandmorty.viewModel.MainViewModel
import com.example.rickandmorty.viewModel.ViewModelFactory
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainApp: Application()
{
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}

val Context.appComponent: AppComponent
get() = when (this)
{
    is MainApp ->appComponent
    else -> this.applicationContext.appComponent
}

class MainActivity : AppCompatActivity() {

    private val tmpCharacters: ArrayList<Results> = ArrayList<Results>()
    private var currentNamePersonage: String = ""
    private lateinit var viewModel: MainViewModel

    lateinit var mService: RetrofitQuery
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: RecyclerViewAdapter
    lateinit var dialog: AlertDialog
    private var page: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbarTop))

        val com: Computer = appComponent.computer

        // Диалог загрузки
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(this).build()


        mService = RetrofitClient.getClient

        setupViewModel()
        setupUI()
        setupObservers()

        btnAlive.setOnClickListener { getPersonage(currentNamePersonage, "Alive") }
        btnDead.setOnClickListener { getPersonage(currentNamePersonage, "Dead") }
        btnAll.setOnClickListener { getPersonage(currentNamePersonage, "") }
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

        if (page > 3) {
            retrieveList(tmpCharacters!!)
            tmpCharacters.clear()
            page = 1
            dialog.dismiss()
            return
        }

        mService.getCharacter(name, status, page).enqueue(object : Callback<CharacterBase> {
            override fun onResponse(call: Call<CharacterBase>, response: Response<CharacterBase>) {

                tmpCharacters!!.addAll((response.body() as CharacterBase).results)
                page += 1
                getPersonage(name, status)
            }

            override fun onFailure(call: Call<CharacterBase>, t: Throwable) {
                println("getPersonage ${t.message}")
                dialog.dismiss()
                Toast.makeText(this@MainActivity, "something went wrong", Toast.LENGTH_LONG)
            }
        })
    }

    // Задание 2
    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitClient.getClient))
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

                        resource.data?.let { character -> retrieveList(character.results) }
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

    private fun retrieveList(character: ArrayList<Results>) {
        adapter.apply {
            addCharacter(character)
            notifyDataSetChanged()
        }
        // title = character.count().toString()
    }
}




