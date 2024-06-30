package com.example.iot_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.iot_project.data.ResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.text.DecimalFormat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FHistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class FHistory : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val BASE_URL = "https://air-quality-itenas.000webhostapp.com/"
    private val today_pm10min: ArrayList<String> = ArrayList()
    private val today_pm10max: ArrayList<String> = ArrayList()
    private val today_pm10avg: ArrayList<String> = ArrayList()
    private val _2weeks_pm10min: ArrayList<String> = ArrayList()
    private val _2weeks_pm10max: ArrayList<String> = ArrayList()
    private val _2weeks_pm10avg: ArrayList<String> = ArrayList()
    private val _10days_pm10min: ArrayList<String> = ArrayList()
    private val _10days_pm10max: ArrayList<String> = ArrayList()
    private val _10days_pm10avg: ArrayList<String> = ArrayList()
    private lateinit var todaymin : TextView
    private lateinit var todaymax : TextView
    private lateinit var todayavg : TextView
    private lateinit var _2weeksmin : TextView
    private lateinit var _2weeksmax : TextView
    private lateinit var _2weeksavg : TextView
    private lateinit var _10daysmin : TextView
    private lateinit var _10daysmax : TextView
    private lateinit var _10daysavg : TextView


    //region Interface API yang digunakan
    interface MyAPI {

        @GET("farhan/daily.php")
        fun getDailyData(): Call<List<ResponseItem>>

        @GET("farhan/2weeks.php")
        fun get2WeeksData(): Call<List<ResponseItem>>

        @GET("/farhan/tendays.php")
        fun get10daysData(): Call<List<ResponseItem>>
    }
    //endregion


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_f_history, container, false)
        getData()
        todaymin = rootView.findViewById(R.id.nilaipm10todaymin)
        todaymax = rootView.findViewById(R.id.nilaipm10todaymax)
        todayavg = rootView.findViewById(R.id.nilaipm10todayaverage)
        _2weeksmin = rootView.findViewById(R.id.nilaipm10weekmin)
        _2weeksmax = rootView.findViewById(R.id.nilaipm10weekmax)
        _2weeksavg = rootView.findViewById(R.id.nilaipm10weekaverage)
        _10daysmin = rootView.findViewById(R.id.nilaipm1010min)
        _10daysmax = rootView.findViewById(R.id.nilaipm1010max)
        _10daysavg = rootView.findViewById(R.id.nilaipm1010average)

        return rootView
    }

    //region fungsi untuk memasukkan data API ke recycle view
    private fun getData() {
        //region mengambil data API menggunakan retrofit
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyAPI::class.java)
        //endregion

        //region memasukkan data daily ke variabel dari json yang diambil dari API
        val decimalFormat = DecimalFormat("#.##")

        api.getDailyData().enqueue(object : Callback<List<ResponseItem>> {
            override fun onResponse(call: Call<List<ResponseItem>>, response: Response<List<ResponseItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseItemList ->
                        Log.d("Farhan", "$responseItemList")

                        for (item in responseItemList) {
                            today_pm10min.add(item.min?.let { decimalFormat.format(it.toDouble()) } ?: "N/A")
                            today_pm10max.add(item.max?.let { decimalFormat.format(it.toDouble()) } ?: "N/A")
                            today_pm10avg.add(item.avg?.let { decimalFormat.format(it.toDouble()) } ?: "N/A")
                        }

                        todaymin.text = today_pm10min.joinToString(", ")
                        todaymax.text = today_pm10max.joinToString(", ")
                        todayavg.text = today_pm10avg.joinToString(", ")
                    }
                }
            }

            override fun onFailure(call: Call<List<ResponseItem>>, t: Throwable) {
                Log.e("API_RESPONSE", "Request failed: ${t.message}", t)
            }
        })

        api.get2WeeksData().enqueue(object : Callback<List<ResponseItem>> {
            override fun onResponse(call: Call<List<ResponseItem>>, response: Response<List<ResponseItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseItemList ->
                        Log.d("Farhan", "$responseItemList")

                        for (item in responseItemList) {
                            _2weeks_pm10min.add(item.min?.let { decimalFormat.format(it.toDouble()) } ?: "N/A")
                            _2weeks_pm10max.add(item.max?.let { decimalFormat.format(it.toDouble()) } ?: "N/A")
                            _2weeks_pm10avg.add(item.avg?.let { decimalFormat.format(it.toDouble()) } ?: "N/A")
                        }

                        _2weeksmin.text = _2weeks_pm10min.joinToString(", ")
                        _2weeksmax.text = _2weeks_pm10max.joinToString(", ")
                        _2weeksavg.text = _2weeks_pm10avg.joinToString(", ")
                    }
                }
            }


            override fun onFailure(call: Call<List<ResponseItem>>, t: Throwable) {
                Log.e("API_RESPONSE", "Request failed: ${t.message}", t)
            }
        })

        api.get10daysData().enqueue(object : Callback<List<ResponseItem>> {
            override fun onResponse(call: Call<List<ResponseItem>>, response: Response<List<ResponseItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseItemList ->
                        Log.d("Farhan", "$responseItemList")

                        for (item in responseItemList) {
                            _10days_pm10min.add(item.min?.let { decimalFormat.format(it.toDouble()) } ?: "N/A")
                            _10days_pm10max.add(item.max?.let { decimalFormat.format(it.toDouble()) } ?: "N/A")
                            _10days_pm10avg.add(item.avg?.let { decimalFormat.format(it.toDouble()) } ?: "N/A")
                        }

                        _10daysmin.text = _10days_pm10min.joinToString(", ")
                        _10daysmax.text = _10days_pm10max.joinToString(", ")
                        _10daysavg.text = _10days_pm10avg.joinToString(", ")
                    }
                }
            }


            override fun onFailure(call: Call<List<ResponseItem>>, t: Throwable) {
                Log.e("API_RESPONSE", "Request failed: ${t.message}", t)
            }
        })
        //endregion
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FHistory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FHistory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}