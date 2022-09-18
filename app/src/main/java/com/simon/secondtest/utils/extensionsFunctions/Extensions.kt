package com.simon.secondtest.utils.extensionsFunctions

import android.content.Context
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

const val SELECTED_BRAND_KEY = "selected_brand"
const val BRAND_DATASTORE_NAME ="datastore"
const val TEST_BRAND_DATASTORE_NAME ="datastores"

fun View.hide(){
    if(this.visibility==View.VISIBLE)
        this.visibility = View.GONE
}
fun View.show(){
    if(this.visibility==View.INVISIBLE || this.visibility==View.GONE)
        this.visibility = View.VISIBLE
}

fun View.hide2(){
    if(this.visibility==View.VISIBLE)
        this.visibility = View.GONE
}
fun View.show2(){
    if(this.visibility==View.INVISIBLE || this.visibility==View.GONE)
        this.visibility = View.VISIBLE
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = BRAND_DATASTORE_NAME)