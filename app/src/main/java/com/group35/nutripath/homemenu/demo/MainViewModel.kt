package com.group35.nutripath.homemenu.demo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel() : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _categoty = MutableLiveData<MutableList<CategoryModel>>()
    private val _recommend = MutableLiveData<MutableList<ItemsModel>>()

    val banner: MutableLiveData<List<SliderModel>> = _banner
    val category: LiveData<MutableList<CategoryModel>> = _categoty
    val recommend: LiveData<MutableList<ItemsModel>> = _recommend

    fun loadRecommended() {
        val Ref = firebaseDatabase.getReference("Items")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                    _recommend.value = lists
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun loadCategory() {
        val Ref = firebaseDatabase.getReference("Category")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<CategoryModel>()
                for (child in snapshot.children) {
                    val data = child.getValue(CategoryModel::class.java)
                    if (data != null) {
                        list.add(data)
                    }
                }
                _categoty.value = list
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun loadBanner() {
        val Ref = firebaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<SliderModel>()
                for (child in snapshot.children) {
                    val data = child.getValue(SliderModel::class.java)
                    if (data != null) {
                        list.add(data!!)
                    }
                }
                _banner.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}