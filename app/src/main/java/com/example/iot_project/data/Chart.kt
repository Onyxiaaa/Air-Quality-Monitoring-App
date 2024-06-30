package com.example.iot_project.data

import com.google.gson.annotations.SerializedName

data class Chart(

	@field:SerializedName("Chart")
	val chart: List<ChartItem?>? = null
)

data class ChartItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("avg_pm10")
	val avgPm10: String? = null
)
