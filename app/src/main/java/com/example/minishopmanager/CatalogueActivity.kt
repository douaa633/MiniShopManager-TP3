package com.example.minishopmanager

import android.content.ContentValues
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class CatalogueActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogue)

        val lvProduits = findViewById<ListView>(R.id.lvProduits)
        val db = DBHelper(this).writableDatabase

        // 1. Lire produits.json depuis assets
        val json = assets.open("produits.json").bufferedReader().use { it.readText() }
        val array = JSONArray(json)

        // 2. Construire les listes
        val noms = ArrayList<String>()
        val prix = ArrayList<Int>()
        val affichage = ArrayList<String>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            noms.add(obj.getString("nom"))
            prix.add(obj.getInt("prix"))
            affichage.add("${obj.getString("nom")} - ${obj.getInt("prix")} DT")
        }

        // 3. Afficher dans la ListView
        lvProduits.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, affichage)

        // 4. Clic sur un produit : insertion SQLite + Toast
        lvProduits.setOnItemClickListener { _, _, position, _ ->
            val values = ContentValues().apply {
                put("nom", noms[position])
                put("prix", prix[position])
            }
            db.insert("panier", null, values)
            Toast.makeText(this, "${noms[position]} ajouté au panier", Toast.LENGTH_SHORT).show()
        }
    }
}