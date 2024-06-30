package com.example.iot_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.iot_project.data.ChartItem
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FChart.newInstance] factory method to
 * create an instance of this fragment.
 */
class FChart : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val BASE_URL = "https://air-quality-itenas.000webhostapp.com/"
    private val arr_pm10: ArrayList<String> = ArrayList()
    private val arr_date: ArrayList<String> = ArrayList()
    private lateinit var avgpm10value : TextView
    private lateinit var datepm10 : TextView

    interface MyAPI {

        @GET("farhan/last2weeks.php")
        fun getAvg2Weeks(): Call<List<ChartItem>>
    }

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
        val rootView = inflater.inflate(R.layout.fragment_f_chart, container, false)

        getData()

        return rootView
    }

    private fun updateChartView() {
        val aaChartView = view?.findViewById<AAChartView>(R.id.aa_chart_view)

        // Set up AAChartModel with data from the API response
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Bar)
//            .title("Average PM10 Air Quality Last 2 Weeks")
            .backgroundColor("#FFFFFF")
            .dataLabelsEnabled(true)
            .categories(arr_date.toTypedArray())
            .series(arrayOf(
                AASeriesElement()
                    .name("Average PM10")
                    .data(arr_pm10.map { it.toFloatOrNull() ?: 0.0 }.toTypedArray())
            )
            )

        // Draw the chart with AAChartModel
        aaChartView?.aa_drawChartWithChartModel(aaChartModel)
    }

    private fun getData() {
        //region mengambil data API menggunakan retrofit
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyAPI::class.java)
        //endregion

        //region memasukkan data daily ke variabel dari json yang diambil dari API

        api.getAvg2Weeks().enqueue(object : Callback<List<ChartItem>> {
            override fun onResponse(call: Call<List<ChartItem>>, response: Response<List<ChartItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseItemList ->
                        Log.d("Farhan", "$responseItemList")

                        for (item in responseItemList) {
                            arr_pm10.add(item.avgPm10 ?: "0.0")
                            arr_date.add(item.date ?: "N/A")
                        }

                        // Update the chart view with the new data
                        updateChartView()
                    }
                }
            }

            override fun onFailure(call: Call<List<ChartItem>>, t: Throwable) {
                Log.e("API_RESPONSE", "Request failed: ${t.message}", t)
            }
        })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FChart.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FChart().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}