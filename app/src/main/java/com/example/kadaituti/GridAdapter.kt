package com.example.kadaituti

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import io.realm.Realm
import io.realm.kotlin.where
import java.text.SimpleDateFormat
import java.util.*

class GridAdapter(context: Context): BaseAdapter(){
    val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    val mDateManager = DateManager()
    val list:List<Date> = mDateManager.getCalendar()
    class ViewHolder{
        lateinit var dailyText: TextView
        lateinit var weatherIcon: ImageView
        lateinit var tempText: TextView
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var holder = ViewHolder()
        var convert = convertView
        if(convertView ==  null){
            convert = mLayoutInflater.inflate(R.layout.gridview_cell,parent,false)
            holder.dailyText = convert.findViewById(R.id.dailyText)
            holder.weatherIcon = convert.findViewById(R.id.weatherImage)
            holder.tempText = convert.findViewById(R.id.tempText)
            convert.tag = holder
        } else{
            holder = convert?.tag as ViewHolder
        }
        //日付表示
        val dateFormat = SimpleDateFormat("d", Locale.US)
        val _dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        holder.dailyText.text = dateFormat.format(list[position])
        val realm = Realm.getDefaultInstance()
        if(0 == realm.where<WeatherData>().equalTo("dt",_dateFormat.format(list[position])).findAll().size){
            //一致する要素がない場合
            holder.tempText.text = "なし"
        } else {
            holder.tempText.text = realm.where<WeatherData>()
                .equalTo("dt",_dateFormat.format(list[position]))
                .findFirst()?.temp
        }

        return convert
    }
}