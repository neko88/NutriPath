package com.group35.nutripath.homemenu.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.group35.nutripath.homemenu.dataobject.BottomDataObject
import com.group35.nutripath.homemenu.dataobject.MiddleDataObject
import com.group35.nutripath.homemenu.dataobject.TopDataObject

class MainHomeViewModel : ViewModel(){
 //   private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _viewObjectTop = MutableLiveData<List<TopDataObject>>()
    private val _viewObjectMiddle = MutableLiveData<MutableList<MiddleDataObject>>()
    private val _viewObjectBottom = MutableLiveData<MutableList<BottomDataObject>>()

    val viewObjectTop: MutableLiveData<List<TopDataObject>> = _viewObjectTop
    val viewObjectMiddle: LiveData<MutableList<MiddleDataObject>> = _viewObjectMiddle
    val viewObjectBottom: LiveData<MutableList<BottomDataObject>> = _viewObjectBottom

    /*
    fun loadTopObjectViewBanner() {
        val Ref = firebaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<TopDataObject>()
                for (child in snapshot.children) {
                    val data = child.getValue(TopDataObject::class.java)
                    if (data != null) {
                        list.add(data!!)
                    }
                }
                _viewObjectTop.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    fun loadMiddleObjectViewBanner() {
        val Ref = firebaseDatabase.getReference("Category")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<MiddleDataObject>()
                for (child in snapshot.children) {
                    val data = child.getValue(MiddleDataObject::class.java)
                    if (data != null) {
                        list.add(data)
                    }
                }
                _viewObjectMiddle.value = list
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    fun loadBottomObjectViewBanner() {
        val Ref = firebaseDatabase.getReference("Items")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<BottomDataObject>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(BottomDataObject::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                    _viewObjectBottom.value = lists
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

*/


}