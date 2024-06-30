package com.example.iot_project

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FHome.newInstance] factory method to
 * create an instance of this fragment.
 */
class FHome : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mqttAndroidClient: MqttAndroidClient
    private lateinit var _stream_temp : TextView
    private lateinit var _stream_hum : TextView
    private lateinit var _stream_lux : TextView
    private lateinit var _stream_methane : TextView
    private lateinit var _stream_pm10 : TextView
    private lateinit var _stream_pm25 : TextView
    private lateinit var _stream_pm100 : TextView
    private lateinit var _stream_co2 : TextView
    private lateinit var connectionstatus : TextView
    private lateinit var card_methane : CardView
    private lateinit var card_pm10 : CardView
    private lateinit var card_pm25 : CardView
    private lateinit var card_pm100 : CardView
    private lateinit var card_o2 : CardView
    private val thresholdMethane = 10000f
    private val thresholdPM10 = 20f
    private val thresholdPM25 = 150f
    private val thresholdPM100 = 150f  // Contoh, bisa diubah sesuai kebutuhan
    private val thresholdCO2 = 5000f



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
        val rootView = inflater.inflate(R.layout.fragment_f_home, container, false)
        _stream_temp = rootView.findViewById(R.id.nilaitemp)
        _stream_hum = rootView.findViewById(R.id.nilaihum)
        _stream_lux = rootView.findViewById(R.id.nilailux)
        _stream_methane = rootView.findViewById(R.id.nilaimethane)
        _stream_pm10 = rootView.findViewById(R.id.nilaipm10)
        _stream_pm25 = rootView.findViewById(R.id.nilaipm25)
        _stream_pm100 = rootView.findViewById(R.id.nilaipm100)
        _stream_co2 = rootView.findViewById(R.id.nilaico2)
        connectionstatus = rootView.findViewById(R.id.streaming_status)
        card_methane = rootView.findViewById(R.id.cardmethane)
        card_pm10 = rootView.findViewById(R.id.cardpm10)
        card_pm25 = rootView.findViewById(R.id.cardpm25)
        card_pm100 = rootView.findViewById(R.id.cardpm100)
        card_o2 = rootView.findViewById(R.id.cardco2)

        val _btn = rootView.findViewById<Button>(R.id.streaming_connectBroker)
        _btn.setOnClickListener{
            connect(requireContext())
            connectionstatus.text = "Connected"
            Toast.makeText(context,"You are connected", Toast.LENGTH_SHORT).show()
        }
        val _btn2 = rootView.findViewById<Button>(R.id.streaming_publishtext)
        var _input = rootView.findViewById<EditText>(R.id.streaming_textinput)
        _btn2.setOnClickListener {
            publish("ramzi", _input.text.toString())
            Toast.makeText(context, "Data was published", Toast.LENGTH_SHORT).show()
        }
        val _btn3 = rootView.findViewById<Button>(R.id.streaming_disconnectBroker)
        _btn3.setOnClickListener {
            disconnect()
            connectionstatus.text = "Disconnected"
            Toast.makeText(context, "You are disconnected", Toast.LENGTH_SHORT).show()
        }
        return rootView
    }


    fun connect(applicationContext : Context) {
        mqttAndroidClient = MqttAndroidClient ( applicationContext,
            "tcp://broker.hivemq.com",
            "1883" )

        mqttAndroidClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.d("Farhan Al Farisi", "connection lost: " + cause?.message.toString())
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d("Farhan Al Farisi", "message: " + message.toString())
                // Assuming the message is in the format "temp hum brightness methane pm10 pm25 pm100 co2"
                val dataValues = message?.toString()?.split(" ")

                if (dataValues != null && dataValues.size == 8) {
                    val temperature = dataValues[0]
                    val humidity = dataValues[1]
                    val brightness = dataValues[2]
                    val methane = dataValues[3].toFloat()  // Convert to float
                    val pm10 = dataValues[4].toFloat()
                    val pm25 = dataValues[5].toFloat()
                    val pm100 = dataValues[6].toFloat()
                    val co2 = dataValues[7].toFloat()

                    _stream_temp.text = "$temperature"
                    _stream_hum.text = "$humidity"
                    _stream_lux.text = "$brightness"
                    _stream_methane.text = "$methane"
                    _stream_pm10.text = "$pm10"
                    _stream_pm25.text = "$pm25"
                    _stream_pm100.text = "$pm100"
                    _stream_co2.text = "$co2"

                    checkAndChangeColor(card_methane, methane, thresholdMethane)
                    checkAndChangeColor(card_pm10, pm10, thresholdPM10)
                    checkAndChangeColor(card_pm25, pm25, thresholdPM25)
                    checkAndChangeColor(card_pm100, pm100, thresholdPM100)
                    checkAndChangeColor(card_o2, co2, thresholdCO2)

                    Log.d("Farhan Al Farisi", "Temperature: $temperature, Humidity: $humidity, Brightness: $brightness, Methane: $methane, PM10: $pm10, PM25: $pm25, PM100: $pm100, CO2: $co2")
                } else {
                    Log.e("Farhan Al Farisi", "Invalid data format")
                }

            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d("Farhan Al Farisi", "complete: " + token.toString())
            }

        })

        try {
            val token = mqttAndroidClient.connect()
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken){
                    Log.d("Farhan Al Farisi", "Koneksi broker berhasil")
                    subscribe("ramzi")
                    //connectionStatus = true
                    // Give your callback on connection established here
                }
                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    //connectionStatus = false
                    Log.i("Farhan Al Farisi", "Koneksi broker gagal")
                    // Give your callback on connection failure here
                    exception.printStackTrace()
                }
            }
        } catch (e: MqttException) {
            // Give your callback on connection failure here
            e.printStackTrace()
        }

    }
    private fun checkAndChangeColor(cardView: CardView, value: Float, threshold: Float) {
        if (value > threshold) {
            // Change card color to indicate exceeding threshold
            cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        } else {
            // Reset card color to default
            cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
        }
    }
    private fun subscribe(topic: String, qos: Int = 1) {
        try {
            mqttAndroidClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("Farhan Al Farisi", "Subscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d("Farhan Al Farisi", "Failed to subscribe $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun disconnect() {
        try {
            mqttAndroidClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("Farhan Al Farisi", "Disconnected")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d("Farhan Al Farisi", "Failed to disconnect")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }


    private fun unsubscribe(topic: String) {
        try {
            mqttAndroidClient.unsubscribe(topic, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("Farhan Al Farisi", "Unsubscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d("Farhan Al Farisi", "Failed to unsubscribe $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String, msg: String, qos: Int = 1, retained: Boolean = false) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttAndroidClient.publish(topic, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("Farhan Al Farisi", "$msg published to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d("Farhan Al Farisi", "Failed to publish $msg to $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FHome.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                FHome().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}