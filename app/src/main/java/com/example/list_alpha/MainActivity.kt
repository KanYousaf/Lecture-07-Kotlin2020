package com.example.list_alpha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private var wordList: ArrayList<String> = ArrayList()
    private var wordToDefinition: HashMap<String, String> = hashMapOf()
    private var wordToDisplay: String = ""
    private var definitionList: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readGREWords()
        setUpEntries()
    }

    fun readGREWords() {
        val scanner: Scanner = Scanner(resources.openRawResource(R.raw.grewords))
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val pieces = line.split("\t")
            if (pieces.size >= 2) {
                wordList.add(pieces[0])
                wordToDefinition[pieces[0]] = pieces[1]
            }
        }
    }

    fun setUpEntries() {
//        val randomWordIndex = Random().nextInt(wordList.size)
//        wordToDisplay = wordList[randomWordIndex]
        wordToDisplay = wordList[(0..wordList.size - 1).random()]
        wordToDisplayTextView.text = wordToDisplay
        definitionList.clear()
        definitionList.add(wordToDefinition[wordToDisplay]!!)
        wordList.shuffle()
        for (otherWords in wordList.subList(0, 4)) {
            if (otherWords == wordToDisplay || definitionList.size == 5) {
                continue
            }
            definitionList.add(wordToDefinition[otherWords]!!)
        }
        definitionList.shuffle()
        displayDefinitionList.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            definitionList
        )
        dataManipulation()
    }

    fun dataManipulation() {
        displayDefinitionList.setOnItemClickListener { parent, view, position, id ->
            val selectedOption = parent.getItemAtPosition(position).toString()

            if (selectedOption.equals(wordToDefinition[wordToDisplay])) {
                Toast.makeText(this@MainActivity, "You selected Right", Toast.LENGTH_SHORT).show()
                readGREWords()
                setUpEntries()
            } else {
                Toast.makeText(this@MainActivity, "You selected Wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onNextClicked(view: View) {
        val myMainActivityIntent = Intent(this@MainActivity, DetailsActivity::class.java)
        startActivity(myMainActivityIntent)

    }
}
